package com.ssgb.easyattendance.database.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "attendance_reports",
        foreignKeys = @ForeignKey(entity = ClassNames.class,
                                parentColumns = "class_id",
                                childColumns = "class_id",
                                onDelete = ForeignKey.CASCADE),
        indices = {@Index("class_id")})
public class AttendanceReports {
    @PrimaryKey
    @NonNull
    private String report_id;
    private String date;
    private String class_id;
    private String classname;
    private String subjName;
    private String date_and_classID;
    private String dateOnly;
    private String monthOnly;
    private int total_students;
    private int present_count;
    private int absent_count;

    public AttendanceReports(String report_id, String date, String class_id, String classname, 
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