package com.ssgb.easyattendance;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.Manifest;
import android.content.pm.PackageManager;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.ssgb.easyattendance.Adapter.ReportsAdapter;
import com.ssgb.easyattendance.realm.AppDatabase;
import com.ssgb.easyattendance.realm.Attendance_Reports;
import com.ssgb.easyattendance.utils.PdfExporter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
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
    private CompactCalendarView calendarView;
    private TextView selectedDateText;
    private TextView noReportsText;
    private TextView monthYearText;
    private TextView viewMonthReportsBtn;
    private ImageView prevMonthBtn;
    private ImageView nextMonthBtn;
    private List<Attendance_Reports> allReports;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
    private SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
    private Calendar currentCalendar;
    private TextView exportPdfBtn;
    private static final int CREATE_FILE_REQUEST_CODE = 102;

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
        calendarView = findViewById(R.id.calendar_view);
        selectedDateText = findViewById(R.id.selected_date_text);
        noReportsText = findViewById(R.id.no_reports_text);
        monthYearText = findViewById(R.id.month_year_text);
        viewMonthReportsBtn = findViewById(R.id.view_month_reports_btn);
        prevMonthBtn = findViewById(R.id.prev_month_btn);
        nextMonthBtn = findViewById(R.id.next_month_btn);
        exportPdfBtn = findViewById(R.id.export_pdf_btn);

        db = AppDatabase.getInstance(this);
        executorService = Executors.newSingleThreadExecutor();
        currentCalendar = Calendar.getInstance();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ReportsAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(adapter);

        setupCalendar();
        setupMonthNavigation();
        setupMonthReportsButton();
        setupExportPdfButton();
        loadAllReports();
    }

    private void setupCalendar() {
        // Set calendar listener
        calendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                filterReportsByDate(dateClicked);
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                // Update month display when user scrolls
                Calendar cal = Calendar.getInstance();
                cal.setTime(firstDayOfNewMonth);
                currentCalendar = cal;
                updateMonthYearDisplay();
            }
        });

        // Set today as selected initially
        calendarView.setCurrentDate(new Date());
        updateMonthYearDisplay();
    }

    private void setupMonthNavigation() {
        prevMonthBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateMonth(-1);
            }
        });

        nextMonthBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateMonth(1);
            }
        });
    }

    private void setupMonthReportsButton() {
        viewMonthReportsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAllReportsForCurrentMonth();
            }
        });
    }

    private void setupExportPdfButton() {
        exportPdfBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exportMonthlyReportPdf();
            }
        });
    }

    private void navigateMonth(int direction) {
        currentCalendar.add(Calendar.MONTH, direction);
        Date newDate = currentCalendar.getTime();
        calendarView.setCurrentDate(newDate);
        updateMonthYearDisplay();
        
        // Refresh events for the new month
        refreshCalendarEvents();
    }

    private void updateMonthYearDisplay() {
        String monthYear = monthFormat.format(currentCalendar.getTime());
        monthYearText.setText(monthYear);
    }

    private void refreshCalendarEvents() {
        // Clear existing events
        calendarView.removeAllEvents();
        
        // Add events for the current month view
        if (allReports != null) {
            for (Attendance_Reports report : allReports) {
                if (report.getDate() != null) {
                    try {
                        Date date = dateFormat.parse(report.getDate());
                        if (date != null) {
                            Calendar reportCal = Calendar.getInstance();
                            reportCal.setTime(date);
                            
                            // Check if the report is in the current month view
                            if (reportCal.get(Calendar.YEAR) == currentCalendar.get(Calendar.YEAR) &&
                                reportCal.get(Calendar.MONTH) == currentCalendar.get(Calendar.MONTH)) {
                                Event event = new Event(Color.RED, date.getTime());
                                calendarView.addEvent(event);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void loadAllReports() {
        executorService.execute(() -> {
            allReports = db.attendanceReportsDao().getByClassId(room_ID);
            runOnUiThread(() -> {
                if (allReports != null) {
                    // Add events to calendar for dates with reports
                    addEventsToCalendar();
                    // Show today's reports initially
                    filterReportsByDate(new Date());
                }
            });
        });
    }

    private void addEventsToCalendar() {
        if (allReports != null) {
            for (Attendance_Reports report : allReports) {
                if (report.getDate() != null) {
                    try {
                        Date date = dateFormat.parse(report.getDate());
                        if (date != null) {
                            Calendar reportCal = Calendar.getInstance();
                            reportCal.setTime(date);
                            
                            // Only add events for the current month view
                            if (reportCal.get(Calendar.YEAR) == currentCalendar.get(Calendar.YEAR) &&
                                reportCal.get(Calendar.MONTH) == currentCalendar.get(Calendar.MONTH)) {
                                Event event = new Event(Color.RED, date.getTime());
                                calendarView.addEvent(event);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void filterReportsByDate(Date selectedDate) {
        if (allReports == null) return;

        String selectedDateString = dateFormat.format(selectedDate);
        List<Attendance_Reports> filteredReports = new ArrayList<>();

        for (Attendance_Reports report : allReports) {
            if (report.getDate() != null && report.getDate().equals(selectedDateString)) {
                filteredReports.add(report);
            }
        }

        // Update the UI
        selectedDateText.setText("Reports for " + selectedDateString + ":");
        
        if (filteredReports.isEmpty()) {
            noReportsText.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            viewMonthReportsBtn.setVisibility(View.VISIBLE);
        } else {
            noReportsText.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            viewMonthReportsBtn.setVisibility(View.VISIBLE);
            adapter.updateList(filteredReports);
        }
    }

    private void showAllReportsForCurrentMonth() {
        if (allReports == null) return;

        List<Attendance_Reports> monthReports = new ArrayList<>();
        String currentMonthYear = monthFormat.format(currentCalendar.getTime());

        for (Attendance_Reports report : allReports) {
            if (report.getDate() != null) {
                try {
                    Date date = dateFormat.parse(report.getDate());
                    if (date != null) {
                        Calendar reportCal = Calendar.getInstance();
                        reportCal.setTime(date);
                        
                        // Check if the report is in the current month view
                        if (reportCal.get(Calendar.YEAR) == currentCalendar.get(Calendar.YEAR) &&
                            reportCal.get(Calendar.MONTH) == currentCalendar.get(Calendar.MONTH)) {
                            monthReports.add(report);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        // Update the UI
        selectedDateText.setText("All Reports for " + currentMonthYear + ":");
        
        if (monthReports.isEmpty()) {
            noReportsText.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            viewMonthReportsBtn.setVisibility(View.GONE);
        } else {
            noReportsText.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            viewMonthReportsBtn.setVisibility(View.VISIBLE);
            adapter.updateList(monthReports);
        }
    }

    private void exportMonthlyReportPdf() {
        String defaultFileName = String.format("Attendance_%s_%s_%d_%02d.pdf",
                classname.replace(" ", "_"), subjName.replace(" ", "_"),
                currentCalendar.get(Calendar.YEAR), currentCalendar.get(Calendar.MONTH) + 1);

        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/pdf");
        intent.putExtra(Intent.EXTRA_TITLE, defaultFileName);
        startActivityForResult(intent, CREATE_FILE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CREATE_FILE_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null && data.getData() != null) {
                Uri uri = data.getData();
                performPdfExport(uri);
            }
        }
    }

    private void performPdfExport(Uri uri) {
        int year = currentCalendar.get(Calendar.YEAR);
        int month = currentCalendar.get(Calendar.MONTH);
        PdfExporter exporter = new PdfExporter(this);
        exporter.exportMonthlyReportToUri(room_ID, classname, subjName, year, month, uri, new PdfExporter.PdfExportCallback() {
            @Override
            public void onSuccess(String filePath) {
                runOnUiThread(() -> {
                    Toast.makeText(Reports_Activity.this, "PDF saved successfully!", Toast.LENGTH_LONG).show();
                    openPdf(uri);
                });
            }
            @Override
            public void onError(String error) {
                runOnUiThread(() -> Toast.makeText(Reports_Activity.this, error, Toast.LENGTH_LONG).show());
            }
        });
    }

    private void openPdf(Uri uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/pdf");
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_ACTIVITY_NO_HISTORY);
        try {
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "No PDF viewer found.", Toast.LENGTH_SHORT).show();
        }
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