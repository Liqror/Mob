package com.example.mob;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Константы для имени и версии базы данных
    private static final String DATABASE_NAME = "notes_database";
    private static final int DATABASE_VERSION = 1;

    // Конструктор
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Методы onCreate и onUpgrade
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Создание таблицы
        db.execSQL("CREATE TABLE notes (id INTEGER PRIMARY KEY, title TEXT, location TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Обновление базы данных, если необходимо
        db.execSQL("DROP TABLE IF EXISTS notes");
        onCreate(db);
    }

    public boolean addNoteToDatabase(String title, String location) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("location", location);

        long newRowId = db.insert("notes", null, values);
        return newRowId != -1;
    }

//     Метод для получения всех заметок из базы данных
    public List<Note> getAllNotes() {
        List<Note> notes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Здесь выполняется запрос к базе данных для получения всех заметок
        Cursor cursor = db.query("notes", null, null, null, null, null, null);
        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int idIndex = cursor.getColumnIndex("id");
                    int titleIndex = cursor.getColumnIndex("title");
                    int locationIndex = cursor.getColumnIndex("location");

                    int id = cursor.getInt(idIndex);
                    String title = cursor.getString(titleIndex);
                    String location = cursor.getString(locationIndex);

                    notes.add(new Note(id, title, location));
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return notes;
    }

}
