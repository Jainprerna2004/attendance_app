package com.ssgb.easyattendance.realm;

import androidx.room.TypeConverter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class Converters {
    @TypeConverter
    public static List<Attendance_Students_List> fromString(String value) {
        if (value == null) return Collections.emptyList();
        Type listType = new TypeToken<List<Attendance_Students_List>>(){}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromList(List<Attendance_Students_List> list) {
        return new Gson().toJson(list);
    }
} 