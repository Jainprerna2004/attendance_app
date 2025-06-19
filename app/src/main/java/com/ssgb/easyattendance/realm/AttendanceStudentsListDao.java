package com.ssgb.easyattendance.realm;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface AttendanceStudentsListDao {
    @Insert
    void insert(Attendance_Students_List attendanceStudent);

    @Update
    void update(Attendance_Students_List attendanceStudent);

    @Delete
    void delete(Attendance_Students_List attendanceStudent);

    @Query("SELECT * FROM attendance_students_list WHERE class_id = :classId")
    List<Attendance_Students_List> getByClassId(String classId);

    @Query("SELECT * FROM attendance_students_list WHERE date_and_classID = :dateAndClassId")
    List<Attendance_Students_List> getByDateAndClassId(String dateAndClassId);

    @Query("SELECT * FROM attendance_students_list WHERE unique_id = :uniqueId")
    Attendance_Students_List getByUniqueId(String uniqueId);

    @Query("SELECT * FROM attendance_students_list")
    List<Attendance_Students_List> getAllStudents();

    @Query("DELETE FROM attendance_students_list WHERE class_id = :classId")
    void deleteByClassId(String classId);
} 