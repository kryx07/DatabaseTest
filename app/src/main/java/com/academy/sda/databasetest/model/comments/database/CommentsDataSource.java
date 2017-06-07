package com.academy.sda.databasetest.model.comments.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.academy.sda.databasetest.model.comments.Comment;
import com.academy.sda.databasetest.utils.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;


public class CommentsDataSource {

    private DatabaseHelper databaseHelper;
    private SQLiteDatabase database;

    public CommentsDataSource(Context context) {
        this.databaseHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        this.database = databaseHelper.getWritableDatabase();
    }

    public long updateComment(Comment comment) {
        ContentValues contentValues = new ContentValues();


        contentValues.put("comment", comment.getComment());
        long recordId = this.database.update("comments", contentValues, "_id=" + comment.getId(), null);

        return recordId;

    }

    public void close() {
        this.databaseHelper.close();
    }

    public Comment createComment(String comment) {
        ContentValues contentValues = new ContentValues();


        contentValues.put("comment", comment);


//        this.database.insert("comments", null, contentValues);
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

        logDebug("Database takes in: " + commentRecord);

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


        }
        cursor.close();

        logDebug("Database returns: " + comments);

        return comments;
    }

    private void logDebug(String string) {
        Log.e(getClass().getSimpleName(), string);

    }
}
