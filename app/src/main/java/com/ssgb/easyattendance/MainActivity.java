package com.ssgb.easyattendance;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ssgb.easyattendance.Adapter.ClassAdapter;
import com.ssgb.easyattendance.Adapter.ClassListAdapter;
import com.ssgb.easyattendance.database.AppDatabase;
import com.ssgb.easyattendance.database.entities.ClassNames;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ClassListAdapter adapter;
    private Button addClass;
    private AppDatabase db;
    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Classes");

        recyclerView = findViewById(R.id.recyclerView);
        addClass = findViewById(R.id.add_class);

        db = AppDatabase.getInstance(this);
        executorService = Executors.newSingleThreadExecutor();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ClassAdapter(this, null);
        recyclerView.setAdapter(adapter);

        loadClasses();

        addClass.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Insert_class_Activity.class);
            startActivity(intent);
        });
    }

    private void loadClasses() {
        executorService.execute(() -> {
            LiveData<List<ClassNames>> classesLiveData = db.classNamesDao().getAllClasses();
            classesLiveData.observe(this, classes -> {
                if (classes != null) {
                    adapter.updateList(classes);
                }
            });
        });
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
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}