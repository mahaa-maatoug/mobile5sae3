package com.example.constat;

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

import com.example.constat.Database.ConstatDatabase;
import com.example.constat.entity.Constat;

import java.util.List;

public class ConstatFragment extends Fragment {
  private RecyclerView recyclerView;
  private ConstatAdapter adapter;
  private List<Constat> constatList;
  private ConstatDatabase constatDatabase;  // Room database

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.constat, container, false);

    // Initialize RecyclerView
    recyclerView = view.findViewById(R.id.recyclerViewConstats);
    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

    // Initialize Room database
    constatDatabase = ConstatDatabase.getDatabase(getContext());

    // Load constats from the database
    loadConstatsFromDatabase();

    // Handle the click for adding a new constat
    ImageButton btnAddConstat = view.findViewById(R.id.add_button);
    btnAddConstat.setOnClickListener(v -> {
      // Load AddConstatFragment when the add button is clicked
      loadAddConstatFragment();
    });

    return view;
  }

  // Load constats from Room database
  private void loadConstatsFromDatabase() {
    new AsyncTask<Void, Void, List<Constat>>() {
      @Override
      protected List<Constat> doInBackground(Void... voids) {
        return constatDatabase.constatDao().getAllConstats();
      }

      @Override
      protected void onPostExecute(List<Constat> constats) {
        // Update the list and adapter
        constatList = constats;
        adapter = new ConstatAdapter(constatList, ConstatFragment.this::showDescriptionFragment);
        recyclerView.setAdapter(adapter);
      }
    }.execute();
  }

  // Add a new constat to the Room database and the RecyclerView
  public void addNewConstat(Constat newConstat) {
    new AsyncTask<Constat, Void, Void>() {
      @Override
      protected Void doInBackground(Constat... constats) {
        constatDatabase.constatDao().insertConstat(constats[0]);
        return null;
      }

      @Override
      protected void onPostExecute(Void aVoid) {
        // Update the UI after the constat is inserted
        loadConstatsFromDatabase();
      }
    }.execute(newConstat);
  }

  // Function to load AddConstatFragment
  private void loadAddConstatFragment() {
    AddConstatFragment addConstatFragment = new AddConstatFragment();
    FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
    transaction.replace(R.id.fragment_container, addConstatFragment);
    transaction.addToBackStack(null);
    transaction.commit();
  }

  // Function to show the DescriptionFragment when a constat is clicked
  private void showDescriptionFragment(Constat constat) {
    DescriptionFragment descriptionFragment = new DescriptionFragment();
    Bundle bundle = new Bundle();
    bundle.putInt("constatId", constat.getId()); // Use "constatId" as the key to match the retrieval in DescriptionFragment
    bundle.putString("date_accident", constat.getDateAccident());
    bundle.putString("lieu_accident", constat.getLieuAccident());
    bundle.putString("description_accident", constat.getDescriptionAccident());
    descriptionFragment.setArguments(bundle);

    FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
    transaction.replace(R.id.fragment_container, descriptionFragment);
    transaction.addToBackStack(null);
    transaction.commit();
  }
}
