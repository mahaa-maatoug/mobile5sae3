package com.example.constat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences mPreferences;
    public static final String sharedPrefFile = "com.example.assu2";
    private EditText mEmail;
    private EditText mPassword;

    private AppDatabase database;
    private UsersAdapter usersAdapter;
    private List<User> userList = new ArrayList<>();
    private RecyclerView recyclerView;

    private static final int REQUEST_SEND_SMS = 123;
    private static final String PHONE_NUMBER = "23068247";  // Votre numéro de téléphone local

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);

        // Vérifier si l'utilisateur est déjà connecté
        boolean isLoggedIn = mPreferences.getBoolean("isLoggedIn", false);
        if (isLoggedIn) {
            // Rediriger vers HomeActivity
            goToHomeActivity();
            return;
        }

        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        recyclerView = findViewById(R.id.recycler_view);

        // Charger les informations d'email et de mot de passe si elles existent
        String savedEmail = mPreferences.getString("email", "");
        String savedPassword = mPreferences.getString("password", "");
        mEmail.setText(savedEmail);
        mPassword.setText(savedPassword);

        database = AppDatabase.getAppDatabase(this);

        // Récupérer la liste des utilisateurs
        userList = database.userDao().getAll();
        usersAdapter = new UsersAdapter(this, userList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(usersAdapter);

        Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(v -> {
            String inputEmail = mEmail.getText().toString().trim();
            String inputPassword = mPassword.getText().toString().trim();

            if (inputEmail.isEmpty() || inputPassword.isEmpty()) {
                Toast.makeText(MainActivity.this, "Veuillez entrer votre email et votre mot de passe.", Toast.LENGTH_SHORT).show();
            } else if (inputEmail.equals(savedEmail) && inputPassword.equals(savedPassword)) {
                // Demander à l'utilisateur s'il souhaite enregistrer le mot de passe
                showSavePasswordDialog(inputEmail, inputPassword);
            } else {
                Toast.makeText(MainActivity.this, "Email ou mot de passe incorrect", Toast.LENGTH_SHORT).show();
            }
        });

        TextView signUpTextView = findViewById(R.id.btn_signup);
        signUpTextView.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        // Lien vers la page de réinitialisation du mot de passe
        TextView forgotPasswordTextView = findViewById(R.id.forgotPassword);
        forgotPasswordTextView.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
        });
    }

    private void showSavePasswordDialog(String email, String password) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enregistrer le mot de passe");
        builder.setMessage("Voulez-vous enregistrer le mot de passe pour une connexion future facile ?");

        builder.setPositiveButton("Oui", (dialog, which) -> {
            // Enregistrer les informations de connexion dans SharedPreferences
            SharedPreferences.Editor editor = mPreferences.edit();
            editor.putString("email", email);
            editor.putString("password", password);
            editor.putBoolean("isLoggedIn", true);
            editor.apply();

            // Envoyer un SMS après une connexion réussie
            sendLoginSuccessSms(email);

            // Rediriger vers HomeActivity
            goToHomeActivity();
        });

        builder.setNegativeButton("Non", (dialog, which) -> {
            // Enregistrer uniquement l'état de connexion
            SharedPreferences.Editor editor = mPreferences.edit();
            editor.putBoolean("isLoggedIn", true);
            editor.apply();

            // Envoyer un SMS après une connexion réussie
            sendLoginSuccessSms(email);

            // Rediriger vers HomeActivity
            goToHomeActivity();
        });

        builder.show();
    }

    // Fonction pour envoyer un SMS après une connexion réussie
    private void sendLoginSuccessSms(String username) {
        String message = "Bonjour " + username + ", vous vous êtes connecté avec succès à votre compte.";

        try {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, REQUEST_SEND_SMS);
            } else {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(PHONE_NUMBER, null, message, null, null);
                Toast.makeText(this, "Message envoyé avec succès", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Échec de l'envoi du message", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    // Gestion de la demande de permission
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_SEND_SMS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                String savedUsername = mPreferences.getString("username", "");
                sendLoginSuccessSms(savedUsername);
            } else {
                Toast.makeText(this, "Permission d'envoyer des SMS refusée", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void goToHomeActivity() {
        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        intent.putExtra("username", mPreferences.getString("username", ""));
        startActivity(intent);
        finish();
    }
}
