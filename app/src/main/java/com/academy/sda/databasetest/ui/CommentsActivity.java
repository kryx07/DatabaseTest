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

import com.academy.sda.databasetest.model.comments.Comment;
import com.academy.sda.databasetest.model.comments.CommentsAdapter;
import com.academy.sda.databasetest.model.comments.database.CommentsDataSource;
import com.academy.sda.databasetest.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommentsActivity extends AppCompatActivity implements CommentsAdapter.CommentClickListener {

    @BindView(R.id.comments_recycler)
    RecyclerView recyclerView;
    @BindView(R.id.activity_main_swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    private final int ADD_COMMENT = 87;
    private final int EDIT_COMMENT = 88;
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

        commentsDataSource = new CommentsDataSource(this);
        commentsDataSource.open();

        getComments();

    }

    private void getComments() {
        showSpinner();
        logAndToast("Getting all comments");

        commentsAdapter.setData(commentsDataSource.getAllComments());

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

    private void startCommentAddActivity(@Nullable Comment comment) {
        Intent intent = new Intent(this, CommentDetailsActivity.class);
        if (comment == null) {
            logAndToast("Adding new comment");
            startActivityForResult(intent, ADD_COMMENT);
        } else {
            logAndToast("Editing comment");
            intent.putExtra(getString(R.string.comment_to_edit_key), comment);
            startActivityForResult(intent, EDIT_COMMENT);
        }
    }

    @Override
    public void onCommentClick(Comment comment) {

        logDebug(comment + " was clicked");

        startCommentAddActivity(comment);
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

        commentsDataSource.close();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Check which request we're responding to
        switch (requestCode) {
            case ADD_COMMENT:
                logDebug("ADD_COMMENT request was returned to " + getClass().getSimpleName());
                // Make sure the request was successful
                if (resultCode == RESULT_OK) {
                    // The user picked a contact.
                    // The Intent's data Uri identifies which contact was selected.

                    logDebug("ADD_COMMENT request successful");
                    makeShortToast("Comment added");
                    getComments();
                    // Do something with the contact here (bigger example below)
                }
            case EDIT_COMMENT:
                logDebug("EDIT_COMMENT request was returned to " + getClass().getSimpleName() + " with " + resultCode);
                logDebug("EDIT_COMMENT request successful");
                makeShortToast("Comment edited");
                getComments();
        }

    }
}
