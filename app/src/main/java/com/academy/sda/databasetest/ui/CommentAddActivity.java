package com.academy.sda.databasetest.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import com.academy.sda.databasetest.R;
import com.academy.sda.databasetest.model.comments.database.CommentsDataSource;
import com.academy.sda.databasetest.utils.DatabaseHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wd42 on 06.06.17.
 */

public class CommentAddActivity extends AppCompatActivity {

    @BindView(R.id.edit_text_new_comment)
    EditText newCommentEditText;

    private CommentsDataSource commentsDataSource;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_add);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        commentsDataSource = new CommentsDataSource(new DatabaseHelper(this, "myDatabase.db", null, 1));
    }

    @OnClick(R.id.button_add_comment)
    public void addNewComment() {
        commentsDataSource.open();
        commentsDataSource.createComment(newCommentEditText.getText().toString());
        commentsDataSource.close();

        finish();


    }



}
