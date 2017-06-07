package com.academy.sda.databasetest.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.academy.sda.databasetest.R;
import com.academy.sda.databasetest.model.comments.Comment;
import com.academy.sda.databasetest.model.comments.database.CommentsDataSource;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommentDetailsActivity extends AppCompatActivity {

    @BindView(R.id.edit_text_new_comment)
    EditText commentText;

    private CommentsDataSource commentsDataSource;
    private Comment comment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logDebug("CommentDetailsActivity has started");
        setContentView(R.layout.activity_comment_add);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        commentsDataSource = new CommentsDataSource(this);
        commentsDataSource.open();

        logDebug("Initializing comment");
        comment = getIntent().getParcelableExtra(getString(R.string.comment_to_edit_key));
        if (comment != null) {
            commentText.setText(comment.getComment());
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        logDebug("Creating menu options");
//        logDebug(comment.toString());
        if (comment == null) {
            getMenuInflater().inflate(R.menu.menu_add_comments, menu);
            logDebug("I think comment is null");
        } else {
            getMenuInflater().inflate(R.menu.menu_edit_comments, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_edit_comments_add_item) {
            addNewComment();
        } else if (item.getItemId() == R.id.menu_edit_comments_update_item) {
            updateComment();
        }
        return true;
    }

    private void updateComment() {

        logDebug("Editing comment");
        Comment updatedComment = comment;
        updatedComment.setComment(commentText.getText().toString());
        commentsDataSource.updateComment(updatedComment);

        finishActivityWithOKResult();

    }

    public void addNewComment() {
        commentsDataSource.createComment(commentText.getText().toString());

        finishActivityWithOKResult();
    }

    private void finishActivityWithOKResult() {
        Intent intent = new Intent();
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        commentsDataSource.close();

        logDebug("CommentDetailsActivity has stopped");

    }

    private void logDebug(String string) {
        Log.e(getClass().getSimpleName(), string);

    }

    private void makeShortToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void logAndToast(String message) {
        logDebug(message);
        makeShortToast(message);
    }
}
