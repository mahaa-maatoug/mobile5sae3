package com.example.contrat;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.contrat.Database.ContratDatabase;
import com.example.contrat.entity.Contrat;

import java.util.List;

public class ContratFragment extends Fragment {
    private RecyclerView recyclerView;
    private ContratAdapter adapter;
    private List<Contrat> contratList;
    private ContratDatabase contratDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contrat, container, false);

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewContracts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize Room database
        contratDatabase = ContratDatabase.getDatabase(getContext());

        // Load contracts from the database
        loadContractsFromDatabase();

        // Handle the click for adding a new contract
        ImageButton btnAddContract = view.findViewById(R.id.add_button);
        btnAddContract.setOnClickListener(v -> loadAddContractFragment());

        return view;
    }

    // Load contracts from Room database
    private void loadContractsFromDatabase() {
        new AsyncTask<Void, Void, List<Contrat>>() {
            @Override
            protected List<Contrat> doInBackground(Void... voids) {
                return contratDatabase.contratDao().getAllContrats();
            }

            @Override
            protected void onPostExecute(List<Contrat> contrats) {
                contratList = contrats;
                adapter = new ContratAdapter(contratList, ContratFragment.this::showDescriptionFragment);
                recyclerView.setAdapter(adapter);
            }
        }.execute();
    }

    // Add a new contract to the Room database and the RecyclerView
    public void addNewContract(Contrat newContract) {
        new AsyncTask<Contrat, Void, Void>() {
            @Override
            protected Void doInBackground(Contrat... contrats) {
                contratDatabase.contratDao().insertContrat(contrats[0]);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                loadContractsFromDatabase();
            }
        }.execute(newContract);
    }

    // Function to load AddContractFragment
    private void loadAddContractFragment() {
        AddContratFragment addContractFragment = new AddContratFragment();
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, addContractFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    // Function to show the DescriptionFragment when a contract is clicked
    private void showDescriptionFragment(Contrat contrat) {
        DescriptionFragment descriptionFragment = new DescriptionFragment();
        Bundle bundle = new Bundle();
        bundle.putDouble("price", contrat.getPrice());
        bundle.putString("type", contrat.getContractType());
        bundle.putString("start_date", contrat.getStartDate());
        bundle.putString("end_date", contrat.getEndDate());
        bundle.putString("description", contrat.getDescription());
        descriptionFragment.setArguments(bundle);

        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, descriptionFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
