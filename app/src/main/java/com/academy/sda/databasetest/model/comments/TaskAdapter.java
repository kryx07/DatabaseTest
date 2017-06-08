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

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskHolder> {


    public interface TaskClickListener {
        /**
         * Called when an item is clicked.
         *
         * @param task to be passed .
         */
        void onTaskClick(Task task);
    }

    public TaskAdapter(TaskClickListener taskClickListener) {
        this.taskClickListener = taskClickListener;
    }

    private TaskClickListener taskClickListener;

    private List<Task> taskList = new ArrayList<>();

    public void setData(List<Task> taskList) {
        this.taskList.clear();
        this.taskList.addAll(taskList);
        notifyDataSetChanged();

    }

    @Override
    public TaskHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_task, parent, false);

        return new TaskHolder(view);
    }

    @Override
    public void onBindViewHolder(final TaskHolder holder, final int position) {
        holder.setTask(taskList.get(position));
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    class TaskHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.task_description)
        TextView taskDescription;
        @BindView(R.id.task_category)
        TextView taskCategory;
        @BindView(R.id.task_date)
        TextView taskDate;

        private Task task;

        public TaskHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    taskClickListener.onTaskClick(task);
                }
            });
        }


        public void setTask(Task task) {
            this.task = task;
            taskDescription.setText(task.getDescription());
        }

    }


}