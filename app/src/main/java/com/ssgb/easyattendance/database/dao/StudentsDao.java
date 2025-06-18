package com.ssgb.easyattendance.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.ssgb.easyattendance.database.entities.Students;

import java.util.List;

@Dao
public interface StudentsDao {
    @Insert
    void insert(Students student);

    @Update
    void update(Students student);

    @Delete
    void delete(Students student);

    @Query("SELECT * FROM students")
    LiveData<List<Students>> getAllStudents();

    @Query("SELECT * FROM students WHERE class_id = :classId")
    LiveData<List<Students>> getStudentsByClassId(String classId);

    @Query("SELECT * FROM students WHERE student_id = :studentId")
    LiveData<Students> getStudentById(String studentId);

    @Query("SELECT * FROM students WHERE student_roll_no = :rollNo")
    LiveData<Students> getStudentByRollNo(String rollNo);
} 