package com.ssgb.easyattendance.realm;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
import androidx.annotation.NonNull;

@Entity(tableName = "class_names")
public class Class_Names {

    @PrimaryKey
    @ColumnInfo(name = "id")
    @NonNull
    String id;

    @ColumnInfo(name = "name_class")
    String name_class;

    @ColumnInfo(name = "name_subject")
    String name_subject;

    @ColumnInfo(name = "position_bg")
    String position_bg;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName_class() {
        return name_class;
    }

    public void setName_class(String name_class) {
        this.name_class = name_class;
    }

    public String getName_subject() {
        return name_subject;
    }

    public void setName_subject(String name_subject) {
        this.name_subject = name_subject;
    }

    public String getPosition_bg() {
        return position_bg;
    }

    public void setPosition_bg(String position_bg) {
        this.position_bg = position_bg;
    }
}
