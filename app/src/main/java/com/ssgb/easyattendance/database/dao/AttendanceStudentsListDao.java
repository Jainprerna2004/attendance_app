package com.ssgb.easyattendance.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.ssgb.easyattendance.database.entities.AttendanceStudentsList;

import java.util.List;

@Dao
public interface AttendanceStudentsListDao {
    @Insert
    void insert(AttendanceStudentsList attendance);

    @Update
    void update(AttendanceStudentsList attendance);

    @Delete
    void delete(AttendanceStudentsList attendance);

    @Query("SELECT * FROM attendance_students_list")
    LiveData<List<AttendanceStudentsList>> getAllAttendanceRecords();

    @Query("SELECT * FROM attendance_students_list WHERE report_id = :reportId")
    LiveData<List<AttendanceStudentsList>> getAttendanceByReportId(String reportId);

    @Query("SELECT * FROM attendance_students_list WHERE student_id = :studentId")
    LiveData<List<AttendanceStudentsList>> getAttendanceByStudentId(String studentId);

    @Query("SELECT * FROM attendance_students_list WHERE class_id = :classId")
    LiveData<List<AttendanceStudentsList>> getAttendanceByClassId(String classId);

    @Query("SELECT * FROM attendance_students_list WHERE date_and_classID = :dateAndClassId")
    LiveData<List<AttendanceStudentsList>> getAttendanceByDateAndClassId(String dateAndClassId);
} 