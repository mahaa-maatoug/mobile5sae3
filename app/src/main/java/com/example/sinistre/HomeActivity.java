package com.example.sinistre;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);

    // Set up the BottomNavigationView
    BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

    // Handle the bottom navigation item selection
    bottomNavigationView.setOnItemSelectedListener(item -> {
      int id = item.getItemId();

      // If "Offres" is selected, load the OffreFragment
      if (id == R.id.nav_declaration_sinistre) {
        loadFragment(new SinistreFragment()); // Load OffreFragment when "Offres" is clicked
        return true;
      }

      // Add more navigation logic here if needed
      return false;
    });

    // Optionally, you can load a default fragment like the "Actualité" fragment, if needed
    if (savedInstanceState == null) {
      // Do not load OffreFragment here; just leave it or load another default fragment if needed
      // loadFragment(new SomeOtherFragment()); // For example, load another fragment
    }
  }

  // Helper method to load a fragment into the fragment container
  private void loadFragment(Fragment fragment) {
    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    transaction.replace(R.id.fragment_container, fragment);  // Use the correct container ID
    transaction.addToBackStack(null);  // Add the transaction to the back stack for back navigation
    transaction.commit();
  }
}

