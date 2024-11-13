package com.example.constat;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.constat.Database.ContratDatabase;
import com.example.constat.entity.Contrat;

public class DescriptionContratFragment extends Fragment {

    private Contrat currentContrat;  // Corrected typo in variable name
    private int contratId;           // To hold the ID of the contrat

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Charger le layout du fragment
        View view = inflater.inflate(R.layout.description_contrat, container, false);

        // Récupérer les éléments TextView pour afficher les détails du contrat
        TextView priceTextView = view.findViewById(R.id.tvPrice);
        TextView typeTextView = view.findViewById(R.id.tvContractType);
        TextView dateTextView = view.findViewById(R.id.tvStartDate);
        TextView descriptionTextView = view.findViewById(R.id.tvDescription);
        Button btnEditContract = view.findViewById(R.id.btnEditContract);  // Bouton Modifier

        Button deleteButton = view.findViewById(R.id.btnDeleteConstat); // Reference to the delete button


        // Récupérer les données passées via les arguments du fragment
        Bundle args = getArguments();
        if (args != null) {
            contratId = args.getInt("contratId", -1);  // Store the contract ID

            ContratDatabase db = ContratDatabase.getDatabase(getActivity());
            db.contratDao().getContratById(contratId).observe(getViewLifecycleOwner(), constat -> {
                if (constat != null) {
                    currentContrat = constat;  // Store the current contract for updating

                    // Mise à jour des TextViews avec les valeurs récupérées
                    priceTextView.setText(getString(R.string.price_label, constat.getPrice()));
                    typeTextView.setText(getString(R.string.contract_type_label, constat.getContractType()));
                    dateTextView.setText(getString(R.string.start_date_label, constat.getStartDate()));
                    descriptionTextView.setText(getString(R.string.description_label, constat.getDescription()));
                }
            });
        }

        deleteButton.setOnClickListener(v -> deleteConstat());


        // Gérer le clic sur le bouton Modifier
        btnEditContract.setOnClickListener(v -> updateConstat());

        return view;
    }


    private void updateConstat() {
        if (currentContrat != null) {
            // Create a new Bundle and put the currentConstat in it
            Bundle bundle = new Bundle();
            bundle.putSerializable("constat", currentContrat); // Pass the currentConstat

            // Create an instance of UpdateConstatFragment and set the arguments
            UpdateContratFragment updateConstatFragment = new UpdateContratFragment();
            updateConstatFragment.setArguments(bundle);

            // Replace the current fragment with UpdateConstatFragment
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, updateConstatFragment) // Use your container ID
                    .addToBackStack(null) // Optional: add to back stack
                    .commit();
        } else {
            Log.d("UpdateConstat", "No current constat to update.");
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


   /* private void updateConstat() {
        if (currentContrat != null) {
            // Create a new fragment to update the contract
            UpdateContratFragment updateContratFragment = new UpdateContratFragment();

            // Pass the current contract data to the update fragment
            Bundle updateArgs = new Bundle();
            updateArgs.putSerializable("contrat", currentContrat); // Pass the current contract as Serializable

            updateContratFragment.setArguments(updateArgs);

            // Replace the current fragment with the update fragment
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, updateContratFragment);  // Replace with the update fragment
            transaction.addToBackStack(null);  // Add to back stack for navigation
            transaction.commit();  // Commit the transaction
        } else {
            Log.d("UpdateContrat", "No current contract to update.");
        }
    }*/
}
