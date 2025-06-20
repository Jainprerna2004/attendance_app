package com.ssgb.easyattendance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText etEmail, etPassword;
    private MaterialButton btnLogin;
    private TextView tvSignUpLink, tvForgotPassword;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private boolean permissionRequested = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase directly
        try {
            FirebaseApp.initializeApp(this);
            mAuth = FirebaseAuth.getInstance();
        } catch (Exception e) {
            Toast.makeText(this, "Firebase initialization failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // Check if user is already logged in
        if (mAuth.getCurrentUser() != null) {
            // User is already logged in, go to main activity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return;
        }

        // Initialize views
        initializeViews();
        setupToolbar();
        setupClickListeners();
        
        // Request permission after everything is set up
        requestStoragePermission();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Removed permission request from here to avoid multiple requests
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Storage permission granted!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Storage permission denied. PDF export may not work.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void initializeViews() {
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvSignUpLink = findViewById(R.id.tvSignUpLink);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Signing in...");
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
        }
    }

    private void setupClickListeners() {
        btnLogin.setOnClickListener(v -> {
            if (validateInputs()) {
                loginUser();
            }
        });

        tvSignUpLink.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            finish();
        });

        tvForgotPassword.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            if (email.isEmpty()) {
                etEmail.setError("Please enter your email");
                etEmail.requestFocus();
                return;
            }
            resetPassword(email);
        });
    }

    private boolean validateInputs() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty()) {
            etEmail.setError("Email is required");
            etEmail.requestFocus();
            return false;
        }

        if (password.isEmpty()) {
            etPassword.setError("Password is required");
            etPassword.requestFocus();
            return false;
        }

        return true;
    }

    private void loginUser() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        progressDialog.show();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    progressDialog.dismiss();
                    if (task.isSuccessful()) {
                        // Sign in success
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        // If sign in fails, display a message to the user
                        Toast.makeText(LoginActivity.this, "Authentication failed: " + task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void resetPassword(String email) {
        progressDialog.setMessage("Sending reset email...");
        progressDialog.show();

        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    progressDialog.dismiss();
                    if (task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "Password reset email sent. Please check your email.",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(LoginActivity.this, "Failed to send reset email: " + task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void requestStoragePermission() {
        // For Android 11+ (API 30+), WRITE_EXTERNAL_STORAGE is automatically granted for app-specific storage
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Toast.makeText(this, "Android 11+: Storage permission automatically granted for app files", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // For older Android versions, check if permission is already granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) 
                == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Storage permission already granted", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Check if we should show rationale
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            // Show rationale dialog
            new android.app.AlertDialog.Builder(this)
                .setTitle("Permission Needed")
                .setMessage("This app needs storage permission to save PDF reports to your Downloads folder.")
                .setPositiveButton("OK", (dialog, which) -> {
                    ActivityCompat.requestPermissions(this, 
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
                })
                .setNegativeButton("Cancel", null)
                .show();
        } else {
            // Request permission directly
            Toast.makeText(this, "Requesting storage permission...", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this, 
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
} 