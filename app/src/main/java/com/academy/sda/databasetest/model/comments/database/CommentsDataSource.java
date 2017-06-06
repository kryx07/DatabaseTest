package com.academy.sda.databasetest.model.comments.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.academy.sda.databasetest.model.comments.Comment;
import com.academy.sda.databasetest.utils.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wd42 on 06.06.17.
 */

public class CommentsDataSource {

    private DatabaseHelper databaseHelper;
    private SQLiteDatabase database;

    public CommentsDataSource(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    public void open() throws SQLException {
        this.database = databaseHelper.getWritableDatabase();
    }

    public void close() {
        this.databaseHelper.close();
    }

    public Comment createComment(String comment) {
        ContentValues contentValues = new ContentValues();

        contentValues.put("comment", comment);
        this.database.insert("comments", null, contentValues);
        long recordId = this.database.insert("comments", null, contentValues);

        Cursor cursor = this.database.query("comments",
                new String[]{"_id", "comment"},
                "_id = " + recordId,
                null,
                null,
                null,
                null);
        cursor.moveToFirst();

        Comment commentRecord = new Comment();
        commentRecord.setId(cursor.getLong(0));
        commentRecord.setComment(cursor.getString(1));

        cursor.close();

        return commentRecord;
    }

    public List<Comment> getAllComments() {
        Cursor cursor = database.query(
                "comments",
                new String[]{"_id", "comment"},
                null,
                null,
                null,
                null,
                null);

        cursor.moveToFirst();
        List<Comment> comments = new ArrayList<>();
        while (!cursor.isAfterLast()) {
            Comment commentRecord = new Comment();
            commentRecord.setId(cursor.getLong(0));
            commentRecord.setComment(cursor.getString(1));

            comments.add(commentRecord);

            cursor.moveToNext();

            cursor.close();


        }
        return comments;
    }
}
