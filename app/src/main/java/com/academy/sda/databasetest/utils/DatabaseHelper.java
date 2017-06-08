package com.academy.sda.databasetest.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(Context context) {
        super(context, "mydatabase.db", null, 1);
    }

    private final String TASKS_TABLE_NAME = "tasks";
    private final String ID_COLUMN = "_id";
    private final String DESCRIPTION_COLUMN = "description";
    private final String CATEGORY_COLUMN = "category";
    private final String DATE_COLUMN = "date";

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + TASKS_TABLE_NAME + " (" + ID_COLUMN
                + " INTEGER PRIMARY KEY AUTOINCREMENT, " + DESCRIPTION_COLUMN +
                " TEXT NOT NULL, " + CATEGORY_COLUMN + " TEXT NOT NULL, " +
                DATE_COLUMN + " TEXT NOT NULL);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
       /* String[] migrations = new String[3];

        migrations[2] = "ALTER TABLE comments ADD email TEXT";
        migrations[3] = "ALTER TABLE comments ADD name TEXT";

        for (int i = oldVersion+1; i<=newVersion; ++i){
            db.execSQL(migrations[i]);
        }*/
    }

    private void logDebug(String string) {
        Log.e(getClass().getSimpleName(), string);

    }
}
