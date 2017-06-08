package com.academy.sda.databasetest.model.comments.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.academy.sda.databasetest.model.comments.Task;
import com.academy.sda.databasetest.utils.DatabaseHelper;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;


public class TasksDataSource {

    private DatabaseHelper databaseHelper;
    private SQLiteDatabase database;

    private final String TASKS_TABLE_NAME = "tasks";
    private final String ID_COLUMN = "_id";
    private final String DESCRIPTION_COLUMN = "description";
    private final String CATEGORY_COLUMN = "category";
    private final String DATE_COLUMN = "date";


    public TasksDataSource(Context context) {
        this.databaseHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        this.database = databaseHelper.getWritableDatabase();
    }

    public long update(Task task) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(DESCRIPTION_COLUMN, task.getDescription());
        contentValues.put(CATEGORY_COLUMN, task.getCategory());
        contentValues.put(DATE_COLUMN, task.getDate().toString());

        long recordId = this.database.update(TASKS_TABLE_NAME, contentValues, ID_COLUMN + "=" + task.getId(), null);

        return recordId;

    }

    public void delete(Task task) {
        database.delete(TASKS_TABLE_NAME, ID_COLUMN + "=" + task.getId(), null);

    }

    public void close() {
        this.databaseHelper.close();
    }

    public Task create(Task task) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(CATEGORY_COLUMN, task.getCategory());
        contentValues.put(DESCRIPTION_COLUMN, task.getDescription());
        contentValues.put(DATE_COLUMN, task.getDate().toString());

        long recordId = this.database.insert(TASKS_TABLE_NAME, null, contentValues);


        Cursor cursor = this.database.query(TASKS_TABLE_NAME,
                new String[]{ID_COLUMN, DESCRIPTION_COLUMN, CATEGORY_COLUMN, DATE_COLUMN},
                ID_COLUMN + "= " + recordId,
                null,
                null,
                null,
                null);
        cursor.moveToFirst();

        Task taskRecord = new Task();
        taskRecord.setId(cursor.getLong(0));
        taskRecord.setDescription(cursor.getString(1));
        taskRecord.setCategory(cursor.getString(2));
        taskRecord.setDate(LocalDate.parse(cursor.getString(3)));

        cursor.close();

        logDebug("Database takes in: " + taskRecord);

        return taskRecord;
    }

    public List<Task> getAll() {
        Cursor cursor = database.query(
                TASKS_TABLE_NAME,
                new String[]{ID_COLUMN, DESCRIPTION_COLUMN, CATEGORY_COLUMN, DATE_COLUMN},
                null,
                null,
                null,
                null,
                null,
                null);

        cursor.moveToFirst();
        List<Task> tasks = new ArrayList<>();
        while (!cursor.isAfterLast()) {
            Task taskRecord = new Task();
            taskRecord.setId(cursor.getLong(0));
            taskRecord.setDescription(cursor.getString(1));
            taskRecord.setCategory(cursor.getString(2));
            taskRecord.setDate(LocalDate.parse(cursor.getString(3)));
            tasks.add(taskRecord);
            cursor.moveToNext();
        }
        cursor.close();

        logDebug("Database returns: " + tasks);

        return tasks;
    }

    private void logDebug(String string) {
        Log.e(getClass().getSimpleName(), string);

    }
}
