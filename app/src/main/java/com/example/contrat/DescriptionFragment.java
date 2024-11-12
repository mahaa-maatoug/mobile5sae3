package com.example.contrat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class DescriptionFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Charger le layout du fragment
        View view = inflater.inflate(R.layout.description_contrat, container, false);

        // Récupérer les éléments TextView pour afficher les détails du contrat
        TextView priceTextView = view.findViewById(R.id.tvPrice);
        TextView typeTextView = view.findViewById(R.id.tvContractType);  // Changer l'ID ici
        TextView dateTextView = view.findViewById(R.id.tvStartDate);      // Changer l'ID ici
        TextView descriptionTextView = view.findViewById(R.id.tvDescription);

        // Récupérer les données passées via les arguments du fragment
        Bundle args = getArguments();
        if (args != null) {
            priceTextView.setText(String.valueOf(args.getDouble("price")));
            typeTextView.setText(args.getString("contractType"));  // Changer la clé ici
            dateTextView.setText(args.getString("startDate"));      // Changer la clé ici
            descriptionTextView.setText(args.getString("description")); // Afficher la description récupérée
        }

        return view;
    }
}
