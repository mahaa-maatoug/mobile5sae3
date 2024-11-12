package com.example.sinistre;

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

import com.example.sinistre.Database.SinistreDatabase;
import com.example.sinistre.entity.Sinistre;

public class AddSinistreFragment extends Fragment {

  private EditText etdateSinistre;
  private EditText etNomSinistre;
  private EditText etDescriptionSinistre;
  private Button btnSaveSinistre;
  private Button btnUploadPhoto; // Button for uploading photos
  private Uri imageUri; // To hold the selected image URI
  private static final int PICK_IMAGE_REQUEST = 1; // Unique request code for image picker


  private SinistreDatabase sinistreDatabase;  // Room database

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.addsinistre, container, false);

    // Initialize Room database
    sinistreDatabase = SinistreDatabase.getDatabase(getContext());

    // Initialize UI components
    etdateSinistre = view.findViewById(R.id.etdateSinistre);
    etNomSinistre = view.findViewById(R.id.etNomSinistre);
    etDescriptionSinistre = view.findViewById(R.id.etDescriptionSinistre);
    btnSaveSinistre = view.findViewById(R.id.btnSaveSinistre);
    btnUploadPhoto = view.findViewById(R.id.btnUploadPhoto); // Initialize upload button

    // Handle back button in the toolbar
    ImageButton backButton = view.findViewById(R.id.back_button);
    backButton.setOnClickListener(v -> getParentFragmentManager().popBackStack());

    // Handle save button click
    btnSaveSinistre.setOnClickListener(v -> saveSinistre());

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


  // Method to save the sinistre
  private void saveSinistre() {
      String date = etdateSinistre.getText().toString();
      String lieu = etNomSinistre.getText().toString();
      String description = etDescriptionSinistre.getText().toString();

      if (!date.isEmpty() && !lieu.isEmpty() && !description.isEmpty()) {
          String photoPath = imageUri != null ? imageUri.toString() : ""; // Convert URI to string
          // Initialize newSinistre before setting photoPath
          Sinistre newSinistre = new Sinistre(date, lieu,  description);
          newSinistre.setPhotoPath(photoPath); // Set the photo path

          new AsyncTask<Sinistre, Void, Void>() {
              @Override
              protected Void doInBackground(Sinistre... sinistres) {
                  try {
                      sinistreDatabase.sinistreDao().insertSinistre(sinistres[0]);
                  } catch (Exception e) {
                      Log.e("AddSinistreFragment", "Error inserting sinistre: ", e);
                  }
                  return null;
              }

              @Override
              protected void onPostExecute(Void aVoid) {
                  Toast.makeText(getContext(), "Sinistre added successfully", Toast.LENGTH_SHORT).show();
                  getParentFragmentManager().popBackStack();
              }
          }.execute(newSinistre);
      } else {
          Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
      }
  }



}
