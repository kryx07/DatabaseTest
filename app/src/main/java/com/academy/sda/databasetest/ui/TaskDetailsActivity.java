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
import com.academy.sda.databasetest.model.comments.Task;
import com.academy.sda.databasetest.model.comments.database.TasksDataSource;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TaskDetailsActivity extends AppCompatActivity {

    @BindView(R.id.edit_text_new_comment)
    EditText commentText;

    private TasksDataSource tasksDataSource;
    private Task task;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logDebug("TaskDetailsActivity has started");
        setContentView(R.layout.activity_comment_add);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        tasksDataSource = new TasksDataSource(this);
        tasksDataSource.open();

        logDebug("Initializing task");
        task = getIntent().getParcelableExtra(getString(R.string.task_to_edit_key));
        if (task != null) {
            commentText.setText(task.getDescription());
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        logDebug("Creating menu options");
//        logDebug(task.toString());
        if (task == null) {
            getMenuInflater().inflate(R.menu.menu_add_comments, menu);
            logDebug("I think task is null");
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
        } else if (item.getItemId() == R.id.menu_edit_comments_delete_item) {
            deleteComment();
        }
        return true;
    }

    private void deleteComment() {
        logDebug("Deleting task");
        tasksDataSource.delete(task);
        finishActivityWithOKResult();
    }

    private void updateComment() {

        logDebug("Editing task");
        Task updatedTask = task;
        updatedTask.setDescription(commentText.getText().toString());
        tasksDataSource.update(updatedTask);

        finishActivityWithOKResult();

    }

    public void addNewComment() {
        //tasksDataSource.create(commentText.getText().toString());

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

        tasksDataSource.close();

        logDebug("TaskDetailsActivity has stopped");

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
