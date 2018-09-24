package com.example.ivanchepelkin.wheatherapp.DateBase;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public class NotesTable {
    private final static String TABLE_NAME = "Notes";
    private final static String COLUMN_ID = "_id";
    private final static String COLUMN_CITY = "city";
    private final static String COLUMN_LAST_UPDATE = "last_update";
    private final static String COLUMN_ICON = "icon";
    private final static String COLUMN_TEMP = "temper";
    private final static String COLUMN_PRESSURE = "pressure";
    private final static String COLUMN_CLOUDY = "cloudy";
    private final static String COLUMN_HOMIDITY = "homidity";
    private final static String COLUMN_TITLE = "title";

    // создаем таблицу БД с нужнымми стобцами
    @SuppressLint("SQLiteString")
    public static void createTable(SQLiteDatabase database) {
        database.execSQL("CREATE TABLE " + TABLE_NAME + " (" + COLUMN_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_CITY + " STRING," +
                COLUMN_LAST_UPDATE + " STRING," + COLUMN_ICON + " STRING," +
                COLUMN_TEMP + " STRING," + COLUMN_PRESSURE + " STRING," +
                COLUMN_CLOUDY + " STRING," + COLUMN_HOMIDITY + " STRING);");
    }

    // обновление базы данных добавлением столбца
    public static void onUpgrade(SQLiteDatabase database) {
        database.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_ICON +
                " TEXT DEFAULT 'Default title'");
    }

    // добавление города с погодой и деталями погоды
    public static void addWeatherInBase(String city, String lastUpgrade, String icon, String pressure,
                                        String temper, String cloudy, String homidity, SQLiteDatabase database) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_CITY, city);
        values.put(COLUMN_LAST_UPDATE, lastUpgrade);
        values.put(COLUMN_ICON, icon);
        values.put(COLUMN_TEMP, temper);
        values.put(COLUMN_PRESSURE, pressure);
        values.put(COLUMN_CLOUDY, cloudy);
        values.put(COLUMN_HOMIDITY, homidity);
        database.insert(TABLE_NAME, null, values);
    }
}

