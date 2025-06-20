package com.ssgb.easyattendance;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ssgb.easyattendance.Adapter.StudentsListAdapter;
import com.ssgb.easyattendance.realm.AppDatabase;
import com.ssgb.easyattendance.realm.Attendance_Reports;
import com.ssgb.easyattendance.realm.Attendance_Students_List;
import com.ssgb.easyattendance.realm.Students_List;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClassDetail_Activity extends AppCompatActivity {

    private ImageView themeImage;
    private TextView className, total_students, place_holder;
    private CardView addStudent, reports_open;
    private Button submit_btn;
    private EditText student_name, reg_no, mobile_no;
    private LinearLayout layout_attendance_taken;
    private RecyclerView mRecyclerview;

    String room_ID, subject_Name, class_Name;

    public static final String TAG = "ClassDetail_Activity";

    AppDatabase db;
    ExecutorService executorService;
    StudentsListAdapter mAdapter;
    Handler handler = new Handler();

    ProgressBar progressBar;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_detail_);

        getWindow().setExitTransition(null);
        db = AppDatabase.getInstance(this);
        executorService = Executors.newSingleThreadExecutor();

        final String theme = getIntent().getStringExtra("theme");
        class_Name = getIntent().getStringExtra("className");
        subject_Name = getIntent().getStringExtra("subjectName");
        room_ID = getIntent().getStringExtra("classroom_ID");

        Toolbar toolbar = findViewById(R.id.toolbar_class_detail);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_disease_detail);
        collapsingToolbarLayout.setTitle(subject_Name);

        themeImage = findViewById(R.id.image_disease_detail);
        className = findViewById(R.id.classname_detail);
        total_students = findViewById(R.id.total_students_detail);
        layout_attendance_taken = findViewById(R.id.attendance_taken_layout);
        layout_attendance_taken.setVisibility(View.GONE);
        addStudent = findViewById(R.id.add_students);
        reports_open = findViewById(R.id.reports_open_btn);
        className.setText(class_Name);
        mRecyclerview = findViewById(R.id.recyclerView_detail);
        progressBar = findViewById(R.id.progressbar_detail);
        place_holder = findViewById(R.id.placeholder_detail);
        place_holder.setVisibility(View.GONE);
        submit_btn = findViewById(R.id.submit_attendance_btn);
        submit_btn.setVisibility(View.GONE);

        switch (theme) {
            case "0":
                themeImage.setImageResource(R.drawable.asset_bg_paleblue);
                break;
            case "1":
                themeImage.setImageResource(R.drawable.asset_bg_green);
                break;
            case "2":
                themeImage.setImageResource(R.drawable.asset_bg_yellow);
                break;
            case "3":
                themeImage.setImageResource(R.drawable.asset_bg_palegreen);
                break;
            case "4":
                themeImage.setImageResource(R.drawable.asset_bg_paleorange);
                break;
            case "5":
                themeImage.setImageResource(R.drawable.asset_bg_white);
                break;
        }

        Runnable r = new Runnable() {
            @Override
            public void run() {
                RoomInit();
                progressBar.setVisibility(View.GONE);
            }
        };
        handler.postDelayed(r, 500);

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        final int count = db.studentsListDao().getStudentsByClassId(room_ID).size();
                        final String size, size2;
                        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ClassDetail_Activity.this);
                        size = String.valueOf(preferences.getAll().size());
                        size2 = String.valueOf(count);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (size.equals(size2)){
                                    submitAttendance();
                                }else {
                                    Toast.makeText(ClassDetail_Activity.this, "Select all........", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
            }
        });

        reports_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClassDetail_Activity.this, Reports_Activity.class);
                intent.putExtra("class_name", class_Name);
                intent.putExtra("subject_name", subject_Name);
                intent.putExtra("room_ID", room_ID);
                startActivity(intent);
            }
        });

        addStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddStudentDialog();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void RoomInit(){
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                final String date = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(new Date());
                final int count = db.studentsListDao().getStudentsByClassId(room_ID).size();
                final Attendance_Reports report = db.attendanceReportsDao().getByDateAndClassId(date, room_ID);
                List<Students_List> students = db.studentsListDao().getStudentsByClassId(room_ID);
                
                // Sort students by roll number
                java.util.Collections.sort(students, new java.util.Comparator<Students_List>() {
                    @Override
                    public int compare(Students_List s1, Students_List s2) {
                        try {
                            // Try to parse as integers for proper numeric sorting
                            int roll1 = Integer.parseInt(s1.getRegNo_student());
                            int roll2 = Integer.parseInt(s2.getRegNo_student());
                            return Integer.compare(roll1, roll2);
                        } catch (NumberFormatException e) {
                            // If parsing fails, fall back to string comparison
                            return s1.getRegNo_student().compareTo(s2.getRegNo_student());
                        }
                    }
                });
                
                final List<Students_List> sortedStudents = students;
                
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (report != null) {
                            layout_attendance_taken.setVisibility(View.VISIBLE);
                            submit_btn.setVisibility(View.GONE);
                        } else {
                            layout_attendance_taken.setVisibility(View.GONE);
                            submit_btn.setVisibility(View.VISIBLE);
                            if (count != 0) {
                                submit_btn.setVisibility(View.VISIBLE);
                                place_holder.setVisibility(View.GONE);
                            } else {
                                submit_btn.setVisibility(View.GONE);
                                place_holder.setVisibility(View.VISIBLE);
                            }
                        }
                        total_students.setText("Total Students : " + count);
                        mRecyclerview.setLayoutManager(new LinearLayoutManager(ClassDetail_Activity.this));
                        String extraClick = "";
                        mAdapter = new StudentsListAdapter(sortedStudents, ClassDetail_Activity.this, date+room_ID, extraClick);
                        mRecyclerview.setAdapter(mAdapter);
                    }
                });
            }
        });
    }

    public void submitAttendance(){
        final ProgressDialog progressDialog = new ProgressDialog(ClassDetail_Activity.this);
        progressDialog.setMessage("Please wait..");
        progressDialog.show();
        
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                final String date = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(new Date());
                List<Attendance_Students_List> list_students = db.attendanceStudentsListDao().getByDateAndClassId(date+room_ID);
                Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
                final String dateOnly = String.valueOf(calendar.get(Calendar.DATE));
                @SuppressLint("SimpleDateFormat")
                final String monthOnly = new SimpleDateFormat("MMM").format(calendar.getTime());
                try {
                    Attendance_Reports attendance_reports = new Attendance_Reports();
                    attendance_reports.setReport_id(date + "_" + room_ID + "_" + System.currentTimeMillis());
                    attendance_reports.setClass_id(room_ID);
                    attendance_reports.setDate(date);
                    attendance_reports.setDateOnly(dateOnly);
                    attendance_reports.setMonthOnly(monthOnly);
                    attendance_reports.setDate_and_classID(date+room_ID);
                    attendance_reports.setClassname(class_Name);
                    attendance_reports.setSubjName(subject_Name);
                    db.attendanceReportsDao().insert(attendance_reports);
                    
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ClassDetail_Activity.this);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.clear();
                            editor.commit();
                            Toast.makeText(ClassDetail_Activity.this, "Attendance Submitted", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            Toast.makeText(ClassDetail_Activity.this, "Error Occurred", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onDestroy() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
        super.onDestroy();
        executorService.shutdown();
    }

    public void addStudent(final String studentName, final String regNo, final String mobileNo) {
        final ProgressDialog progressDialog = new ProgressDialog(ClassDetail_Activity.this);
        progressDialog.setMessage("Adding student..");
        progressDialog.show();
        
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    // Check if student with same roll number already exists in this class
                    Students_List existingStudent = db.studentsListDao().getStudentByRegNoAndClassId(regNo, room_ID);
                    if (existingStudent != null) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                                Toast.makeText(ClassDetail_Activity.this, "Student with Roll No " + regNo + " already exists!", Toast.LENGTH_LONG).show();
                            }
                        });
                        return;
                    }
                    
                    Students_List student = new Students_List();
                    // Generate unique ID using timestamp and class ID to avoid conflicts
                    String uniqueId = room_ID + "_" + regNo + "_" + System.currentTimeMillis();
                    student.setId(uniqueId);
                    student.setName_student(studentName.trim());
                    student.setRegNo_student(regNo.trim());
                    student.setMobileNo_student(mobileNo.isEmpty() ? "N/A" : mobileNo.trim());
                    student.setClass_id(room_ID);
                    
                    db.studentsListDao().insert(student);
                    
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            Toast.makeText(ClassDetail_Activity.this, "Student Added Successfully", Toast.LENGTH_SHORT).show();
                            RoomInit(); // Refresh list
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            Toast.makeText(ClassDetail_Activity.this, "Error adding student: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }

    private void showAddStudentDialog() {
        View view = getLayoutInflater().inflate(R.layout.dialog_add_student, null);
        EditText nameInput = view.findViewById(R.id.name_input);
        EditText rollNoInput = view.findViewById(R.id.roll_no_input);

        androidx.appcompat.app.AlertDialog dialog = new androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Add New Student")
            .setView(view)
            .setPositiveButton("Add", null) // Set to null initially to prevent auto-dismiss
            .setNegativeButton("Cancel", null)
            .create();

        dialog.setOnShowListener(dialogInterface -> {
            Button positiveButton = dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(v -> {
                String name = nameInput.getText().toString().trim();
                String rollNo = rollNoInput.getText().toString().trim();

                // Validation
                if (name.isEmpty()) {
                    nameInput.setError("Student name is required");
                    nameInput.requestFocus();
                    return;
                }

                if (rollNo.isEmpty()) {
                    rollNoInput.setError("Roll number is required");
                    rollNoInput.requestFocus();
                    return;
                }

                // Check if roll number is numeric
                try {
                    Integer.parseInt(rollNo);
                } catch (NumberFormatException e) {
                    rollNoInput.setError("Roll number must be a number");
                    rollNoInput.requestFocus();
                    return;
                }

                // Check if roll number is positive
                if (Integer.parseInt(rollNo) <= 0) {
                    rollNoInput.setError("Roll number must be positive");
                    rollNoInput.requestFocus();
                    return;
                }

                dialog.dismiss();
                addStudent(name, rollNo, "");
            });
        });

        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_class_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}