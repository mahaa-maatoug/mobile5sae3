package com.example.constat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText emailEditText;
    private SharedPreferences mPreferences;
    public static final String sharedPrefFile = "com.example.assu2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgotpassword);

        emailEditText = findViewById(R.id.email);
        Button resetPasswordButton = findViewById(R.id.resetPasswordButton);
        TextView backToLoginTextView = findViewById(R.id.backToLogin);

        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);

        resetPasswordButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(ForgotPasswordActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();
            } else {
                String savedEmail = mPreferences.getString("email", "");
                String savedPassword = mPreferences.getString("password", "");

                Log.d("ForgotPasswordActivity", "Saved email: " + savedEmail);
                Log.d("ForgotPasswordActivity", "Entered email: " + email);

                if (email.equals(savedEmail)) {
                    // Envoyer un email contenant le mot de passe
                    sendRecoveryEmail(email, savedPassword);
                } else {
                    Toast.makeText(ForgotPasswordActivity.this, "Email not found", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Gestionnaire d'événements pour revenir à la page de connexion
        backToLoginTextView.setOnClickListener(v -> {
            Intent intent = new Intent(ForgotPasswordActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // Facultatif : ferme l'activité actuelle
        });
    }

    private void sendRecoveryEmail(String email, String password) {
        // Configuration des paramètres de votre compte email
        String fromEmail = "Zaafranimaram07@gmail.com"; // Remplacez par votre adresse Gmail
        String fromPassword = "oefy cxwl dayv oxzb"; // Remplacez par le mot de passe d'application généré
        String subject = "Password Recovery";
        String message = "Your password is: " + password;

        // Utiliser AsyncTask pour envoyer l'email en arrière-plan
        new SendEmailTask(fromEmail, fromPassword, email, subject, message).execute();
    }

    // AsyncTask pour envoyer l'email en arrière-plan
    private class SendEmailTask extends AsyncTask<Void, Void, Boolean> {

        private String fromEmail, fromPassword, toEmail, emailSubject, emailMessage;

        public SendEmailTask(String fromEmail, String fromPassword, String toEmail, String emailSubject, String emailMessage) {
            this.fromEmail = fromEmail;
            this.fromPassword = fromPassword;
            this.toEmail = toEmail;
            this.emailSubject = emailSubject;
            this.emailMessage = emailMessage;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                // Propriétés pour la configuration SMTP
                Properties props = new Properties();
                props.put("mail.smtp.host", "smtp.gmail.com");
                props.put("mail.smtp.port", "587");
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", "true");

                // Créer une session avec authentification
                Session session = Session.getInstance(props, new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(fromEmail, fromPassword);
                    }
                });

                // Créer le message
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(fromEmail));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
                message.setSubject(emailSubject);
                message.setText(emailMessage);

                // Envoyer l'email
                Transport.send(message);
                return true;

            } catch (MessagingException e) {
                Log.e("SendEmailTask", "Error while sending email: ", e);
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                Toast.makeText(ForgotPasswordActivity.this, "Password recovery email sent successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ForgotPasswordActivity.this, "Failed to send password recovery email", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
