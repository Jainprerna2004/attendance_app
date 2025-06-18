package com.ssgb.easyattendance.database.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "students",
        foreignKeys = @ForeignKey(entity = ClassNames.class,
                                parentColumns = "class_id",
                                childColumns = "class_id",
                                onDelete = ForeignKey.CASCADE),
        indices = {@Index("class_id")})
public class Students {
    @PrimaryKey
    @NonNull
    private String student_id;
    private String student_name;
    private String student_roll_no;
    private String student_phone;
    private String class_id;

    public Students(String student_id, String student_name, String student_roll_no, String student_phone, String class_id) {
        this.student_id = student_id;
        this.student_name = student_name;
        this.student_roll_no = student_roll_no;
        this.student_phone = student_phone;
        this.class_id = class_id;
    }

    @NonNull
    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(@NonNull String student_id) {
        this.student_id = student_id;
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

    public String getClass_id() {
        return class_id;
    }

    public void setClass_id(String class_id) {
        this.class_id = class_id;
    }
} 