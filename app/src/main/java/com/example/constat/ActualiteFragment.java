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

import com.example.constat.Database.ActualiteDatabase;
import com.example.constat.entity.Actualite;

import java.util.List;

public class ActualiteFragment extends Fragment {
  private RecyclerView recyclerView;
  private ActualiteAdapter adapter;
  private List<Actualite> actualiteList;
  private ActualiteDatabase actualiteDatabase;  // Room database

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.actualite, container, false);

    // Initialize RecyclerView
    recyclerView = view.findViewById(R.id.recyclerViewActualites);
    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

    // Initialize Room database
    actualiteDatabase = ActualiteDatabase.getDatabase(getContext());

    // Load actualites from the database
    loadActualitesFromDatabase();

    // Handle the click for adding a new actualite
    ImageButton btnAddActualite = view.findViewById(R.id.add_button);
    btnAddActualite.setOnClickListener(v -> {
      // Load AddActualiteFragment when the add button is clicked
      loadAddActualiteFragment();
    });

    return view;
  }

  // Load actualites from Room database
  private void loadActualitesFromDatabase() {
    new AsyncTask<Void, Void, List<Actualite>>() {
      @Override
      protected List<Actualite> doInBackground(Void... voids) {
        return actualiteDatabase.actualiteDao().getAllActualites();
      }

      @Override
      protected void onPostExecute(List<Actualite> actualites) {
        // Update the list and adapter
        actualiteList = actualites;
        adapter = new ActualiteAdapter(actualiteList, ActualiteFragment.this::showDescriptionFragment);
        recyclerView.setAdapter(adapter);
      }
    }.execute();
  }

  // Add a new actualite to the Room database and the RecyclerView
  public void addNewActualite(Actualite newActualite) {
    new AsyncTask<Actualite, Void, Void>() {
      @Override
      protected Void doInBackground(Actualite... actualites) {
        actualiteDatabase.actualiteDao().insertActualite(actualites[0]);
        return null;
      }

      @Override
      protected void onPostExecute(Void aVoid) {
        // Update the UI after the actualite is inserted
        loadActualitesFromDatabase();
      }
    }.execute(newActualite);
  }

  // Function to load AddActualiteFragment
  private void loadAddActualiteFragment() {
    AddActualiteFragment addActualiteFragment = new AddActualiteFragment();
    FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
    transaction.replace(R.id.fragment_container, addActualiteFragment);
    transaction.addToBackStack(null);
    transaction.commit();
  }

  // Function to show the DescriptionActualiteFragment when a actualite is clicked
  private void showDescriptionFragment(Actualite actualite) {
    DescriptionActualiteFragment descriptionActualiteFragment = new DescriptionActualiteFragment();
    Bundle bundle = new Bundle();
    bundle.putInt("actualiteId", actualite.getId()); // Use "actualiteId" as the key to match the retrieval in DescriptionActualiteFragment
    bundle.putString("date_actualite", actualite.getDateActualite());
    bundle.putString("lieu_actualite", actualite.getTitreActualite());
    bundle.putString("description_actualite", actualite.getDescriptionActualite());
    descriptionActualiteFragment.setArguments(bundle);

    FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
    transaction.replace(R.id.fragment_container, descriptionActualiteFragment);
    transaction.addToBackStack(null);
    transaction.commit();
  }
}
