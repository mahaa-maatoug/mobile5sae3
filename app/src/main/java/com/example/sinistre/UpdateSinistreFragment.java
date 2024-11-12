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

public class UpdateSinistreFragment extends Fragment {

    private EditText etdateSinistre;
    private EditText etNomSinistre;
    private EditText etDescriptionSinistre;
    private Button btnUpdateSinistre;
    private Button btnUploadPhoto;
    private Uri imageUri;
    private static final int PICK_IMAGE_REQUEST = 1;

    private SinistreDatabase sinistreDatabase; // Room database
    private Sinistre existingSinistre; // Declare existingSinistre here

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
        btnUpdateSinistre = view.findViewById(R.id.btnSaveSinistre);
        btnUploadPhoto = view.findViewById(R.id.btnUploadPhoto);

        // Handle back button in the toolbar
        ImageButton backButton = view.findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        // Get the existing Sinistre from the arguments (if any)
        Bundle args = getArguments();
        if (args != null) {
            existingSinistre = (Sinistre) args.getSerializable("sinistre"); // Retrieve the existing Sinistre
            populateFields(existingSinistre);
            btnUpdateSinistre.setText("Update Sinistre");
        }

        // Handle update button click
        btnUpdateSinistre.setOnClickListener(v -> updateSinistre());
        btnUploadPhoto.setOnClickListener(v -> openFileChooser());

        return view;
    }

    private void populateFields(Sinistre sinistre) {
        if (sinistre != null) {
            etdateSinistre.setText(sinistre.getDateSinistre());
            etNomSinistre.setText(sinistre.getNomSinistre());
            etDescriptionSinistre.setText(sinistre.getDescriptionSinistre());
            String photoPath = sinistre.getPhotoPath();
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

    private void updateSinistre() {
        String date = etdateSinistre.getText().toString();
        String lieu = etNomSinistre.getText().toString();
        String description = etDescriptionSinistre.getText().toString();

        if (!date.isEmpty() && !lieu.isEmpty() && !description.isEmpty()) {
            String photoPath = imageUri != null ? imageUri.toString() : "";

            existingSinistre.setdateSinistre(date);
            existingSinistre.setNomSinistre(lieu);
            existingSinistre.setDescriptionSinistre(description);
            existingSinistre.setPhotoPath(photoPath);

            new AsyncTask<Sinistre, Void, Void>() {
                @Override
                protected Void doInBackground(Sinistre... sinistres) {
                    try {
                        sinistreDatabase.sinistreDao().updateSinistre(sinistres[0]);
                    } catch (Exception e) {
                        Log.e("UpdateSinistreFragment", "Error updating sinistre: ", e);
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    Toast.makeText(getContext(), "Sinistre updated successfully", Toast.LENGTH_SHORT).show();
                    getParentFragmentManager().popBackStack();
                }
            }.execute(existingSinistre);
        } else {
            Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
        }
    }
}
