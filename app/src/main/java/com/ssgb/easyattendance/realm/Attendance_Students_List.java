package com.ssgb.easyattendance.realm;

import io.realm.annotations.PrimaryKey;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
import androidx.annotation.NonNull;

@Entity(tableName = "attendance_students_list")
public class Attendance_Students_List {

    @ColumnInfo(name = "studentName")
    String studentName;

    @ColumnInfo(name = "studentRegNo")
    String studentRegNo;

    @ColumnInfo(name = "attendance")
    String attendance;

    @ColumnInfo(name = "mobNo")
    String mobNo;

    @ColumnInfo(name = "classID")
    String classID;

    @ColumnInfo(name = "date_and_classID")
    String date_and_classID;

    @PrimaryKey
    @ColumnInfo(name = "unique_ID")
    @NonNull
    String unique_ID;

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentRegNo() {
        return studentRegNo;
    }

    public void setStudentRegNo(String studentRegNo) {
        this.studentRegNo = studentRegNo;
    }

    public String getAttendance() {
        return attendance;
    }

    public void setAttendance(String attendance) {
        this.attendance = attendance;
    }

    public String getMobNo() {
        return mobNo;
    }

    public void setMobNo(String mobNo) {
        this.mobNo = mobNo;
    }

    public String getClassID() {
        return classID;
    }

    public void setClassID(String classID) {
        this.classID = classID;
    }

    public String getDate_and_classID() {
        return date_and_classID;
    }

    public void setDate_and_classID(String date_and_classID) {
        this.date_and_classID = date_and_classID;
    }

    public String getUnique_ID() {
        return unique_ID;
    }

    public void setUnique_ID(String unique_ID) {
        this.unique_ID = unique_ID;
    }
}
