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

    private final String COMMENT_TABLE_NAME = "comments";
    private final String COMMENT_COLUMN = "comment";
    private final String ID_COLUMN = "_id";


    public CommentsDataSource(Context context) {
        this.databaseHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        this.database = databaseHelper.getWritableDatabase();
    }

    public long updateComment(Comment comment) {
        ContentValues contentValues = new ContentValues();


        contentValues.put(COMMENT_COLUMN, comment.getComment());
        long recordId = this.database.update(COMMENT_TABLE_NAME, contentValues, ID_COLUMN + "=" + comment.getId(), null);

        return recordId;

    }

    public void delete(Comment comment) {
        database.delete(COMMENT_TABLE_NAME, ID_COLUMN + "=" + comment.getId(), null);

    }

    public void close() {
        this.databaseHelper.close();
    }

    public Comment createComment(String comment) {
        ContentValues contentValues = new ContentValues();


        contentValues.put(COMMENT_COLUMN, comment);


//        this.database.insert("comments", null, contentValues);
        long recordId = this.database.insert(COMMENT_TABLE_NAME, null, contentValues);


        Cursor cursor = this.database.query(COMMENT_TABLE_NAME,
                new String[]{ID_COLUMN, COMMENT_COLUMN},
                ID_COLUMN + "= " + recordId,
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
                COMMENT_TABLE_NAME,
                new String[]{ID_COLUMN, COMMENT_COLUMN},
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
