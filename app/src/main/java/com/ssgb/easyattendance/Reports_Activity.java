package com.ssgb.easyattendance;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ssgb.easyattendance.Adapter.ReportsAdapter;
import com.ssgb.easyattendance.realm.AppDatabase;
import com.ssgb.easyattendance.realm.Attendance_Reports;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Reports_Activity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ReportsAdapter adapter;
    private AppDatabase db;
    private ExecutorService executorService;
    private String room_ID;
    private String classname;
    private String subjName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        Toolbar toolbar = findViewById(R.id.toolbar_reports);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        room_ID = getIntent().getStringExtra("room_ID");
        classname = getIntent().getStringExtra("class_name");
        subjName = getIntent().getStringExtra("subject_name");

        recyclerView = findViewById(R.id.recyclerView_reports);

        db = AppDatabase.getInstance(this);
        executorService = Executors.newSingleThreadExecutor();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ReportsAdapter(this, null);
        recyclerView.setAdapter(adapter);

        loadReports();
    }

    private void loadReports() {
        executorService.execute(() -> {
            List<Attendance_Reports> reports = db.attendanceReportsDao().getByClassId(room_ID);
            runOnUiThread(() -> {
                if (reports != null) {
                    adapter.updateList(reports);
                }
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