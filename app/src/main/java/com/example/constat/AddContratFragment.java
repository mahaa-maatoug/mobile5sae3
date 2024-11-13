package com.example.constat;

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

import com.example.constat.Database.ContratDatabase;
import com.example.constat.entity.Contrat;

public class AddContratFragment extends Fragment {

    private EditText etPrice;
    private EditText etType;
    private EditText etDescription;
    private EditText etDate;
    private Button btnSaveContrat;

    private ContratDatabase contratDatabase;  // Room database

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.addcontrat, container, false);

        // Initialize Room database
        contratDatabase = ContratDatabase.getDatabase(getContext());

        // Initialize UI components
        etPrice = view.findViewById(R.id.etPrice);
        etType = view.findViewById(R.id.etType);
        etDescription = view.findViewById(R.id.etDescription);
        etDate = view.findViewById(R.id.etDate);
        btnSaveContrat = view.findViewById(R.id.btnSaveContrat);

        // Handle back button in the toolbar
        ImageButton backButton = view.findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        // Handle save button click
        btnSaveContrat.setOnClickListener(v -> saveContrat());

        return view;
    }

    // Method to save the contract
    private void saveContrat() {
        String priceText = etPrice.getText().toString();
        String type = etType.getText().toString();
        String description = etDescription.getText().toString();
        String startDate = etDate.getText().toString(); // Assuming startDate is from etDate
        String endDate = "Some End Date"; // You should retrieve this value from another EditText if needed

        if (!priceText.isEmpty() && !type.isEmpty() && !description.isEmpty() && !startDate.isEmpty()) {
            double price;

            try {
                price = Double.parseDouble(priceText);  // Convertir en double
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Format de prix invalide", Toast.LENGTH_SHORT).show();
                return; // Quitter la méthode si le format est invalide
            }

            // Créer un nouvel objet contrat avec les paramètres requis
            Contrat newContrat = new Contrat("Titre du contrat", type, startDate, endDate, price, description);

            // Insérer le contrat dans la base de données Room
            new AsyncTask<Contrat, Void, Void>() {
                @Override
                protected Void doInBackground(Contrat... contrats) {
                    contratDatabase.contratDao().insertContrat(contrats[0]);
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    // Avertir l'utilisateur
                    Toast.makeText(getContext(), "Contrat ajouté avec succès", Toast.LENGTH_SHORT).show();

                    // Revenir au fragment précédent
                    getParentFragmentManager().popBackStack();
                }
            }.execute(newContrat);
        } else {
            Toast.makeText(getContext(), "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
        }
    }

}
