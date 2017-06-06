package com.academy.sda.databasetest.ui;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.academy.sda.databasetest.model.comments.Comment;
import com.academy.sda.databasetest.model.comments.CommentsAdapter;
import com.academy.sda.databasetest.model.comments.database.CommentsDataSource;
import com.academy.sda.databasetest.utils.DatabaseHelper;
import com.academy.sda.databasetest.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommentsActivity extends AppCompatActivity implements CommentsAdapter.CommentClickListener {

    @BindView(R.id.comments_recycler)
    RecyclerView recyclerView;
    @BindView(R.id.activity_main_swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    private CommentsAdapter commentsAdapter;
    private CommentsDataSource commentsDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getComments();
            }
        });

    }

    private void init() {

        commentsAdapter = new CommentsAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(commentsAdapter);

        commentsDataSource = new CommentsDataSource(new DatabaseHelper(this, "myDatabase.db", null, 1));


        getComments();

    }

    private void getComments() {
        showSpinner();
        logAndToast("Getting all comments");
        commentsDataSource.open();

        commentsDataSource.getAllComments();
        commentsDataSource.close();

        hideSpinner();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_comments, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_add_comment) {
            makeShortToast("Adding comment");
            startCommentAddActivity();
        }
        return true;
    }

    private void startCommentAddActivity() {
        Intent intent = new Intent(getApplicationContext(), CommentAddActivity.class);
        startActivity(intent);
    }

    @Override
    public void onCommentClick(Comment comment) {
        logDebug(comment + " was clicked");
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
}
