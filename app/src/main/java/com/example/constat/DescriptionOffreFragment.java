package com.example.constat;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.constat.Database.OffreDatabase;
import com.example.constat.entity.Offre;

public class DescriptionOffreFragment extends Fragment {

  private int offreId; // To hold the ID of the offer for deletion and update
  private Offre currentOffre; // To hold the current offer for updating


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.description, container, false);

    // Retrieve UI elements
    TextView priceTextView = view.findViewById(R.id.tvPrice);
    TextView typeTextView = view.findViewById(R.id.tvType);
    TextView dateTextView = view.findViewById(R.id.tvDate);
    TextView descriptionTextView = view.findViewById(R.id.tvDescription);
    ImageView offreImageView = view.findViewById(R.id.imageOffre); // ImageView for photo
    Button deleteButton = view.findViewById(R.id.btnDeleteOffre); // Reference to the delete button
    Button updateButton = view.findViewById(R.id.btnUpdateOffre); // Reference to the update button

    // Get the ID of the Offer from the arguments
    Bundle args = getArguments();
    if (args != null) {
      offreId = args.getInt("OffreId"); // Store the ID for later use
      Log.d("OffreData", "Retrieved Offer ID: " + offreId); // Display the ID as text for now

      // Get the database instance and observe the Offer
      OffreDatabase db = OffreDatabase.getDatabase(getActivity());
      db.offreDao().getOffreById(offreId).observe(getViewLifecycleOwner(), offre -> {
        if (offre != null) {
          currentOffre = offre; // Store the current offre for updating
          Log.d("OffreData", "Price: " + offre.getPrice());
          Log.d("OffreData", "Type: " + offre.getType());
          Log.d("OffreData", "Description: " + offre.getDescription());
          Log.d("OffreData", "Date: " + offre.getDate());

          // Set the TextView values with the retrieved data
          priceTextView.setText("Price: $" + offre.getPrice());
          typeTextView.setText("Type: " + offre.getType());
          dateTextView.setText("Date: " + offre.getDate());
          descriptionTextView.setText("Description: " + offre.getDescription());

          // Assuming you don't have a specific image path for the offer
          // If there is an image path, load it as shown in the previous implementation
          offreImageView.setImageResource(R.drawable.img); // Placeholder image
        } else {
          Log.d("OffreData", "No Offer found with the given ID.");
        }
      });
    }

    // Set up the delete button click listener
    deleteButton.setOnClickListener(v -> deleteOffer());

    // Set up the update button click listener
    updateButton.setOnClickListener(v -> updateOffer());

    return view;
  }

  // Method to delete the Offer
  private void deleteOffer() {
    if (offreId != 0) {
      new Thread(() -> {
        OffreDatabase db = OffreDatabase.getDatabase(getActivity());
        db.offreDao().deleteById(offreId);
        Log.d("DeleteOffer", "Deleted Offer with ID: " + offreId);
        getActivity().runOnUiThread(() -> getActivity().getSupportFragmentManager().popBackStack());
      }).start();
    } else {
      Log.d("DeleteOffer", "Invalid ID for deletion.");
    }
  }

  // Method to update the Offer by navigating to UpdateOfferFragment
  private void updateOffer() {
    if (currentOffre != null) {
      // Create a new Bundle and put the currentOffer in it
      Bundle bundle = new Bundle();
      bundle.putSerializable("offre", currentOffre); // Pass the currentOffer

      // Create an instance of UpdateOfferFragment and set the arguments
      UpdateOffreFragment updateOfferFragment = new UpdateOffreFragment();
      updateOfferFragment.setArguments(bundle);

      // Replace the current fragment with UpdateOfferFragment
      getActivity().getSupportFragmentManager()
        .beginTransaction()
        .replace(R.id.fragment_container, updateOfferFragment) // Use your container ID
        .addToBackStack(null) // Optional: add to back stack
        .commit();
    } else {
      Log.d("UpdateOffer", "No current offer to update.");
    }
  }
}
