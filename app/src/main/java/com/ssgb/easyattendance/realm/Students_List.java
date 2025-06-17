package com.ssgb.easyattendance.realm;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
import androidx.annotation.NonNull;

@Entity(tableName = "students_list")
public class Students_List {

    @PrimaryKey
    @ColumnInfo(name = "id")
    @NonNull
    String id;

    @ColumnInfo(name = "name_student")
    String name_student;

    @ColumnInfo(name = "regNo_student")
    String regNo_student;

    @ColumnInfo(name = "mobileNo_student")
    String mobileNo_student;

    @ColumnInfo(name = "class_id")
    String class_id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName_student() {
        return name_student;
    }

    public void setName_student(String name_student) {
        this.name_student = name_student;
    }

    public String getRegNo_student() {
        return regNo_student;
    }

    public void setRegNo_student(String regNo_student) {
        this.regNo_student = regNo_student;
    }

    public String getClass_id() {
        return class_id;
    }

    public void setClass_id(String class_id) {
        this.class_id = class_id;
    }

    public String getMobileNo_student() {
        return mobileNo_student;
    }

    public void setMobileNo_student(String mobileNo_student) {
        this.mobileNo_student = mobileNo_student;
    }

}
