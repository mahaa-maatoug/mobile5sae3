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
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.constat.Database.ContratDatabase;
import com.example.constat.entity.Contrat;

public class UpdateContratFragment extends Fragment {

    private EditText etPrice;
    private EditText etType;
    private EditText etDescription;
    private EditText etDate;
    private Button btnUpdateContrat;

    private ContratDatabase contratDatabase;
    private int contratId;

    private Contrat existingConstat;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.addcontrat, container, false);

        contratDatabase = ContratDatabase.getDatabase(getContext());

        etPrice = view.findViewById(R.id.etPrice);
        etType = view.findViewById(R.id.etType);
        etDescription = view.findViewById(R.id.etDescription);
        etDate = view.findViewById(R.id.etDate);
        btnUpdateContrat = view.findViewById(R.id.btnSaveContrat);

        ImageButton backButton = view.findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        // Récupérer l'ID du contrat depuis les arguments
        Bundle args = getArguments();
        if (getArguments() != null) {
            existingConstat = (Contrat) args.getSerializable("constat"); // Retrieve the existing Constat
            populateFields(existingConstat);
            btnUpdateContrat.setText("Update Constat");
        }

        btnUpdateContrat.setOnClickListener(v -> updateContrat());

        return view;
    }

    // Populating the fields with existing contract data
    private void populateFields(Contrat constat) {
        if (constat != null) {
            etDate.setText(constat.getStartDate());
            etDescription.setText(constat.getDescription());
            etPrice.setText(String.valueOf(constat.getPrice()));
            etType.setText(constat.getContractType());
        }
    }

    private void deleteConstat() {
        if (contratId != 0) {
            new Thread(() -> {
                ContratDatabase db = ContratDatabase.getDatabase(getActivity());
                db.contratDao().deleteConstatById(contratId);
                Log.d("DeleteConstat", "Deleted Constat with ID: " + contratId);
                getActivity().runOnUiThread(() -> getActivity().getSupportFragmentManager().popBackStack());
            }).start();
        } else {
            Log.d("DeleteConstat", "Invalid ID for deletion.");
        }
    }

    // Method to update the contract
    private void updateContrat() {
        String priceText = etPrice.getText().toString();
        String type = etType.getText().toString();
        String description = etDescription.getText().toString();
        String startDate = etDate.getText().toString();

        // Vérifiez si au moins un champ a été modifié
        if (priceText.isEmpty() && type.isEmpty() && description.isEmpty() && startDate.isEmpty()) {
            Toast.makeText(getContext(), "Veuillez remplir au moins un champ", Toast.LENGTH_SHORT).show();
            return;
        }

        // Vérification du format du prix
        final double price;
        if (!priceText.isEmpty()) {
            try {
                price = Double.parseDouble(priceText);
                if (price < 0) {
                    Toast.makeText(getContext(), "Le prix ne peut pas être négatif", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Format de prix invalide", Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            price = -1; // If the price is empty, set it to -1
        }

        // Vérifier que l'ID du contrat est valide
        if (contratId == -1) {
            Toast.makeText(getContext(), "Contrat invalide", Toast.LENGTH_SHORT).show();
            return;
        }

        // Mettre à jour le contrat avec les nouveaux champs remplis
        existingConstat.setPrice(price != -1 ? price : existingConstat.getPrice());
        existingConstat.setContractType(!type.isEmpty() ? type : existingConstat.getContractType());
        existingConstat.setDescription(!description.isEmpty() ? description : existingConstat.getDescription());
        existingConstat.setStartDate(!startDate.isEmpty() ? startDate : existingConstat.getStartDate());

        // Exécuter la mise à jour dans un AsyncTask
        new AsyncTask<Contrat, Void, Void>() {
            @Override
            protected Void doInBackground(Contrat... contrats) {
                try {
                    contratDatabase.contratDao().updateContrat(contrats[0]);
                } catch (Exception e) {
                    Log.e("UpdateContratFragment", "Error updating contract: ", e);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                Toast.makeText(getContext(), "Contrat mis à jour avec succès", Toast.LENGTH_SHORT).show();
                getParentFragmentManager().popBackStack(); // Retourner au fragment précédent
            }
        }.execute(existingConstat);
    }
}
