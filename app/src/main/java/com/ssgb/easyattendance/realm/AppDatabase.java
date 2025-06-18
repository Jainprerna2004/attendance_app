package com.ssgb.easyattendance.realm;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Students_List.class, Class_Names.class, Attendance_Students_List.class, Attendance_Reports.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract StudentsListDao studentsListDao();
    public abstract ClassNamesDao classNamesDao();
    public abstract AttendanceStudentsListDao attendanceStudentsListDao();
    public abstract AttendanceReportsDao attendanceReportsDao();
} 