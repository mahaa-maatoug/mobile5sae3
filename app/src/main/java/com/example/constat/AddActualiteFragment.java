package com.example.constat;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.constat.Database.ActualiteDatabase;
import com.example.constat.entity.Actualite;

public class AddActualiteFragment extends Fragment {

  private EditText etDateActualite;
  private EditText etLieuActualite;
  private EditText etDescriptionActualite;
  private Button btnSaveActualite;
  private Button btnUploadPhoto; // Button for uploading photos
  private Uri imageUri; // To hold the selected image URI
  private static final int PICK_IMAGE_REQUEST = 1; // Unique request code for image picker


  private ActualiteDatabase actualiteDatabase;  // Room database

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.addactualite, container, false);

    // Initialize Room database
    actualiteDatabase = ActualiteDatabase.getDatabase(getContext());

    // Initialize UI components
    etDateActualite = view.findViewById(R.id.etDateActualite);
    etLieuActualite = view.findViewById(R.id.etLieuActualite);
    etDescriptionActualite = view.findViewById(R.id.etDescriptionActualite);
    btnSaveActualite = view.findViewById(R.id.btnSaveActualite);
    btnUploadPhoto = view.findViewById(R.id.btnUploadPhoto); // Initialize upload button

    // Handle back button in the toolbar
    ImageButton backButton = view.findViewById(R.id.back_button);
    backButton.setOnClickListener(v -> getParentFragmentManager().popBackStack());

    // Handle save button click
    btnSaveActualite.setOnClickListener(v -> saveActualite());

    btnUploadPhoto.setOnClickListener(v -> openFileChooser());


    return view;
  }
  private void openFileChooser() {
    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
    intent.setType("image/*"); // Set the type to images only
    intent.addCategory(Intent.CATEGORY_OPENABLE);
    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
  }
  @Override
  public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null) {
      imageUri = data.getData(); // Get the image URI
      // Optionally, you can show a toast or an image preview here
      Toast.makeText(getContext(), "Image selected", Toast.LENGTH_SHORT).show();
    }
  }


  // Method to save the actualite
  private void saveActualite() {
      String date = etDateActualite.getText().toString();
      String titreActualite = etLieuActualite.getText().toString();
      String description = etDescriptionActualite.getText().toString();

      if (!date.isEmpty() && !titreActualite.isEmpty() && !description.isEmpty()) {
          String photoPath = imageUri != null ? imageUri.toString() : ""; // Convert URI to string
          // Initialize newActualite before setting photoPath
          Actualite newActualite = new Actualite(description, titreActualite, date);
          newActualite.setPhotoPath(photoPath); // Set the photo path

          new AsyncTask<Actualite, Void, Void>() {
              @Override
              protected Void doInBackground(Actualite... actualites) {
                  try {
                      actualiteDatabase.actualiteDao().insertActualite(actualites[0]);
                  } catch (Exception e) {
                      Log.e("AddActualiteFragment", "Error inserting actualite: ", e);
                  }
                  return null;
              }

              @Override
              protected void onPostExecute(Void aVoid) {
                  Toast.makeText(getContext(), "Actualite added successfully", Toast.LENGTH_SHORT).show();
                  getParentFragmentManager().popBackStack();
              }
          }.execute(newActualite);
      } else {
          Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
      }
  }



}
