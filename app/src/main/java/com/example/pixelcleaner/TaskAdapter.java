package com.example.pixelcleaner;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private List<Task> tasks;
    private OnTaskClickListener listener;
    
    public interface OnTaskClickListener {
        void onDelete(Task task);
        void onToggleComplete(Task task);
        void onEdit(Task task);
    }
    
    public TaskAdapter(List<Task> tasks, OnTaskClickListener listener) {
        this.tasks = tasks;
        this.listener = listener;
    }
    
    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.bind(task, listener);
    }
    
    @Override
    public int getItemCount() {
        return tasks.size();
    }
    
    public void updateTasks(List<Task> newTasks) {
        this.tasks = newTasks;
        notifyDataSetChanged();
    }
    
    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        private TextView titleView;
        private TextView descriptionView;
        private TextView dateView;
        private CheckBox completeCheckbox;
        private ImageButton deleteButton;
        private ImageButton editButton;
        
        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            titleView = itemView.findViewById(R.id.task_title);
            descriptionView = itemView.findViewById(R.id.task_description);
            dateView = itemView.findViewById(R.id.task_date);
            completeCheckbox = itemView.findViewById(R.id.task_complete_checkbox);
            deleteButton = itemView.findViewById(R.id.task_delete_button);
            editButton = itemView.findViewById(R.id.task_edit_button);
        }
        
        public void bind(Task task, OnTaskClickListener listener) {
            titleView.setText(task.title);
            descriptionView.setText(task.description);
            completeCheckbox.setChecked(task.isCompleted);
            
            // Format date
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault());
            dateView.setText(sdf.format(new Date(task.createdAt)));
            
            // Set text decoration for completed tasks
            if (task.isCompleted) {
                titleView.setAlpha(0.5f);
                descriptionView.setAlpha(0.5f);
            } else {
                titleView.setAlpha(1.0f);
                descriptionView.setAlpha(1.0f);
            }
            
            completeCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                task.isCompleted = isChecked;
                if (listener != null) {
                    listener.onToggleComplete(task);
                }
            });
            
            deleteButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDelete(task);
                }
            });
            
            editButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onEdit(task);
                }
            });
        }
    }
}
