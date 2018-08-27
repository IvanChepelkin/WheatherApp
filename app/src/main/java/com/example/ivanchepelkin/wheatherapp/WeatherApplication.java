package com.example.ivanchepelkin.wheatherapp;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

public class WeatherApplication extends Application {
    @SuppressLint("StaticFieldLeak")
    static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }
}
