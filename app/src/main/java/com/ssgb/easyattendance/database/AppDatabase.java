package com.ssgb.easyattendance.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.ssgb.easyattendance.database.dao.AttendanceReportsDao;
import com.ssgb.easyattendance.database.dao.AttendanceStudentsListDao;
import com.ssgb.easyattendance.database.dao.ClassNamesDao;
import com.ssgb.easyattendance.database.dao.StudentsDao;
import com.ssgb.easyattendance.database.entities.AttendanceReports;
import com.ssgb.easyattendance.database.entities.AttendanceStudentsList;
import com.ssgb.easyattendance.database.entities.ClassNames;
import com.ssgb.easyattendance.database.entities.Students;

@Database(entities = {
    ClassNames.class,
    Students.class,
    AttendanceReports.class,
    AttendanceStudentsList.class
}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "attendance_db";
    private static AppDatabase instance;

    public abstract ClassNamesDao classNamesDao();
    public abstract StudentsDao studentsDao();
    public abstract AttendanceReportsDao attendanceReportsDao();
    public abstract AttendanceStudentsListDao attendanceStudentsListDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                context.getApplicationContext(),
                AppDatabase.class,
                DATABASE_NAME
            ).build();
        }
        return instance;
    }
} 