package com.example.constat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {
  private SharedPreferences mPreferences;
  public static final String sharedPrefFile = "com.example.assu2";
  private TextView welcomeMessage;
  private Button logoutButton;
  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);

    mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
    // Afficher le message de bienvenue
    welcomeMessage = findViewById(R.id.welcome_message);
    String savedUsername = mPreferences.getString("username", "User");
    welcomeMessage.setText("Salut " + savedUsername + "!");

    // Récupérer le bouton de retour
    ImageButton backButton = findViewById(R.id.back_button);
    logoutButton = findViewById(R.id.logout_button); // Récupérer le bouton de déconnexion

    // Gérer le clic sur le bouton "Back" pour afficher le bouton de déconnexion
    backButton.setOnClickListener(v -> {
      if (logoutButton.getVisibility() == View.GONE) {
        // Rendre le bouton de déconnexion visible s'il est caché
        logoutButton.setVisibility(View.VISIBLE);
      } else {
        // Cacher le bouton de déconnexion s'il est visible
        logoutButton.setVisibility(View.GONE);
      }
    });

    // Gérer le clic sur le bouton "Logout" pour se déconnecter
    logoutButton.setOnClickListener(v -> {
      // Mettre à jour l'état de connexion
      SharedPreferences.Editor preferencesEditor = mPreferences.edit();
      preferencesEditor.putBoolean("isLoggedIn", false); // Déconnexion, mais ne supprime pas l'email et le mot de passe
      preferencesEditor.apply();

      // Retourner à l'activité de connexion
      Intent intent = new Intent(HomeActivity.this, MainActivity.class);
      startActivity(intent);
      finish(); // Fermer l'activité actuelle
    });

    // Set up the BottomNavigationView
    BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

    // Handle the bottom navigation item selection
    bottomNavigationView.setOnItemSelectedListener(item -> {
      int id = item.getItemId();

      if (id == R.id.nav_actualite) {
        loadFragment(new ActualiteFragment());
        return true;
      } else if (id == R.id.nav_assurance_type) {
        loadFragment(new ConstatFragment());
        return true;
      }else if (id == R.id.nav_offres) {
        loadFragment(new OffreFragment());
        return true;
      }else if (id == R.id.nav_declaration_sinistre) {
        loadFragment(new SinistreFragment());
        return true;
      }else if (id == R.id.nav_contrat) {
        loadFragment(new ContratFragment());
        return true;
      }
      // Additional else-if cases for other menu items if needed

      return false;
    });


    // Optionally, load a default fragment on startup, if desired
    if (savedInstanceState == null) {
      loadFragment(new ActualiteFragment()); // Load ActualiteFragment as default
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
