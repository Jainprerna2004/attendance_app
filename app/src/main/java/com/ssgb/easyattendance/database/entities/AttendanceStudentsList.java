package com.ssgb.easyattendance.database.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "attendance_students_list",
        foreignKeys = {
            @ForeignKey(entity = Students.class,
                       parentColumns = "student_id",
                       childColumns = "student_id",
                       onDelete = ForeignKey.CASCADE),
            @ForeignKey(entity = AttendanceReports.class,
                       parentColumns = "report_id",
                       childColumns = "report_id",
                       onDelete = ForeignKey.CASCADE)
        },
        indices = {
            @Index("student_id"),
            @Index("report_id")
        })
public class AttendanceStudentsList {
    @PrimaryKey
    @NonNull
    private String unique_id;
    private String student_id;
    private String report_id;
    private String student_name;
    private String student_roll_no;
    private String student_phone;
    private String attendance;
    private String class_id;
    private String date_and_classID;

    public AttendanceStudentsList(String unique_id, String student_id, String report_id,
                                String student_name, String student_roll_no, String student_phone,
                                String attendance, String class_id, String date_and_classID) {
        this.unique_id = unique_id;
        this.student_id = student_id;
        this.report_id = report_id;
        this.student_name = student_name;
        this.student_roll_no = student_roll_no;
        this.student_phone = student_phone;
        this.attendance = attendance;
        this.class_id = class_id;
        this.date_and_classID = date_and_classID;
    }

    @NonNull
    public String getUnique_id() {
        return unique_id;
    }

    public void setUnique_id(@NonNull String unique_id) {
        this.unique_id = unique_id;
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public String getReport_id() {
        return report_id;
    }

    public void setReport_id(String report_id) {
        this.report_id = report_id;
    }

    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }

    public String getStudent_roll_no() {
        return student_roll_no;
    }

    public void setStudent_roll_no(String student_roll_no) {
        this.student_roll_no = student_roll_no;
    }

    public String getStudent_phone() {
        return student_phone;
    }

    public void setStudent_phone(String student_phone) {
        this.student_phone = student_phone;
    }

    public String getAttendance() {
        return attendance;
    }

    public void setAttendance(String attendance) {
        this.attendance = attendance;
    }

    public String getClass_id() {
        return class_id;
    }

    public void setClass_id(String class_id) {
        this.class_id = class_id;
    }

    public String getDate_and_classID() {
        return date_and_classID;
    }

    public void setDate_and_classID(String date_and_classID) {
        this.date_and_classID = date_and_classID;
    }
} 