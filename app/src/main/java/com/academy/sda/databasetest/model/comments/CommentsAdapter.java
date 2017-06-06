package com.academy.sda.databasetest.model.comments;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.academy.sda.databasetest.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ClientsHolder> {


    public interface CommentClickListener {
        /**
         * Called when an item is clicked.
         *
         * @param comment Client to be passed .
         */
        void onCommentClick(Comment comment);
    }

    public CommentsAdapter(CommentClickListener commentClickListener) {
        this.commentClickListener = commentClickListener;
    }

    private CommentClickListener commentClickListener;

    private List<Comment> commentList = new ArrayList<>();

    public void setData(List<Comment> commentList) {
        this.commentList.clear();
        this.commentList.addAll(commentList);
        notifyDataSetChanged();

    }

    @Override
    public ClientsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_comment, parent, false);

        return new ClientsHolder(view);
    }

    @Override
    public void onBindViewHolder(final ClientsHolder holder, final int position) {
        holder.setComment(commentList.get(position));
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    class ClientsHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.comment_text)
        TextView clientName;

        private Comment comment;

        public ClientsHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    commentClickListener.onCommentClick(comment);
                }
            });
        }


        public void setComment(Comment comment) {
            this.comment = comment;
            clientName.setText(comment.getComment());
        }

    }


}