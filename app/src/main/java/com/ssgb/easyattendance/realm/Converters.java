package com.ssgb.easyattendance.realm;

import androidx.room.TypeConverter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

/**
 * Type converters for Room database
 * Handles conversion between complex data types and database storage types
 */
public class Converters {

    private static final Gson gson = new Gson();

    /**
     * Convert Date to Long for database storage
     */
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    /**
     * Convert Long from database to Date
     */
    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    /**
     * Convert List<String> to JSON string for database storage
     */
    @TypeConverter
    public static List<String> fromStringList(String value) {
        if (value == null) {
            return null;
        }
        Type listType = new TypeToken<List<String>>() {}.getType();
        return gson.fromJson(value, listType);
    }

    /**
     * Convert JSON string from database to List<String>
     */
    @TypeConverter
    public static String stringListToString(List<String> list) {
        if (list == null) {
            return null;
        }
        return gson.toJson(list);
    }

    /**
     * Convert List<Integer> to JSON string for database storage
     */
    @TypeConverter
    public static List<Integer> fromIntegerList(String value) {
        if (value == null) {
            return null;
        }
        Type listType = new TypeToken<List<Integer>>() {}.getType();
        return gson.fromJson(value, listType);
    }

    /**
     * Convert JSON string from database to List<Integer>
     */
    @TypeConverter
    public static String integerListToString(List<Integer> list) {
        if (list == null) {
            return null;
        }
        return gson.toJson(list);
    }

    /**
     * Convert Boolean to Integer for database storage
     */
    @TypeConverter
    public static Boolean fromInteger(Integer value) {
        return value == null ? null : value == 1;
    }

    /**
     * Convert Integer from database to Boolean
     */
    @TypeConverter
    public static Integer booleanToInteger(Boolean value) {
        return value == null ? null : (value ? 1 : 0);
    }

    /**
     * Convert String array to JSON string for database storage
     */
    @TypeConverter
    public static String[] fromStringArray(String value) {
        if (value == null) {
            return null;
        }
        Type arrayType = new TypeToken<String[]>() {}.getType();
        return gson.fromJson(value, arrayType);
    }

    /**
     * Convert JSON string from database to String array
     */
    @TypeConverter
    public static String stringArrayToString(String[] array) {
        if (array == null) {
            return null;
        }
        return gson.toJson(array);
    }
} 