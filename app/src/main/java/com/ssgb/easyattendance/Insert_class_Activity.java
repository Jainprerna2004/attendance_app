package com.ssgb.easyattendance;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.ssgb.easyattendance.database.AppDatabase;
import com.ssgb.easyattendance.database.entities.ClassNames;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Insert_class_Activity extends AppCompatActivity {
    private EditText className, subjectName;
    private Button createClass;
    private AppDatabase db;
    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_class);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Create Class");

        className = findViewById(R.id.class_name);
        subjectName = findViewById(R.id.subject_name);
        createClass = findViewById(R.id.create_class);

        db = AppDatabase.getInstance(this);
        executorService = Executors.newSingleThreadExecutor();

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

    private void createClass(String className, String subjectName) {
        executorService.execute(() -> {
            ClassNames classNames = new ClassNames(
                UUID.randomUUID().toString(),
                className,
                subjectName
            );
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