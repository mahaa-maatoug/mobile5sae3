package com.example.constat;

import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
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

import java.util.Calendar;

public class AddOfferFragment extends Fragment {

  private EditText etPrice;
  private EditText etType;
  private EditText etDescription;
  private TextView etDate;
  private Button btnSaveOffer;

  private OffreDatabase offreDatabase;  // Room database

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.addoffre, container, false);

    // Initialize Room database and UI components
    offreDatabase = OffreDatabase.getDatabase(getContext());
    etPrice = view.findViewById(R.id.etPrice);
    etType = view.findViewById(R.id.etType);
    etDescription = view.findViewById(R.id.etDescription);
    etDate = view.findViewById(R.id.etDate);
    btnSaveOffer = view.findViewById(R.id.btnSaveOffer);

    // Date picker for date field
    etDate.setOnClickListener(v -> showDatePickerDialog());

    // Handle back button in the toolbar
    ImageButton backButton = view.findViewById(R.id.back_button);
    backButton.setOnClickListener(v -> getParentFragmentManager().popBackStack());

    // Handle save button click
    btnSaveOffer.setOnClickListener(v -> saveOffer());

    return view;
  }

  private void showDatePickerDialog() {
    final Calendar calendar = Calendar.getInstance();
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH);
    int day = calendar.get(Calendar.DAY_OF_MONTH);

    DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
      (view, selectedYear, selectedMonth, selectedDay) -> {
        // Format and set the selected date in etDate
        String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
        etDate.setText(selectedDate);
      }, year, month, day);
    datePickerDialog.show();
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
