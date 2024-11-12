package com.example.sinistre;

import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    EdgeToEdge.enable(this); // Assuming this is a utility for edge-to-edge layout
    setContentView(R.layout.activity_main);

    // Handle insets for system bars
    ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
      Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
      v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
      return insets;
    });

    // Load the default fragment (SinistreFragment) on launch
    if (savedInstanceState == null) {
      loadFragment(new SinistreFragment());
    }

    // Button for loading the SinistreFragment
    Button btnSinistre = findViewById(R.id.btn_sinistre);
    btnSinistre.setOnClickListener(v -> {
      loadFragment(new SinistreFragment());
    });

    // Button for loading the DescriptionFragment with some data
    Button btnDescription = findViewById(R.id.btn_description);
    btnDescription.setOnClickListener(v -> {
      DescriptionFragment descriptionFragment = new DescriptionFragment();

      // Pass some data to the description fragment using a bundle
      Bundle bundle = new Bundle();
      bundle.putString("dateSinistre", "15/10/2024");
      bundle.putString("nomSinistre", "Paris");
      bundle.putString("descriptionSinistre", "Collision légère sans blessure");
      descriptionFragment.setArguments(bundle);

      // Load the description fragment
      loadFragment(descriptionFragment);
    });
  }

  // Method to replace the fragment container with the specified fragment
  private void loadFragment(Fragment fragment) {
    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    transaction.replace(R.id.fragment_container, fragment); // Replace with the new fragment
    transaction.addToBackStack(null); // Add to back stack to allow "back" navigation
    transaction.commit();
  }
}

