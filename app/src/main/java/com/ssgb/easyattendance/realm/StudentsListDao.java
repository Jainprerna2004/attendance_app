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

    @Query("SELECT * FROM students_list WHERE class_id = :classId")
    List<Students_List> getStudentsByClassId(String classId);

    @Query("SELECT * FROM students_list WHERE name_student LIKE :name")
    List<Students_List> findByName(String name);

    @Query("SELECT COUNT(*) FROM students_list WHERE class_id = :classId")
    int countByClassId(String classId);

    @Query("SELECT * FROM students_list")
    List<Students_List> getAll();

    @Query("DELETE FROM students_list WHERE class_id = :classId")
    void deleteByClassId(String classId);

    @Query("SELECT * FROM students_list WHERE regNo_student = :regNo AND class_id = :classId LIMIT 1")
    Students_List getStudentByRegNoAndClassId(String regNo, String classId);
} 