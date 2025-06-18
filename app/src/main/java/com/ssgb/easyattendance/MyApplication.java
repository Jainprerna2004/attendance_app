package com.ssgb.easyattendance;

import android.app.Application;

import com.ssgb.easyattendance.database.AppDatabase;

public class MyApplication extends Application {
    private static AppDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        database = AppDatabase.getInstance(this);
    }

    public static AppDatabase getDatabase() {
        return database;
    }
} 