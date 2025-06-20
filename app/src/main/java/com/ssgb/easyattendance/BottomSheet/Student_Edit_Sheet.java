package com.ssgb.easyattendance.BottomSheet;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.ssgb.easyattendance.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.ssgb.easyattendance.realm.AppDatabase;
import com.ssgb.easyattendance.realm.StudentsListDao;
import com.ssgb.easyattendance.realm.Students_List;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Student_Edit_Sheet extends BottomSheetDialogFragment {

    public String _name, _regNo, _mobNo, _classId;
    public EditText name_student, regNo_student, mobNo_student;
    public TextView saveButton;
    public CardView call;
    private AppDatabase db;
    private StudentsListDao studentsListDao;
    private ExecutorService executorService;

    public Student_Edit_Sheet(String stuName, String regNo, String mobileNo, String classId) {
        _name = stuName;
        _regNo = regNo;
        _mobNo = mobileNo;
        _classId = classId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.bottomsheet_student_edit, container, false);

        name_student = v.findViewById(R.id.stu_name_edit);
        regNo_student = v.findViewById(R.id.stu_regNo_edit);
        mobNo_student = v.findViewById(R.id.stu_mobNo_edit);
        saveButton = v.findViewById(R.id.save_button);
        call = v.findViewById(R.id.call_edit);

        // Initialize database
        db = AppDatabase.getInstance(requireContext());
        studentsListDao = db.studentsListDao();
        executorService = Executors.newSingleThreadExecutor();

        name_student.setText(_name);
        regNo_student.setText(_regNo);
        mobNo_student.setText(_mobNo);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveStudentChanges();
            }
        });

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uri = "tel:" + _mobNo.trim();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(uri));
                startActivity(intent);
            }
        });

        return v;
    }

    private void saveStudentChanges() {
        String newName = name_student.getText().toString().trim();
        String newRegNo = regNo_student.getText().toString().trim();
        String newMobileNo = mobNo_student.getText().toString().trim();

        // Validate input
        if (newName.isEmpty() || newRegNo.isEmpty() || newMobileNo.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    // Find the student by registration number and class ID
                    Students_List student = studentsListDao.getStudentByRegNoAndClassId(_regNo, _classId);
                    
                    if (student != null) {
                        // Update student information
                        student.setName_student(newName);
                        student.setRegNo_student(newRegNo);
                        student.setMobileNo_student(newMobileNo);
                        
                        studentsListDao.update(student);
                        
                        requireActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(requireContext(), "Student updated successfully", Toast.LENGTH_SHORT).show();
                                dismiss();
                                
                                // Notify parent activity to refresh the list
                                if (getActivity() != null) {
                                    getActivity().recreate();
                                }
                            }
                        });
                    } else {
                        requireActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(requireContext(), "Student not found", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (Exception e) {
                    requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(requireContext(), "Error updating student: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }
}
