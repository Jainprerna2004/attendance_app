package com.ssgb.easyattendance.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.ssgb.easyattendance.database.entities.AttendanceReports;

import java.util.List;

@Dao
public interface AttendanceReportsDao {
    @Insert
    void insert(AttendanceReports report);

    @Update
    void update(AttendanceReports report);

    @Delete
    void delete(AttendanceReports report);

    @Query("SELECT * FROM attendance_reports")
    LiveData<List<AttendanceReports>> getAllReports();

    @Query("SELECT * FROM attendance_reports WHERE class_id = :classId")
    LiveData<List<AttendanceReports>> getReportsByClassId(String classId);

    @Query("SELECT * FROM attendance_reports WHERE date = :date AND class_id = :classId")
    LiveData<AttendanceReports> getReportByDateAndClassId(String date, String classId);

    @Query("SELECT * FROM attendance_reports WHERE dateOnly = :dateOnly")
    LiveData<List<AttendanceReports>> getReportsByDate(String dateOnly);

    @Query("SELECT * FROM attendance_reports WHERE monthOnly = :monthOnly")
    LiveData<List<AttendanceReports>> getReportsByMonth(String monthOnly);
} 