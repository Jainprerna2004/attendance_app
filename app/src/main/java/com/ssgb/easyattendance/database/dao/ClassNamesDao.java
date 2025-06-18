package com.ssgb.easyattendance.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.ssgb.easyattendance.database.entities.ClassNames;

import java.util.List;

@Dao
public interface ClassNamesDao {
    @Insert
    void insert(ClassNames classNames);

    @Update
    void update(ClassNames classNames);

    @Delete
    void delete(ClassNames classNames);

    @Query("SELECT * FROM class_names")
    LiveData<List<ClassNames>> getAllClasses();

    @Query("SELECT * FROM class_names WHERE class_id = :classId")
    LiveData<ClassNames> getClassById(String classId);

    @Query("SELECT * FROM class_names WHERE class_name = :className")
    LiveData<ClassNames> getClassByName(String className);
} 