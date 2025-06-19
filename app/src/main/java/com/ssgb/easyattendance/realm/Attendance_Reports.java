package com.ssgb.easyattendance.realm;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
import androidx.room.Ignore;
import androidx.annotation.NonNull;
import java.util.List;

@Entity(tableName = "attendance_reports")
public class Attendance_Reports {

    @PrimaryKey
    @ColumnInfo(name = "report_id")
    @NonNull
    private String report_id;

    @ColumnInfo(name = "date")
    private String date;

    @ColumnInfo(name = "class_id")
    private String class_id;

    @ColumnInfo(name = "classname")
    private String classname;

    @ColumnInfo(name = "subjName")
    private String subjName;

    @ColumnInfo(name = "date_and_classID")
    private String date_and_classID;

    @ColumnInfo(name = "dateOnly")
    private String dateOnly;

    @ColumnInfo(name = "monthOnly")
    private String monthOnly;

    @ColumnInfo(name = "total_students")
    private int total_students;

    @ColumnInfo(name = "present_count")
    private int present_count;

    @ColumnInfo(name = "absent_count")
    private int absent_count;

    // Default constructor
    public Attendance_Reports() {}

    // Constructor with parameters - ignored by Room
    @Ignore
    public Attendance_Reports(String report_id, String date, String class_id, String classname, 
                           String subjName, String date_and_classID, String dateOnly, String monthOnly) {
        this.report_id = report_id;
        this.date = date;
        this.class_id = class_id;
        this.classname = classname;
        this.subjName = subjName;
        this.date_and_classID = date_and_classID;
        this.dateOnly = dateOnly;
        this.monthOnly = monthOnly;
    }

    // Getters and Setters
    @NonNull
    public String getReport_id() {
        return report_id;
    }

    public void setReport_id(@NonNull String report_id) {
        this.report_id = report_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getClass_id() {
        return class_id;
    }

    public void setClass_id(String class_id) {
        this.class_id = class_id;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getSubjName() {
        return subjName;
    }

    public void setSubjName(String subjName) {
        this.subjName = subjName;
    }

    public String getDate_and_classID() {
        return date_and_classID;
    }

    public void setDate_and_classID(String date_and_classID) {
        this.date_and_classID = date_and_classID;
    }

    public String getDateOnly() {
        return dateOnly;
    }

    public void setDateOnly(String dateOnly) {
        this.dateOnly = dateOnly;
    }

    public String getMonthOnly() {
        return monthOnly;
    }

    public void setMonthOnly(String monthOnly) {
        this.monthOnly = monthOnly;
    }

    public int getTotal_students() {
        return total_students;
    }

    public void setTotal_students(int total_students) {
        this.total_students = total_students;
    }

    public int getPresent_count() {
        return present_count;
    }

    public void setPresent_count(int present_count) {
        this.present_count = present_count;
    }

    public int getAbsent_count() {
        return absent_count;
    }

    public void setAbsent_count(int absent_count) {
        this.absent_count = absent_count;
    }
} 