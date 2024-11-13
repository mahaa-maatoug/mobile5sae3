package com.example.constat;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import com.example.constat.Database.OffreDatabase;
import com.example.constat.entity.Offre;


public class UpdateOffreFragment extends Fragment {

  private EditText etPrice;
  private EditText etType;
  private EditText etDescription;
  private TextView etDate;
  private Button btnUpdateOffre;
  private OffreDatabase offreDatabase; // Room database
  private Offre existingOffre; // Declare existingOffre here

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
    btnUpdateOffre = view.findViewById(R.id.btnSaveOffer);

    // Handle back button in the toolbar
    ImageButton backButton = view.findViewById(R.id.back_button);
    backButton.setOnClickListener(v -> getParentFragmentManager().popBackStack());

    // Get the existing Offre from the arguments (if any)
    Bundle args = getArguments();
    if (args != null) {
      existingOffre = (Offre) args.getSerializable("offre"); // Retrieve the existing Offre
      populateFields(existingOffre);
      btnUpdateOffre.setText("Update Offre");
    }

    // Handle update button click
    btnUpdateOffre.setOnClickListener(v -> updateOffre());

    return view;
  }

  private void populateFields(Offre offre) {
    if (offre != null) {
      etPrice.setText(String.valueOf(offre.getPrice()));
      etType.setText(offre.getType());
      etDescription.setText(offre.getDescription());
      etDate.setText(offre.getDate());
    }
  }

  private void updateOffre() {
    String priceText = etPrice.getText().toString();
    String type = etType.getText().toString();
    String description = etDescription.getText().toString();
    String date = etDate.getText().toString();

    if (!priceText.isEmpty() && !type.isEmpty() && !description.isEmpty() && !date.isEmpty()) {
      double price = Double.parseDouble(priceText); // Convert price to double

      existingOffre.setPrice(price);
      existingOffre.setType(type);
      existingOffre.setDescription(description);
      existingOffre.setDate(date);

      new AsyncTask<Offre, Void, Void>() {
        @Override
        protected Void doInBackground(Offre... offres) {
          try {
            offreDatabase.offreDao().updateOffre(offres[0]);
          } catch (Exception e) {
            Log.e("UpdateOffreFragment", "Error updating offre: ", e);
          }
          return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
          Toast.makeText(getContext(), "Offre updated successfully", Toast.LENGTH_SHORT).show();
          getParentFragmentManager().popBackStack();
        }
      }.execute(existingOffre);
    } else {
      Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
    }
  }
}
