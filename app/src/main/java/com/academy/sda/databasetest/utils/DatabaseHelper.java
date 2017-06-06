package com.academy.sda.databasetest.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "myDatabase.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE comments(_id INTEGER PRIMARY KEY AUTOINCREMENT, command TEXT NOT NULL);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String[] migrations = new String[3];

        migrations[2] = "ALTER TABLE comments ADD email TEXT";
        migrations[3] = "ALTER TABLE comments ADD name TEXT";

        for (int i = oldVersion+1; i<=newVersion; ++i){
            db.execSQL(migrations[i]);
        }
    }
}
