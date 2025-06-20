package com.ssgb.easyattendance.viewholders;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.ssgb.easyattendance.BottomSheet.Student_Edit_Sheet;
import com.ssgb.easyattendance.R;
import com.ssgb.easyattendance.realm.AppDatabase;
import com.ssgb.easyattendance.realm.AttendanceStudentsListDao;
import com.ssgb.easyattendance.realm.Students_List;
import com.ssgb.easyattendance.realm.Attendance_Students_List;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ViewHolder_students extends RecyclerView.ViewHolder{

    public Activity mActivity;
    List<Students_List> mList;

    public final TextView student_name;
    public final TextView student_regNo;
    public LinearLayout layout;
    public String stuName, regNo, mobileNo, mRoomID;
    public RadioGroup radioGroup;
    public RadioButton radioButton_present, radioButton_absent;

    AppDatabase db;
    AttendanceStudentsListDao attendanceStudentsListDao;
    ExecutorService executorService;

    public ViewHolder_students(@NonNull final View itemView, Activity MainActivity, List<Students_List> list, final String roomID) {
        super(itemView);

        student_name = itemView.findViewById(R.id.student_name_adapter);
        student_regNo = itemView.findViewById(R.id.student_regNo_adapter);
        radioGroup = itemView.findViewById(R.id.radioGroup);
        radioButton_present = itemView.findViewById(R.id.radio_present);
        radioButton_absent = itemView.findViewById(R.id.radio_absent);
        layout = itemView.findViewById(R.id.layout_click);

        mActivity = MainActivity;
        mList = list;
        mRoomID = roomID;
        db = AppDatabase.getInstance(mActivity.getApplicationContext());
        attendanceStudentsListDao = db.attendanceStudentsListDao();
        executorService = Executors.newSingleThreadExecutor();
        
        // For demo: always show radioGroup (logic can be improved with Room queries if needed)
        radioGroup.setVisibility(View.VISIBLE);

        radioButton_present.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String attendance = "Present";
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mActivity);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(mList.get(getAdapterPosition()).getRegNo_student(), attendance);
                editor.apply();
                
                final String uniqueId = mList.get(getAdapterPosition()).getRegNo_student() + mRoomID;
                final Students_List student = mList.get(getAdapterPosition());
                
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        // Check if record exists
                        Attendance_Students_List existingRecord = attendanceStudentsListDao.getByUniqueId(uniqueId);
                        
                        if (existingRecord != null) {
                            // Update existing record
                            existingRecord.setAttendance(attendance);
                            attendanceStudentsListDao.update(existingRecord);
                        } else {
                            // Insert new record
                            Attendance_Students_List attendance_students_list = new Attendance_Students_List();
                            attendance_students_list.setStudentName(student.getName_student());
                            attendance_students_list.setAttendance(attendance);
                            attendance_students_list.setStudentPhone(student.getMobileNo_student());
                            attendance_students_list.setStudentRollNo(student.getRegNo_student());
                            attendance_students_list.setClassId(student.getClass_id());
                            attendance_students_list.setDateAndClassId(mRoomID);
                            attendance_students_list.setUniqueId(uniqueId);
                            attendanceStudentsListDao.insert(attendance_students_list);
                        }
                    }
                });
            }
        });
        
        radioButton_absent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String attendance = "Absent";
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mActivity);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(mList.get(getAdapterPosition()).getRegNo_student(), attendance);
                editor.apply();
                
                final String uniqueId = mList.get(getAdapterPosition()).getRegNo_student() + mRoomID;
                final Students_List student = mList.get(getAdapterPosition());
                
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        // Check if record exists
                        Attendance_Students_List existingRecord = attendanceStudentsListDao.getByUniqueId(uniqueId);
                        
                        if (existingRecord != null) {
                            // Update existing record
                            existingRecord.setAttendance(attendance);
                            attendanceStudentsListDao.update(existingRecord);
                        } else {
                            // Insert new record
                            Attendance_Students_List attendance_students_list = new Attendance_Students_List();
                            attendance_students_list.setStudentName(student.getName_student());
                            attendance_students_list.setAttendance(attendance);
                            attendance_students_list.setStudentPhone(student.getMobileNo_student());
                            attendance_students_list.setStudentRollNo(student.getRegNo_student());
                            attendance_students_list.setClassId(student.getClass_id());
                            attendance_students_list.setDateAndClassId(mRoomID);
                            attendance_students_list.setUniqueId(uniqueId);
                            attendanceStudentsListDao.insert(attendance_students_list);
                        }
                    }
                });
            }
        });

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stuName = mList.get(getAdapterPosition()).getName_student();
                regNo = mList.get(getAdapterPosition()).getRegNo_student();
                mobileNo = mList.get(getAdapterPosition()).getMobileNo_student();
                String classId = mList.get(getAdapterPosition()).getClass_id();
                Student_Edit_Sheet student_edit_sheet = new Student_Edit_Sheet(stuName, regNo, mobileNo, classId);
                student_edit_sheet.setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetTheme);
                student_edit_sheet.show(((FragmentActivity)view.getContext()).getSupportFragmentManager(), "BottomSheet");
            }
        });
    }

    public void bind(Attendance_Students_List student) {
        student_name.setText(student.getStudentName());
        student_regNo.setText(student.getStudentRollNo());
    }

    public void updateAttendance(String status) {
        // This method is no longer used in the new layout
    }

    public void cleanup() {
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }
}
