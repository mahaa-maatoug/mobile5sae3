package com.example.sinistre;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sinistre.Database.SinistreDatabase;
import com.example.sinistre.entity.Sinistre;

import java.util.List;

public class SinistreFragment extends Fragment {
  private RecyclerView recyclerView;
  private SinistreAdapter adapter;
  private List<Sinistre> sinistreList;
  private SinistreDatabase sinistreDatabase;  // Room database

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.sinistre, container, false);

    // Initialize RecyclerView
    recyclerView = view.findViewById(R.id.recyclerViewSinistres);
    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

    // Initialize Room database
    sinistreDatabase = SinistreDatabase.getDatabase(getContext());

    // Load sinistres from the database
    loadSinistresFromDatabase();

    // Handle the click for adding a new sinistre
    ImageButton btnAddSinistre = view.findViewById(R.id.add_button);
    btnAddSinistre.setOnClickListener(v -> {
      // Load AddSinistreFragment when the add button is clicked
      loadAddSinistreFragment();
    });

    return view;
  }

  // Load sinistres from Room database
  private void loadSinistresFromDatabase() {
    new AsyncTask<Void, Void, List<Sinistre>>() {
      @Override
      protected List<Sinistre> doInBackground(Void... voids) {
        return sinistreDatabase.sinistreDao().getAllSinistres();
      }

      @Override
      protected void onPostExecute(List<Sinistre> sinistres) {
        // Update the list and adapter
        sinistreList = sinistres;
        adapter = new SinistreAdapter(sinistreList, SinistreFragment.this::showDescriptionFragment);
        recyclerView.setAdapter(adapter);
      }
    }.execute();
  }

  // Add a new sinistre to the Room database and the RecyclerView
  public void addNewSinistre(Sinistre newSinistre) {
    new AsyncTask<Sinistre, Void, Void>() {
      @Override
      protected Void doInBackground(Sinistre... sinistres) {
        sinistreDatabase.sinistreDao().insertSinistre(sinistres[0]);
        return null;
      }

      @Override
      protected void onPostExecute(Void aVoid) {
        // Update the UI after the sinistre is inserted
        loadSinistresFromDatabase();
      }
    }.execute(newSinistre);
  }

  // Function to load AddSinistreFragment
  private void loadAddSinistreFragment() {
    AddSinistreFragment addSinistreFragment = new AddSinistreFragment();
    FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
    transaction.replace(R.id.fragment_container, addSinistreFragment);
    transaction.addToBackStack(null);
    transaction.commit();
  }

  // Function to show the DescriptionFragment when a sinistre is clicked
  private void showDescriptionFragment(Sinistre sinistre) {
    DescriptionFragment descriptionFragment = new DescriptionFragment();
    Bundle bundle = new Bundle();
    bundle.putInt("sinistreId", sinistre.getId()); // Use "sinistreId" as the key to match the retrieval in DescriptionFragment
    bundle.putString("date_sinistre", sinistre.getDateSinistre());
    bundle.putString("nom_sinistre", sinistre.getNomSinistre());
    bundle.putString("description_sinistre", sinistre.getDescriptionSinistre());
    descriptionFragment.setArguments(bundle);

    FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
    transaction.replace(R.id.fragment_container, descriptionFragment);
    transaction.addToBackStack(null);
    transaction.commit();
  }
}
