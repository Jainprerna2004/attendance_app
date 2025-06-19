package com.ssgb.easyattendance;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SignupActivity extends AppCompatActivity {
    private EditText nameInput, emailInput, passwordInput;
    private Button signupButton;

    private TextView loginText;
    private ProgressBar progressBar;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        auth = FirebaseAuth.getInstance();

        nameInput = findViewById(R.id.etName);
        emailInput = findViewById(R.id.etEmail);
        passwordInput = findViewById(R.id.etPassword);
        signupButton = findViewById(R.id.btnSignUp);
        loginText = findViewById(R.id.tvLoginLink);
        progressBar = findViewById(R.id.progressBar);

        signupButton.setOnClickListener(v -> {
            String name = nameInput.getText().toString();
            String email = emailInput.getText().toString();
            String password = passwordInput.getText().toString();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            signupUser(name, email, password);
        });

        loginText.setOnClickListener(v -> {
            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void signupUser(String name, String email, String password) {
        progressBar.setVisibility(View.VISIBLE);
        signupButton.setEnabled(false);

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, task -> {
                if (task.isSuccessful()) {
                    // Update user profile with name
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .build();

                    auth.getCurrentUser().updateProfile(profileUpdates)
                        .addOnCompleteListener(profileTask -> {
                            progressBar.setVisibility(View.GONE);
                            signupButton.setEnabled(true);

                            if (profileTask.isSuccessful()) {
                                Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(SignupActivity.this, "Failed to update profile: " + profileTask.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                            }
                        });
                } else {
                    progressBar.setVisibility(View.GONE);
                    signupButton.setEnabled(true);
                    Toast.makeText(SignupActivity.this, "Authentication failed: " + task.getException().getMessage(),
                        Toast.LENGTH_SHORT).show();
                }
            });
    }
} 