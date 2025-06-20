package com.ssgb.easyattendance.realm;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.annotation.NonNull;

@Database(entities = {Students_List.class, Class_Names.class, Attendance_Students_List.class, Attendance_Reports.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;
    
    public abstract StudentsListDao studentsListDao();
    public abstract ClassNamesDao classNamesDao();
    public abstract AttendanceStudentsListDao attendanceStudentsListDao();
    public abstract AttendanceReportsDao attendanceReportsDao();
    
    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                context.getApplicationContext(),
                AppDatabase.class,
                "attendance_database"
            ).build();
        }
        return instance;
    }
} 