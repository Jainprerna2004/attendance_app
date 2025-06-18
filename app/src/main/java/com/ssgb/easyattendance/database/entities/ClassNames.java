package com.ssgb.easyattendance.database.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "class_names", indices = {@Index("class_id")})
public class ClassNames {
    @PrimaryKey
    @NonNull
    private String class_id;
    private String class_name;
    private String subject_name;
    private String teacher_name;
    private String room_no;
    private String time;
    private String date;

    public ClassNames(String class_id, String class_name, String subject_name) {
        this.class_id = class_id;
        this.class_name = class_name;
        this.subject_name = subject_name;
    }

    @NonNull
    public String getClass_id() {
        return class_id;
    }

    public void setClass_id(@NonNull String class_id) {
        this.class_id = class_id;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getSubject_name() {
        return subject_name;
    }

    public void setSubject_name(String subject_name) {
        this.subject_name = subject_name;
    }

    public String getTeacher_name() {
        return teacher_name;
    }

    public void setTeacher_name(String teacher_name) {
        this.teacher_name = teacher_name;
    }

    public String getRoom_no() {
        return room_no;
    }

    public void setRoom_no(String room_no) {
        this.room_no = room_no;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
} 