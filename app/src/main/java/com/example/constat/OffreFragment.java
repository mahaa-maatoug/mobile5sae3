package com.example.constat;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.constat.Database.OffreDatabase;
import com.example.constat.entity.Offre;



import java.util.List;

public class OffreFragment extends Fragment {

  private RecyclerView recyclerView;
  private OffreAdapter adapter;
  private List<Offre> offreList;
  private OffreDatabase offreDatabase; // Room database
  private SearchView searchView; // Utilisez androidx.appcompat.widget.SearchView

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.offre, container, false);

    // Initialize RecyclerView
    recyclerView = view.findViewById(R.id.recyclerViewOffers);
    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

    // Initialize Room database
    offreDatabase = OffreDatabase.getDatabase(getContext());

    // Initialize SearchView
    searchView = view.findViewById(R.id.searchView);
    setupSearchView();

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

  // Set up the SearchView to filter the offers
  private void setupSearchView() {
    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
      @Override
      public boolean onQueryTextSubmit(String query) {
        // Filtre la liste lorsque l'utilisateur soumet la recherche
        if (adapter != null) {
          adapter.filter(query);
        }
        return false;
      }

      @Override
      public boolean onQueryTextChange(String newText) {
        // Filtre la liste en temps réel lorsque l'utilisateur tape du texte
        if (adapter != null) {
          adapter.filter(newText);
        }
        return false;
      }
    });
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

  // Function to show the DescriptionOffreFragment when an offer is clicked
  private void showDescriptionFragment(Offre offre) {
    DescriptionOffreFragment descriptionOffreFragment = new DescriptionOffreFragment();
    Bundle bundle = new Bundle();
    bundle.putInt("OffreId", offre.getId());
    bundle.putDouble("price", offre.getPrice());
    bundle.putString("type", offre.getType());
    bundle.putString("date", offre.getDate());
    bundle.putString("description", offre.getDescription());
    descriptionOffreFragment.setArguments(bundle);

    FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
    transaction.replace(R.id.fragment_container, descriptionOffreFragment);
    transaction.addToBackStack(null);
    transaction.commit();
  }
}
