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

import java.io.Serializable; // Make sure to import Serializable

public class UpdateConstatFragment extends Fragment {

    private EditText etDateAccident;
    private EditText etLieuAccident;
    private EditText etDescriptionAccident;
    private Button btnUpdateConstat;
    private Button btnUploadPhoto;
    private Uri imageUri;
    private static final int PICK_IMAGE_REQUEST = 1;

    private ConstatDatabase constatDatabase; // Room database
    private Constat existingConstat; // Declare existingConstat here

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
        btnUpdateConstat = view.findViewById(R.id.btnSaveConstat);
        btnUploadPhoto = view.findViewById(R.id.btnUploadPhoto);

        // Handle back button in the toolbar
        ImageButton backButton = view.findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        // Get the existing Constat from the arguments (if any)
        Bundle args = getArguments();
        if (args != null) {
            existingConstat = (Constat) args.getSerializable("constat"); // Retrieve the existing Constat
            populateFields(existingConstat);
            btnUpdateConstat.setText("Update Constat");
        }

        // Handle update button click
        btnUpdateConstat.setOnClickListener(v -> updateConstat());
        btnUploadPhoto.setOnClickListener(v -> openFileChooser());

        return view;
    }

    private void populateFields(Constat constat) {
        if (constat != null) {
            etDateAccident.setText(constat.getDateAccident());
            etLieuAccident.setText(constat.getLieuAccident());
            etDescriptionAccident.setText(constat.getDescriptionAccident());
            String photoPath = constat.getPhotoPath();
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

    private void updateConstat() {
        String date = etDateAccident.getText().toString();
        String lieu = etLieuAccident.getText().toString();
        String description = etDescriptionAccident.getText().toString();

        if (!date.isEmpty() && !lieu.isEmpty() && !description.isEmpty()) {
            String photoPath = imageUri != null ? imageUri.toString() : "";

            existingConstat.setDateAccident(date);
            existingConstat.setLieuAccident(lieu);
            existingConstat.setDescriptionAccident(description);
            existingConstat.setPhotoPath(photoPath);

            new AsyncTask<Constat, Void, Void>() {
                @Override
                protected Void doInBackground(Constat... constats) {
                    try {
                        constatDatabase.constatDao().updateConstat(constats[0]);
                    } catch (Exception e) {
                        Log.e("UpdateConstatFragment", "Error updating constat: ", e);
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    Toast.makeText(getContext(), "Constat updated successfully", Toast.LENGTH_SHORT).show();
                    getParentFragmentManager().popBackStack();
                }
            }.execute(existingConstat);
        } else {
            Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
        }
    }
}
