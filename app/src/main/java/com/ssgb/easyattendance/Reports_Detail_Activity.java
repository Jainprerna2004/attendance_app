package com.ssgb.easyattendance;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ssgb.easyattendance.Adapter.Reports_Detail_Adapter;
import com.ssgb.easyattendance.database.AppDatabase;
import com.ssgb.easyattendance.database.entities.AttendanceStudentsList;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Reports_Detail_Activity extends AppCompatActivity {
    private TextView className, date;
    private RecyclerView recyclerView;
    private Reports_Detail_Adapter adapter;
    private AppDatabase db;
    private ExecutorService executorService;
    private String room_ID;
    private String classname;
    private String dateStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        room_ID = getIntent().getStringExtra("room_ID");
        classname = getIntent().getStringExtra("classname");
        dateStr = getIntent().getStringExtra("date");

        className = findViewById(R.id.class_name);
        date = findViewById(R.id.date);
        recyclerView = findViewById(R.id.recyclerView);

        className.setText(classname);
        date.setText(dateStr);

        db = AppDatabase.getInstance(this);
        executorService = Executors.newSingleThreadExecutor();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Reports_Detail_Adapter(this, null);
        recyclerView.setAdapter(adapter);

        loadAttendanceDetails();
    }

    private void loadAttendanceDetails() {
        executorService.execute(() -> {
            LiveData<List<AttendanceStudentsList>> attendanceLiveData = 
                db.attendanceStudentsListDao().getAttendanceByDateAndClassId(dateStr + "_" + room_ID);
            attendanceLiveData.observe(this, attendanceList -> {
                if (attendanceList != null) {
                    adapter.updateList(attendanceList);
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