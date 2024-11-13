package com.example.constat;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.constat.Database.ConstatDatabase;
import com.example.constat.entity.Constat;

import java.io.InputStream;
import java.io.Serializable;

public class DescriptionFragment extends Fragment {

  private int constatId; // To hold the ID of the constat for deletion and update
  private Constat currentConstat; // To hold the current constat for updating

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.description_constat, container, false);

    // Retrieve UI elements
    TextView dateAccidentTextView = view.findViewById(R.id.tvDateAccident);
    TextView lieuAccidentTextView = view.findViewById(R.id.tvLieuAccident);
    TextView descriptionAccidentTextView = view.findViewById(R.id.tvDescriptionAccident);
    ImageView accidentPhotoImageView = view.findViewById(R.id.imageAccident); // ImageView for photo
    Button deleteButton = view.findViewById(R.id.btnDeleteConstat); // Reference to the delete button
    Button updateButton = view.findViewById(R.id.btnUpdateConstat); // Reference to the update button

    // Get the ID of the Constat from the arguments
    Bundle args = getArguments();
    if (args != null) {
      constatId = args.getInt("constatId"); // Store the ID for later use
      lieuAccidentTextView.setText(String.valueOf(constatId)); // Display the ID as text for now

      // Get the database instance and observe the Constat
      ConstatDatabase db = ConstatDatabase.getDatabase(getActivity());
      db.constatDao().getConstatById(constatId).observe(getViewLifecycleOwner(), constat -> {
        if (constat != null) {
          currentConstat = constat; // Store the current constat for updating
          Log.d("ConstatData", "Date of Accident: " + constat.getDateAccident());
          Log.d("ConstatData", "Lieu of Accident: " + constat.getLieuAccident());
          Log.d("ConstatData", "Description: " + constat.getDescriptionAccident());

          // Set the TextView values with the retrieved data
          dateAccidentTextView.setText("Date of Accident: " + constat.getDateAccident());
          lieuAccidentTextView.setText("Lieu of Accident: " + constat.getLieuAccident());
          descriptionAccidentTextView.setText("Description: " + constat.getDescriptionAccident());
          String photoPath = constat.getPhotoPath();
          if (photoPath != null && !photoPath.isEmpty()) {
            loadImage(photoPath, accidentPhotoImageView);
          } else {
            accidentPhotoImageView.setImageResource(R.drawable.img); // Set a placeholder if no image
          }
        } else {
          Log.d("ConstatData", "No Constat found with the given ID.");
        }
      });
    }

    deleteButton.setOnClickListener(v -> deleteConstat());

    // Set up the update button click listener
    updateButton.setOnClickListener(v -> updateConstat());

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

  // Method to delete the Constat
  private void deleteConstat() {
    if (constatId != 0) {
      new Thread(() -> {
        ConstatDatabase db = ConstatDatabase.getDatabase(getActivity());
        db.constatDao().deleteConstatById(constatId);
        Log.d("DeleteConstat", "Deleted Constat with ID: " + constatId);
        getActivity().runOnUiThread(() -> getActivity().getSupportFragmentManager().popBackStack());
      }).start();
    } else {
      Log.d("DeleteConstat", "Invalid ID for deletion.");
    }
  }
  // Method to update the Constat by navigating to UpdateConstatFragment
  private void updateConstat() {
    if (currentConstat != null) {
      // Create a new Bundle and put the currentConstat in it
      Bundle bundle = new Bundle();
      bundle.putSerializable("constat", currentConstat); // Pass the currentConstat

      // Create an instance of UpdateConstatFragment and set the arguments
      UpdateConstatFragment updateConstatFragment = new UpdateConstatFragment();
      updateConstatFragment.setArguments(bundle);

      // Replace the current fragment with UpdateConstatFragment
      getActivity().getSupportFragmentManager()
              .beginTransaction()
              .replace(R.id.fragment_container, updateConstatFragment) // Use your container ID
              .addToBackStack(null) // Optional: add to back stack
              .commit();
    } else {
      Log.d("UpdateConstat", "No current constat to update.");
    }
  }

}
