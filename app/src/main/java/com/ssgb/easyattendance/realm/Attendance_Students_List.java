package com.ssgb.easyattendance.realm;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
import androidx.room.Ignore;
import androidx.annotation.NonNull;

@Entity(tableName = "attendance_students_list")
public class Attendance_Students_List {

    @PrimaryKey
    @ColumnInfo(name = "unique_id")
    @NonNull
    private String uniqueId;

    @ColumnInfo(name = "student_id")
    private String studentId;

    @ColumnInfo(name = "report_id")
    private String reportId;

    @ColumnInfo(name = "student_name")
    private String studentName;

    @ColumnInfo(name = "student_roll_no")
    private String studentRollNo;

    @ColumnInfo(name = "student_phone")
    private String studentPhone;

    @ColumnInfo(name = "attendance")
    private String attendance;

    @ColumnInfo(name = "class_id")
    private String classId;

    @ColumnInfo(name = "date_and_classID")
    private String dateAndClassId;

    // Default constructor
    public Attendance_Students_List() {
    }

    // Constructor with all parameters - ignored by Room
    @Ignore
    public Attendance_Students_List(String uniqueId, String studentId, String reportId,
                                    String studentName, String studentRollNo, String studentPhone,
                                    String attendance, String classId, String dateAndClassId) {
        this.uniqueId = uniqueId;
        this.studentId = studentId;
        this.reportId = reportId;
        this.studentName = studentName;
        this.studentRollNo = studentRollNo;
        this.studentPhone = studentPhone;
        this.attendance = attendance;
        this.classId = classId;
        this.dateAndClassId = dateAndClassId;
    }

    // Getters and Setters
    @NonNull
    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(@NonNull String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentRollNo() {
        return studentRollNo;
    }

    public void setStudentRollNo(String studentRollNo) {
        this.studentRollNo = studentRollNo;
    }

    public String getStudentPhone() {
        return studentPhone;
    }

    public void setStudentPhone(String studentPhone) {
        this.studentPhone = studentPhone;
    }

    public String getAttendance() {
        return attendance;
    }

    public void setAttendance(String attendance) {
        this.attendance = attendance;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getDateAndClassId() {
        return dateAndClassId;
    }

    public void setDateAndClassId(String dateAndClassId) {
        this.dateAndClassId = dateAndClassId;
    }
} 