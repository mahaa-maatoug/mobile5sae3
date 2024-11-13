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

public class UpdateActualiteFragment extends Fragment {

    private EditText etDateActualite;
    private EditText etLieuActualite;
    private EditText etDescriptionActualite;
    private Button btnUpdateActualite;
    private Button btnUploadPhoto;
    private Uri imageUri;
    private static final int PICK_IMAGE_REQUEST = 1;

    private ActualiteDatabase actualiteDatabase; // Room database
    private Actualite existingActualite; // Declare existingActualite here

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
        btnUpdateActualite = view.findViewById(R.id.btnSaveActualite);
        btnUploadPhoto = view.findViewById(R.id.btnUploadPhoto);

        // Handle back button in the toolbar
        ImageButton backButton = view.findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        // Get the existing Actualite from the arguments (if any)
        Bundle args = getArguments();
        if (args != null) {
            existingActualite = (Actualite) args.getSerializable("actualite"); // Retrieve the existing Actualite
            populateFields(existingActualite);
            btnUpdateActualite.setText("Update Actualite");
        }

        // Handle update button click
        btnUpdateActualite.setOnClickListener(v -> updateActualite());
        btnUploadPhoto.setOnClickListener(v -> openFileChooser());

        return view;
    }

    private void populateFields(Actualite actualite) {
        if (actualite != null) {
            etDateActualite.setText(actualite.getDateActualite());
            etLieuActualite.setText(actualite.getTitreActualite());
            etDescriptionActualite.setText(actualite.getDescriptionActualite());
            String photoPath = actualite.getPhotoPath();
            if (photoPath != null && !photoPath.isEmpty()) {
                imageUri = Uri.parse(photoPath);
                // Optionally, load the image here if you want to show it
            }
        }
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null) {
            imageUri = data.getData();
            Toast.makeText(getContext(), "Image selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateActualite() {
        String date = etDateActualite.getText().toString();
        String lieu = etLieuActualite.getText().toString();
        String description = etDescriptionActualite.getText().toString();

        if (!date.isEmpty() && !lieu.isEmpty() && !description.isEmpty()) {
            String photoPath = imageUri != null ? imageUri.toString() : "";

            existingActualite.setDateActualite(date);
            existingActualite.setTitreActualite(lieu);
            existingActualite.setDescriptionActualite(description);
            existingActualite.setPhotoPath(photoPath);

            new AsyncTask<Actualite, Void, Void>() {
                @Override
                protected Void doInBackground(Actualite... actualites) {
                    try {
                        actualiteDatabase.actualiteDao().updateActualite(actualites[0]);
                    } catch (Exception e) {
                        Log.e("UpdateActualiteFragment", "Error updating actualite: ", e);
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    Toast.makeText(getContext(), "Actualite updated successfully", Toast.LENGTH_SHORT).show();
                    getParentFragmentManager().popBackStack();
                }
            }.execute(existingActualite);
        } else {
            Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
        }
    }
}
