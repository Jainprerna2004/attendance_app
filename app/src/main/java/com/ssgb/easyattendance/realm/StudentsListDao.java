package com.ssgb.easyattendance.realm;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;
import java.util.List;

@Dao
public interface StudentsListDao {
    @Insert
    void insert(Students_List student);

    @Update
    void update(Students_List student);

    @Delete
    void delete(Students_List student);

    @Query("SELECT * FROM students_list WHERE class_id = :classId ORDER BY name_student ASC")
    List<Students_List> getStudentsByClassId(String classId);

    @Query("SELECT * FROM students_list WHERE name_student LIKE :name")
    List<Students_List> findByName(String name);

    @Query("SELECT COUNT(*) FROM students_list WHERE class_id = :classId")
    int countByClassId(String classId);

    @Query("SELECT * FROM students_list")
    List<Students_List> getAll();
} 