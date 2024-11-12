package com.example.mahaassure;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mahaassure.Database.OffreDatabase;
import com.example.mahaassure.entity.Offre;

import java.util.ArrayList;
import java.util.List;

public class OffreFragment extends Fragment {
  private RecyclerView recyclerView;
  private OffreAdapter adapter;
  private List<Offre> offreList;
  private OffreDatabase offreDatabase;  // Room database

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.offre, container, false);

    // Initialize RecyclerView
    recyclerView = view.findViewById(R.id.recyclerViewOffers);
    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

    // Initialize Room database
    offreDatabase = OffreDatabase.getDatabase(getContext());

    // Load offers from the database
    loadOffersFromDatabase();

    // Handle the click for adding a new offer
    ImageButton btnAddOffer = view.findViewById(R.id.add_button);
    btnAddOffer.setOnClickListener(v -> {
      // Load AddOfferFragment when the add button is clicked
      loadAddOfferFragment();
    });

    return view;
  }

  // Load offers from Room database
  private void loadOffersFromDatabase() {
    new AsyncTask<Void, Void, List<Offre>>() {
      @Override
      protected List<Offre> doInBackground(Void... voids) {
        return offreDatabase.offreDao().getAllOffres();
      }

      @Override
      protected void onPostExecute(List<Offre> offres) {
        // Update the list and adapter
        offreList = offres;
        adapter = new OffreAdapter(offreList, OffreFragment.this::showDescriptionFragment);
        recyclerView.setAdapter(adapter);
      }
    }.execute();
  }

  // Add a new offer to the Room database and the RecyclerView
  public void addNewOffer(Offre newOffer) {
    new AsyncTask<Offre, Void, Void>() {
      @Override
      protected Void doInBackground(Offre... offres) {
        offreDatabase.offreDao().insertOffre(offres[0]);
        return null;
      }

      @Override
      protected void onPostExecute(Void aVoid) {
        // Update the UI after the offer is inserted
        loadOffersFromDatabase();
      }
    }.execute(newOffer);
  }

  // Function to load AddOfferFragment
  private void loadAddOfferFragment() {
    AddOfferFragment addOfferFragment = new AddOfferFragment();
    FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
    transaction.replace(R.id.fragment_container, addOfferFragment);
    transaction.addToBackStack(null);
    transaction.commit();
  }

  // Function to show the DescriptionFragment when an offer is clicked
  private void showDescriptionFragment(Offre offre) {
    DescriptionFragment descriptionFragment = new DescriptionFragment();
    Bundle bundle = new Bundle();
    bundle.putDouble("price", offre.getPrice());
    bundle.putString("type", offre.getType());
    bundle.putString("date", offre.getDate());
    bundle.putString("description", offre.getDescription()); // Ajoutez cette ligne pour passer la description
    descriptionFragment.setArguments(bundle);

    FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
    transaction.replace(R.id.fragment_container, descriptionFragment);
    transaction.addToBackStack(null);
    transaction.commit();
  }
}
