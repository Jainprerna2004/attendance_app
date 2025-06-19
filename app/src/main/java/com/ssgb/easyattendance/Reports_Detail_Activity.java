package com.ssgb.easyattendance;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ssgb.easyattendance.Adapter.Reports_Detail_Adapter;
import com.ssgb.easyattendance.realm.AppDatabase;
import com.ssgb.easyattendance.realm.Attendance_Students_List;
import com.ssgb.easyattendance.realm.Attendance_Reports;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Reports_Detail_Activity extends AppCompatActivity {
    private TextView className;
    private TextView subjectName;
    private RecyclerView recyclerView;
    private Reports_Detail_Adapter adapter;
    private AppDatabase db;
    private ExecutorService executorService;
    private String room_ID;
    private String classname;
    private String subjectname;
    private String dateStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports__detail);

        Toolbar toolbar = findViewById(R.id.toolbar_reports_detail);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        room_ID = getIntent().getStringExtra("room_ID");
        classname = getIntent().getStringExtra("classname");
        subjectname = getIntent().getStringExtra("subjectname");
        dateStr = getIntent().getStringExtra("date");

        className = findViewById(R.id.classname_report_detail);
        subjectName = findViewById(R.id.subjName_report_detail);
        recyclerView = findViewById(R.id.recyclerView_reports_detail);

        className.setText(classname);
        subjectName.setText(subjectname);
        
        // Set the date in the toolbar title
        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        if (toolbarTitle != null) {
            toolbarTitle.setText("Date: " + dateStr);
        }

        db = AppDatabase.getInstance(this);
        executorService = Executors.newSingleThreadExecutor();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Reports_Detail_Adapter(this, null);
        recyclerView.setAdapter(adapter);

        // Initially hide the no data text
        TextView noDataText = findViewById(R.id.no_data_text);
        if (noDataText != null) {
            noDataText.setVisibility(View.GONE);
        }

        loadAttendanceDetails();
    }

    private void loadAttendanceDetails() {
        executorService.execute(() -> {
            String queryString = dateStr + room_ID;
            System.out.println("Querying for: " + queryString);
            
            List<Attendance_Students_List> attendanceList = 
                db.attendanceStudentsListDao().getByDateAndClassId(queryString);
            
            System.out.println("Found " + (attendanceList != null ? attendanceList.size() : 0) + " records");
            
            runOnUiThread(() -> {
                if (attendanceList != null && !attendanceList.isEmpty()) {
                    System.out.println("Updating adapter with " + attendanceList.size() + " items");
                    adapter.updateList(attendanceList);
                } else {
                    System.out.println("No attendance records found for: " + queryString);
                    
                    // Show message that no attendance was taken today
                    TextView noDataText = findViewById(R.id.no_data_text);
                    if (noDataText != null) {
                        noDataText.setVisibility(View.VISIBLE);
                        noDataText.setText("No attendance taken for " + dateStr + "\n\nTap to view available dates");
                    }
                    
                    // Make the text clickable to show available dates
                    if (noDataText != null) {
                        noDataText.setOnClickListener(v -> showAvailableDates());
                    }
                    
                    // Try to get all records to see what's in the database
                    executorService.execute(() -> {
                        List<Attendance_Students_List> allRecords = db.attendanceStudentsListDao().getAllStudents();
                        System.out.println("Total records in database: " + (allRecords != null ? allRecords.size() : 0));
                        if (allRecords != null) {
                            for (Attendance_Students_List record : allRecords) {
                                System.out.println("Record: " + record.getDateAndClassId() + " - " + record.getStudentName());
                            }
                        }
                    });
                }
            });
        });
    }

    private void showAvailableDates() {
        executorService.execute(() -> {
            // Get all attendance reports for this class
            List<Attendance_Reports> reports = db.attendanceReportsDao().getByClassId(room_ID);
            
            if (reports != null && !reports.isEmpty()) {
                // Create a list of available dates
                String[] dates = new String[reports.size()];
                for (int i = 0; i < reports.size(); i++) {
                    dates[i] = reports.get(i).getDate();
                }
                
                runOnUiThread(() -> {
                    new androidx.appcompat.app.AlertDialog.Builder(this)
                        .setTitle("Available Attendance Dates")
                        .setItems(dates, (dialog, which) -> {
                            // Load attendance for selected date
                            String selectedDate = dates[which];
                            dateStr = selectedDate;
                            
                            // Update toolbar title
                            TextView toolbarTitle = findViewById(R.id.toolbar_title);
                            if (toolbarTitle != null) {
                                toolbarTitle.setText("Date: " + dateStr);
                            }
                            
                            // Hide no data text
                            TextView noDataText = findViewById(R.id.no_data_text);
                            if (noDataText != null) {
                                noDataText.setVisibility(View.GONE);
                            }
                            
                            // Load attendance for the selected date
                            loadAttendanceDetails();
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
                });
            } else {
                runOnUiThread(() -> {
                    Toast.makeText(this, "No attendance records found for this class", Toast.LENGTH_LONG).show();
                });
            }
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