package com.example.sinistre;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.sinistre.Database.SinistreDatabase;
import com.example.sinistre.entity.Sinistre;

import java.io.InputStream;

public class DescriptionFragment extends Fragment {

  private int sinistreId; // To hold the ID of the sinistre for deletion and update
  private Sinistre currentSinistre; // To hold the current sinistre for updating

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.description_sinistre, container, false);

    // Retrieve UI elements
    TextView dateSinistreTextView = view.findViewById(R.id.tvdateSinistre);
    TextView nomSinistreTextView = view.findViewById(R.id.tvNomSinistre);
    TextView descriptionSinistreTextView = view.findViewById(R.id.tvDescriptionSinistre);
    ImageView sinistrePhotoImageView = view.findViewById(R.id.imageSinistre); // ImageView for photo
    Button deleteButton = view.findViewById(R.id.btnDeleteSinistre); // Reference to the delete button
    Button updateButton = view.findViewById(R.id.btnUpdateSinistre); // Reference to the update button

    // Get the ID of the Sinistre from the arguments
    Bundle args = getArguments();
    if (args != null) {
      sinistreId = args.getInt("sinistreId"); // Store the ID for later use
      nomSinistreTextView.setText(String.valueOf(sinistreId)); // Display the ID as text for now

      // Get the database instance and observe the Sinistre
      SinistreDatabase db = SinistreDatabase.getDatabase(getActivity());
      db.sinistreDao().getSinistreById(sinistreId).observe(getViewLifecycleOwner(), sinistre -> {
        if (sinistre != null) {
          currentSinistre = sinistre; // Store the current sinistre for updating
          Log.d("SinistreData", "Date of Sinistre: " + sinistre.getDateSinistre());
          Log.d("SinistreData", "Nom: " + sinistre.getNomSinistre());
          Log.d("SinistreData", "Description: " + sinistre.getDescriptionSinistre());

          // Set the TextView values with the retrieved data
          dateSinistreTextView.setText("Date of Sinistre: " + sinistre.getDateSinistre());
          nomSinistreTextView.setText("Nom: " + sinistre.getNomSinistre());
          descriptionSinistreTextView.setText("Description: " + sinistre.getDescriptionSinistre());
          String photoPath = sinistre.getPhotoPath();
          if (photoPath != null && !photoPath.isEmpty()) {
            loadImage(photoPath, sinistrePhotoImageView);
          } else {
            sinistrePhotoImageView.setImageResource(R.drawable.img); // Set a placeholder if no image
          }
        } else {
          Log.d("SinistreData", "No Sinistre found with the given ID.");
        }
      });
    }

    deleteButton.setOnClickListener(v -> deleteSinistre());

    // Set up the update button click listener
    updateButton.setOnClickListener(v -> updateSinistre());

    return view;
  }

  // Method to load the image from the URI
  private void loadImage(String photoPath, ImageView imageView) {
    try {
      Uri imageUri = Uri.parse(photoPath); // Parse the photo path to a Uri
      InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri); // Open the input stream
      Bitmap bitmap = BitmapFactory.decodeStream(imageStream); // Decode the stream to a Bitmap
      imageView.setImageBitmap(bitmap); // Set the bitmap to the ImageView
    } catch (Exception e) {
      Log.e("ImageLoadError", "Error loading image: " + e.getMessage());
    }
  }

  // Method to delete the Sinistre
  private void deleteSinistre() {
    if (sinistreId != 0) {
      new Thread(() -> {
        SinistreDatabase db = SinistreDatabase.getDatabase(getActivity());
        db.sinistreDao().deleteSinistreById(sinistreId);
        Log.d("DeleteSinistre", "Deleted Sinistre with ID: " + sinistreId);
        getActivity().runOnUiThread(() -> getActivity().getSupportFragmentManager().popBackStack());
      }).start();
    } else {
      Log.d("DeleteSinistre", "Invalid ID for deletion.");
    }
  }
  // Method to update the Sinistre by navigating to UpdateSinistreFragment
  private void updateSinistre() {
    if (currentSinistre != null) {
      // Create a new Bundle and put the currentSinistre in it
      Bundle bundle = new Bundle();
      bundle.putSerializable("sinistre", currentSinistre); // Pass the currentSinistre

      // Create an instance of UpdateSinistreFragment and set the arguments
      UpdateSinistreFragment updateSinistreFragment = new UpdateSinistreFragment();
      updateSinistreFragment.setArguments(bundle);

      // Replace the current fragment with UpdateSinistreFragment
      getActivity().getSupportFragmentManager()
              .beginTransaction()
              .replace(R.id.fragment_container, updateSinistreFragment) // Use your container ID
              .addToBackStack(null) // Optional: add to back stack
              .commit();
    } else {
      Log.d("UpdateSinistre", "No current sinistre to update.");
    }
  }

}
