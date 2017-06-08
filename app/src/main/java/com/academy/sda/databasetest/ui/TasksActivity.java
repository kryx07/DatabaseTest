package com.academy.sda.databasetest.ui;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.academy.sda.databasetest.model.comments.Task;
import com.academy.sda.databasetest.model.comments.TaskAdapter;
import com.academy.sda.databasetest.model.comments.database.TasksDataSource;
import com.academy.sda.databasetest.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TasksActivity extends AppCompatActivity implements TaskAdapter.TaskClickListener {

    @BindView(R.id.comments_recycler)
    RecyclerView recyclerView;
    @BindView(R.id.activity_main_swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    private final int ADD_TASK = 87;
    private final int EDIT_TASK = 88;

    private TaskAdapter taskAdapter;
    private TasksDataSource tasksDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getTasks();
            }
        });

    }

    private void init() {

        taskAdapter = new TaskAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(taskAdapter);

        tasksDataSource = new TasksDataSource(this);
        tasksDataSource.open();

        //getTasks();

    }

    private void getTasks() {
        showSpinner();
        logAndToast("Getting all tasks");

        taskAdapter.setData(tasksDataSource.getAll());

        hideSpinner();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_comments, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_comments_item) {
            startCommentAddActivity(null);
        }
        return true;
    }

    private void startCommentAddActivity(@Nullable Task task) {
        Intent intent = new Intent(this, TaskDetailsActivity.class);
        if (task == null) {
            logAndToast("Adding new task");
            startActivityForResult(intent, ADD_TASK);
        } else {
            logAndToast("Editing task");
            intent.putExtra(getString(R.string.task_to_edit_key), task);
            startActivityForResult(intent, EDIT_TASK);
        }
    }

    @Override
    public void onTaskClick(Task task) {

        logDebug(task + " was clicked");

        startCommentAddActivity(task);
    }

    private void showSpinner() {
        swipeRefreshLayout.setRefreshing(true);
    }

    private void hideSpinner() {
        swipeRefreshLayout.setRefreshing(false);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();

        tasksDataSource.close();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Check which request we're responding to
        switch (requestCode) {
            case ADD_TASK:
                logDebug("ADD_TASK request was returned to " + getClass().getSimpleName());
                // Make sure the request was successful
                if (resultCode == RESULT_OK) {
                    // The user picked a contact.
                    // The Intent's data Uri identifies which contact was selected.

                    logDebug("ADD_TASK request successful");
                    makeShortToast("Task added");
                    getTasks();
                    // Do something with the contact here (bigger example below)
                }
            case EDIT_TASK:
                logDebug("EDIT_TASK request was returned to " + getClass().getSimpleName() + " with " + resultCode);
                logDebug("EDIT_TASK request successful");
                makeShortToast("Task edited");
                getTasks();
        }

    }
}
