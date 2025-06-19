package com.ssgb.easyattendance;

import android.app.Application;

import com.google.firebase.FirebaseApp;
import com.ssgb.easyattendance.realm.AppDatabase;

public class MyApplication extends Application {
    private static AppDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        
        // Initialize Firebase
        FirebaseApp.initializeApp(this);
        
        // Initialize database
        database = AppDatabase.getInstance(this);
    }

    public static AppDatabase getDatabase() {
        return database;
    }
} 