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
    void insert(Attendance_Reports report);

    @Update
    void update(Attendance_Reports report);

    @Delete
    void delete(Attendance_Reports report);

    @Query("SELECT * FROM attendance_reports WHERE classId = :classId ORDER BY date DESC")
    List<Attendance_Reports> getByClassId(String classId);

    @Query("SELECT * FROM attendance_reports WHERE date_and_classID = :dateAndClassId")
    Attendance_Reports getByDateAndClassId(String dateAndClassId);

    @Query("SELECT * FROM attendance_reports")
    List<Attendance_Reports> getAll();
} 