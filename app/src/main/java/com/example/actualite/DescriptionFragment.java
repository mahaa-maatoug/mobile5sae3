package com.example.actualite;

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

import com.example.actualite.Database.ActualiteDatabase;
import com.example.actualite.entity.Actualite;

import java.io.InputStream;

public class DescriptionFragment extends Fragment {

  private int actualiteId; // To hold the ID of the actualite for deletion and update
  private Actualite currentActualite; // To hold the current actualite for updating

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.description_actualite, container, false);

    // Retrieve UI elements
    TextView dateActualiteTextView = view.findViewById(R.id.tvDateActualite);
    TextView lieuActualiteTextView = view.findViewById(R.id.tvLieuActualite);
    TextView descriptionActualiteTextView = view.findViewById(R.id.tvDescriptionActualite);
    ImageView actualitePhotoImageView = view.findViewById(R.id.imageActualite); // ImageView for photo
    Button deleteButton = view.findViewById(R.id.btnDeleteActualite); // Reference to the delete button
    Button updateButton = view.findViewById(R.id.btnUpdateActualite); // Reference to the update button

    // Get the ID of the Actualite from the arguments
    Bundle args = getArguments();
    if (args != null) {
      actualiteId = args.getInt("actualiteId"); // Store the ID for later use
      lieuActualiteTextView.setText(String.valueOf(actualiteId)); // Display the ID as text for now

      // Get the database instance and observe the Actualite
      ActualiteDatabase db = ActualiteDatabase.getDatabase(getActivity());
      db.actualiteDao().getActualiteById(actualiteId).observe(getViewLifecycleOwner(), actualite -> {
        if (actualite != null) {
          currentActualite = actualite; // Store the current actualite for updating
          lieuActualiteTextView.setText("Titre : " + actualite.getTitreActualite());
          // Set the TextView values with the retrieved data
          dateActualiteTextView.setText("Date of Actualite: " + actualite.getDateActualite());
          descriptionActualiteTextView.setText("Description: " + actualite.getDescriptionActualite());
          String photoPath = actualite.getPhotoPath();
          if (photoPath != null && !photoPath.isEmpty()) {
            loadImage(photoPath, actualitePhotoImageView);
          } else {
            actualitePhotoImageView.setImageResource(R.drawable.img); // Set a placeholder if no image
          }
        } else {
          Log.d("ActualiteData", "No Actualite found with the given ID.");
        }
      });
    }

    deleteButton.setOnClickListener(v -> deleteActualite());

    // Set up the update button click listener
    updateButton.setOnClickListener(v -> updateActualite());

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

  // Method to delete the Actualite
  private void deleteActualite() {
    if (actualiteId != 0) {
      new Thread(() -> {
        ActualiteDatabase db = ActualiteDatabase.getDatabase(getActivity());
        db.actualiteDao().deleteActualiteById(actualiteId);
        Log.d("DeleteActualite", "Deleted Actualite with ID: " + actualiteId);
        getActivity().runOnUiThread(() -> getActivity().getSupportFragmentManager().popBackStack());
      }).start();
    } else {
      Log.d("DeleteActualite", "Invalid ID for deletion.");
    }
  }
  // Method to update the Actualite by navigating to UpdateActualiteFragment
  private void updateActualite() {
    if (currentActualite != null) {
      // Create a new Bundle and put the currentActualite in it
      Bundle bundle = new Bundle();
      bundle.putSerializable("actualite", currentActualite); // Pass the currentActualite

      // Create an instance of UpdateActualiteFragment and set the arguments
      UpdateActualiteFragment updateActualiteFragment = new UpdateActualiteFragment();
      updateActualiteFragment.setArguments(bundle);

      // Replace the current fragment with UpdateActualiteFragment
      getActivity().getSupportFragmentManager()
              .beginTransaction()
              .replace(R.id.fragment_container, updateActualiteFragment) // Use your container ID
              .addToBackStack(null) // Optional: add to back stack
              .commit();
    } else {
      Log.d("UpdateActualite", "No current actualite to update.");
    }
  }

}
