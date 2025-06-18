package com.ssgb.easyattendance;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ssgb.easyattendance.Adapter.ReportsAdapter;
import com.ssgb.easyattendance.database.AppDatabase;
import com.ssgb.easyattendance.database.entities.AttendanceReports;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Reports_Activity extends AppCompatActivity {
    private TextView className, subjectName;
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

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        room_ID = getIntent().getStringExtra("room_ID");
        classname = getIntent().getStringExtra("classname");
        subjName = getIntent().getStringExtra("subjName");

        className = findViewById(R.id.class_name);
        subjectName = findViewById(R.id.subject_name);
        recyclerView = findViewById(R.id.recyclerView);

        className.setText(classname);
        subjectName.setText(subjName);

        db = AppDatabase.getInstance(this);
        executorService = Executors.newSingleThreadExecutor();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ReportsAdapter(this, null);
        recyclerView.setAdapter(adapter);

        loadReports();
    }

    private void loadReports() {
        executorService.execute(() -> {
            LiveData<List<AttendanceReports>> reportsLiveData = db.attendanceReportsDao().getReportsByClassId(room_ID);
            reportsLiveData.observe(this, reports -> {
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