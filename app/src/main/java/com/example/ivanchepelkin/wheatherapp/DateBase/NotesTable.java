package com.example.ivanchepelkin.wheatherapp.DateBase;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

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
    public static void addNote(String city, SQLiteDatabase database) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_LAST_UPDATE, city);

        database.insert(TABLE_NAME, null, values);
    }

    public static void editNote(int noteToEdit, int newNote, SQLiteDatabase database) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_LAST_UPDATE, newNote);

        //database.update(TABLE_NAME, values, COLUMN_LAST_UPDATE + "=" + noteToEdit, null);
        /*database.update(TABLE_NAME, values, "%s = %s",
                new String[] {COLUMN_LAST_UPDATE, String.valueOf(noteToEdit)});*/
        database.execSQL("UPDATE " + TABLE_NAME + " set " + COLUMN_LAST_UPDATE + " = 100 " + "WHERE "
                + COLUMN_LAST_UPDATE + " = " + noteToEdit + ";");
    }

    public static void deleteNote(int note, SQLiteDatabase database) {
        database.delete(TABLE_NAME, COLUMN_LAST_UPDATE + " = " + note, null);
        //DELETE FROM Notes WHERE note = 5;
    }

    public static void deleteAll(SQLiteDatabase database) {
        database.delete(TABLE_NAME, null, null);
        //DELETE * FROM Notes
    }

    public static List<Integer> getAllNotes(SQLiteDatabase database) {
        Cursor cursor = database.query(TABLE_NAME, null, null, null,
                null, null, null);
        /* Здесь может быть абсолюьтно любой запрос на выборку, как и в принципе в любом запросе
        Cursor cursor1 = database.query(TABLE_NAME, new String[] {COLUMN_LAST_UPDATE},
                COLUMN_LAST_UPDATE + " = 5", null, null, null,"DESC",
                "25");*/
        //Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        /*Cursor cursor1 = database.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_LAST_UPDATE +
                "= 100", null);*/
        return getResultFromCursor(cursor);
    }

    private static List<Integer> getResultFromCursor(Cursor cursor) {
        List<Integer> result = null;

        if (cursor != null && cursor.moveToFirst()) {
            result = new ArrayList<>(cursor.getCount());

            int noteIdx = cursor.getColumnIndex(COLUMN_LAST_UPDATE);
            int i = 0;
            do {
                result.add(cursor.getInt(noteIdx));
                i++;
            } while (cursor.moveToNext());
        }

        try {
            cursor.close();
        } catch (Exception ignored) {
        }
        return result == null ? new ArrayList<Integer>(0) : result;
    }
}

