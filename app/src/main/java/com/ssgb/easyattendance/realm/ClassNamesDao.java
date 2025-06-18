package com.ssgb.easyattendance.realm;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;
import java.util.List;

@Dao
public interface ClassNamesDao {
    @Insert
    void insert(Class_Names className);

    @Update
    void update(Class_Names className);

    @Delete
    void delete(Class_Names className);

    @Query("SELECT * FROM class_names WHERE name_class LIKE :name")
    List<Class_Names> findByName(String name);

    @Query("SELECT * FROM class_names WHERE name_subject LIKE :subject")
    List<Class_Names> findBySubject(String subject);

    @Query("SELECT * FROM class_names")
    List<Class_Names> getAll();
} 