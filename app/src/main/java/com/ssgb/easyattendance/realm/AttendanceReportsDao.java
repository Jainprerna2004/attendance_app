package com.ssgb.easyattendance.realm;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;
import java.util.List;

@Dao
public interface AttendanceReportsDao {
    @Insert
    void insert(Attendance_Reports attendanceReport);

    @Update
    void update(Attendance_Reports attendanceReport);

    @Delete
    void delete(Attendance_Reports attendanceReport);

    @Query("SELECT * FROM attendance_reports WHERE class_id = :classId")
    List<Attendance_Reports> getByClassId(String classId);

    @Query("DELETE FROM attendance_reports WHERE class_id = :classId")
    void deleteByClassId(String classId);

    @Query("SELECT * FROM attendance_reports WHERE class_id = :classId AND date = :date")
    Attendance_Reports getByDateAndClassId(String date, String classId);

    @Query("SELECT * FROM attendance_reports")
    List<Attendance_Reports> getAllReports();
} 