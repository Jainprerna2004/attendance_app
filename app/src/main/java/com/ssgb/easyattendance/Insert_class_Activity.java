package com.ssgb.easyattendance;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.ssgb.easyattendance.realm.AppDatabase;
import com.ssgb.easyattendance.realm.Class_Names;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Insert_class_Activity extends AppCompatActivity {
    private EditText className, subjectName;
    private Button createClass;
    private LinearLayout themeContainer;
    private AppDatabase db;
    private ExecutorService executorService;
    private String selectedTheme = "0"; // Default theme
    private ImageButton[] themeButtons;
    private int selectedThemeIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_class_);

        Toolbar toolbar = findViewById(R.id.toolbar_insert_class);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Create Class");

        className = findViewById(R.id.className_createClass);
        subjectName = findViewById(R.id.subjectName_createClass);
        createClass = findViewById(R.id.button_createClass);
        themeContainer = findViewById(R.id.themeContainer);

        db = AppDatabase.getInstance(this);
        executorService = Executors.newSingleThreadExecutor();

        // Initialize theme buttons array
        themeButtons = new ImageButton[6];
        themeButtons[0] = findViewById(R.id.theme1);
        themeButtons[1] = findViewById(R.id.theme2);
        themeButtons[2] = findViewById(R.id.theme3);
        themeButtons[3] = findViewById(R.id.theme4);
        themeButtons[4] = findViewById(R.id.theme5);
        themeButtons[5] = findViewById(R.id.theme6);

        // Set up theme selection
        setupThemeSelection();

        createClass.setOnClickListener(v -> {
            String class_name = className.getText().toString();
            String subj_name = subjectName.getText().toString();

            if (class_name.isEmpty() || subj_name.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            createClass(class_name, subj_name);
        });
    }

    private void setupThemeSelection() {
        // Set default selection
        if (themeButtons[0] != null) {
            themeButtons[0].setSelected(true);
            animateThemeSelection(0);
        }

        // Set click listeners for all theme buttons
        for (int i = 0; i < themeButtons.length; i++) {
            final int index = i;
            if (themeButtons[i] != null) {
                themeButtons[i].setOnClickListener(v -> {
                    selectedTheme = String.valueOf(index);
                    selectedThemeIndex = index;
                    animateThemeSelection(index);
                });
            }
        }
    }

    private void animateThemeSelection(int selectedIndex) {
        // Reset all buttons to normal size and unselected state
        for (int i = 0; i < themeButtons.length; i++) {
            if (themeButtons[i] != null) {
                themeButtons[i].setSelected(false);
                themeButtons[i].setScaleX(1.0f);
                themeButtons[i].setScaleY(1.0f);
                themeButtons[i].setAlpha(0.7f);
            }
        }

        // Animate the selected button
        if (themeButtons[selectedIndex] != null) {
            themeButtons[selectedIndex].setSelected(true);
            
            // Scale up animation
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(themeButtons[selectedIndex], "scaleX", 1.0f, 1.2f);
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(themeButtons[selectedIndex], "scaleY", 1.0f, 1.2f);
            ObjectAnimator alpha = ObjectAnimator.ofFloat(themeButtons[selectedIndex], "alpha", 0.7f, 1.0f);

            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(scaleX, scaleY, alpha);
            animatorSet.setDuration(200);
            animatorSet.start();

            // Show toast with theme name
            String[] themeNames = {"Pale Blue", "Green", "Yellow", "Pale Green", "Pale Orange", "White"};
            Toast.makeText(this, "Selected: " + themeNames[selectedIndex] + " Theme", Toast.LENGTH_SHORT).show();
        }
    }

    private void createClass(String className, String subjectName) {
        executorService.execute(() -> {
            Class_Names classNames = new Class_Names();
            classNames.setId(UUID.randomUUID().toString());
            classNames.setName_class(className);
            classNames.setName_subject(subjectName);
            classNames.setPosition_bg(selectedTheme);
            db.classNamesDao().insert(classNames);
            runOnUiThread(() -> {
                Toast.makeText(this, "Class created successfully", Toast.LENGTH_SHORT).show();
                finish();
            });
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}