package com.ssgb.easyattendance;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.ssgb.easyattendance.Adapter.ClassListAdapter;
import com.ssgb.easyattendance.realm.AppDatabase;
import com.ssgb.easyattendance.realm.Class_Names;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.Manifest;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ClassListAdapter adapter;
    private FloatingActionButton addClass;
    private ImageButton logoutButton;
    private AppDatabase db;
    private ExecutorService executorService;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar_beginner);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Classes");

        recyclerView = findViewById(R.id.recyclerView_main);
        addClass = findViewById(R.id.fab_main);
        logoutButton = findViewById(R.id.buttonLogout);

        db = AppDatabase.getInstance(this);
        executorService = Executors.newSingleThreadExecutor();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ClassListAdapter(new ArrayList<>(), this);
        recyclerView.setAdapter(adapter);

        loadClasses();

        addClass.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Insert_class_Activity.class);
            startActivity(intent);
        });

        // Setup logout button
        logoutButton.setOnClickListener(v -> {
            logoutUser();
        });

        // Setup bottom app bar menu
        com.google.android.material.bottomappbar.BottomAppBar bottomAppBar = findViewById(R.id.bottomAppBar);
        bottomAppBar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.search) {
                showSearchDialog();
                return true;
            }
            return false;
        });
    }

    private void loadClasses() {
        executorService.execute(() -> {
            List<Class_Names> classes = db.classNamesDao().getAll();
            runOnUiThread(() -> {
                if (classes != null) {
                    if (adapter == null) {
                        adapter = new ClassListAdapter(classes, this);
                        recyclerView.setAdapter(adapter);
                    } else {
                        adapter.updateList(classes);
                    }
                }
            });
        });
    }

    public void refreshClassesList() {
        loadClasses();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadClasses();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else if (item.getItemId() == R.id.search) {
            // Handle bottom app bar search button
            showSearchDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }

    private void logoutUser() {
        mAuth.signOut();
        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
        
        // Navigate back to login screen
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // No menu inflation for toolbar - search is handled in bottom app bar
        return true;
    }

    private void showSearchDialog() {
        // Create a simple search dialog
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Search Classes");

        // Set up the input
        final android.widget.EditText input = new android.widget.EditText(this);
        input.setHint("Enter class name or subject");
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("Search", (dialog, which) -> {
            String searchText = input.getText().toString().trim();
            if (adapter != null) {
                if (!searchText.isEmpty()) {
                    adapter.getFilter().filter(searchText);
                    Toast.makeText(this, "Searching for: " + searchText, Toast.LENGTH_SHORT).show();
                } else {
                    // If search text is empty, show all classes
                    adapter.getFilter().filter("");
                    Toast.makeText(this, "Showing all classes", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Adapter not ready", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.setNeutralButton("Clear", (dialog, which) -> {
            if (adapter != null) {
                adapter.getFilter().filter("");
                Toast.makeText(this, "Showing all classes", Toast.LENGTH_SHORT).show();
            }
        });

        android.app.AlertDialog dialog = builder.show();
        
        // Set button text colors to make them visible and colorful
        dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setTextColor(android.graphics.Color.parseColor("#FF6B6B"));
        dialog.getButton(android.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(android.graphics.Color.parseColor("#333333"));
        dialog.getButton(android.app.AlertDialog.BUTTON_NEUTRAL).setTextColor(android.graphics.Color.parseColor("#2196F3"));
    }
}