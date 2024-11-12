package com.example.mahaassure;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.fragment.app.Fragment;

import com.example.mahaassure.Database.OffreDatabase;
import com.example.mahaassure.entity.Offre;

public class AddOfferFragment extends Fragment {

  private EditText etPrice;
  private EditText etType;
  private EditText etDescription;
  private EditText etDate;
  private Button btnSaveOffer;

  private OffreDatabase offreDatabase;  // Room database

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.addoffre, container, false);

    // Initialize Room database
    offreDatabase = OffreDatabase.getDatabase(getContext());

    // Initialize UI components
    etPrice = view.findViewById(R.id.etPrice);
    etType = view.findViewById(R.id.etType);
    etDescription = view.findViewById(R.id.etDescription);
    etDate = view.findViewById(R.id.etDate);
    btnSaveOffer = view.findViewById(R.id.btnSaveOffer);

    // Handle back button in the toolbar
    ImageButton backButton = view.findViewById(R.id.back_button);
    backButton.setOnClickListener(v -> getParentFragmentManager().popBackStack());

    // Handle save button click
    btnSaveOffer.setOnClickListener(v -> saveOffer());

    return view;
  }

  // Method to save the offer
  private void saveOffer() {
    String priceText = etPrice.getText().toString();
    double price = Double.parseDouble(priceText);  // Convert to double
    String type = etType.getText().toString();
    String description = etDescription.getText().toString();
    String date = etDate.getText().toString();

    if (!priceText.isEmpty() && !type.isEmpty() && !description.isEmpty() && !date.isEmpty()) {
      // Create a new offer object
      Offre newOffer = new Offre(price, type, description, date);

      // Insert the offer into the Room database
      new AsyncTask<Offre, Void, Void>() {
        @Override
        protected Void doInBackground(Offre... offres) {
          offreDatabase.offreDao().insertOffre(offres[0]);
          return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
          // Notify the user
          Toast.makeText(getContext(), "Offer added successfully", Toast.LENGTH_SHORT).show();

          // Navigate back to the previous fragment
          getParentFragmentManager().popBackStack();
        }
      }.execute(newOffer);
    } else {
      Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
    }
  }
}
