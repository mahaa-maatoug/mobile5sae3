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

import com.example.constat.Database.ConstatDatabase;
import com.example.constat.entity.Constat;

public class AddConstatFragment extends Fragment {

  private EditText etDateAccident;
  private EditText etLieuAccident;
  private EditText etDescriptionAccident;
  private Button btnSaveConstat;
  private Button btnUploadPhoto; // Button for uploading photos
  private Uri imageUri; // To hold the selected image URI
  private static final int PICK_IMAGE_REQUEST = 1; // Unique request code for image picker


  private ConstatDatabase constatDatabase;  // Room database

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.addconstat, container, false);

    // Initialize Room database
    constatDatabase = ConstatDatabase.getDatabase(getContext());

    // Initialize UI components
    etDateAccident = view.findViewById(R.id.etDateAccident);
    etLieuAccident = view.findViewById(R.id.etLieuAccident);
    etDescriptionAccident = view.findViewById(R.id.etDescriptionAccident);
    btnSaveConstat = view.findViewById(R.id.btnSaveConstat);
    btnUploadPhoto = view.findViewById(R.id.btnUploadPhoto); // Initialize upload button

    // Handle back button in the toolbar
    ImageButton backButton = view.findViewById(R.id.back_button);
    backButton.setOnClickListener(v -> getParentFragmentManager().popBackStack());

    // Handle save button click
    btnSaveConstat.setOnClickListener(v -> saveConstat());

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


  // Method to save the constat
  private void saveConstat() {
      String date = etDateAccident.getText().toString();
      String lieu = etLieuAccident.getText().toString();
      String description = etDescriptionAccident.getText().toString();

      if (!date.isEmpty() && !lieu.isEmpty() && !description.isEmpty()) {
          String photoPath = imageUri != null ? imageUri.toString() : ""; // Convert URI to string
          // Initialize newConstat before setting photoPath
          Constat newConstat = new Constat(date, lieu, "", "", "", description);
          newConstat.setPhotoPath(photoPath); // Set the photo path

          new AsyncTask<Constat, Void, Void>() {
              @Override
              protected Void doInBackground(Constat... constats) {
                  try {
                      constatDatabase.constatDao().insertConstat(constats[0]);
                  } catch (Exception e) {
                      Log.e("AddConstatFragment", "Error inserting constat: ", e);
                  }
                  return null;
              }

              @Override
              protected void onPostExecute(Void aVoid) {
                  Toast.makeText(getContext(), "Constat added successfully", Toast.LENGTH_SHORT).show();
                  getParentFragmentManager().popBackStack();
              }
          }.execute(newConstat);
      } else {
          Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
      }
  }



}
