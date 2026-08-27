package com.example.pixelcleaner;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private Button addTaskButton;
    private RadioGroup filterGroup;
    private TaskAdapter taskAdapter;
    private TaskDatabase database;
    private List<Task> currentTasks;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        database = TaskDatabase.getInstance(this);
        
        recyclerView = findViewById(R.id.tasks_recycler_view);
        addTaskButton = findViewById(R.id.add_task_button);
        filterGroup = findViewById(R.id.filter_group);
        
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        
        loadAllTasks();
        
        addTaskButton.setOnClickListener(v -> showAddTaskDialog());
        
        filterGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.filter_all) {
                loadAllTasks();
            } else if (checkedId == R.id.filter_active) {
                loadActiveTasks();
            } else if (checkedId == R.id.filter_completed) {
                loadCompletedTasks();
            }
        });
    }
    
    private void loadAllTasks() {
        currentTasks = database.taskDao().getAllTasks();
        updateAdapter();
    }
    
    private void loadActiveTasks() {
        currentTasks = database.taskDao().getActiveTasks();
        updateAdapter();
    }
    
    private void loadCompletedTasks() {
        currentTasks = database.taskDao().getCompletedTasks();
        updateAdapter();
    }
    
    private void updateAdapter() {
        if (taskAdapter == null) {
            taskAdapter = new TaskAdapter(currentTasks, new TaskAdapter.OnTaskClickListener() {
                @Override
                public void onDelete(Task task) {
                    deleteTask(task);
                }
                
                @Override
                public void onToggleComplete(Task task) {
                    updateTask(task);
                }
                
                @Override
                public void onEdit(Task task) {
                    showEditTaskDialog(task);
                }
            });
            recyclerView.setAdapter(taskAdapter);
        } else {
            taskAdapter.updateTasks(currentTasks);
        }
    }
    
    private void showAddTaskDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add New Task");
        
        final EditText titleInput = new EditText(this);
        titleInput.setHint("Task Title");
        final EditText descriptionInput = new EditText(this);
        descriptionInput.setHint("Task Description");
        
        builder.setView(titleInput);
        builder.setView(descriptionInput);
        
        builder.setPositiveButton("Add", (dialog, which) -> {
            String title = titleInput.getText().toString().trim();
            String description = descriptionInput.getText().toString().trim();
            
            if (title.isEmpty()) {
                Toast.makeText(MainActivity.this, "Please enter a task title", Toast.LENGTH_SHORT).show();
                return;
            }
            
            Task task = new Task(title, description);
            database.taskDao().insert(task);
            loadAllTasks();
            Toast.makeText(MainActivity.this, "Task added", Toast.LENGTH_SHORT).show();
        });
        
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }
    
    private void showEditTaskDialog(Task task) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Task");
        
        final EditText titleInput = new EditText(this);
        titleInput.setText(task.title);
        final EditText descriptionInput = new EditText(this);
        descriptionInput.setText(task.description);
        
        builder.setView(titleInput);
        builder.setView(descriptionInput);
        
        builder.setPositiveButton("Update", (dialog, which) -> {
            String title = titleInput.getText().toString().trim();
            String description = descriptionInput.getText().toString().trim();
            
            if (title.isEmpty()) {
                Toast.makeText(MainActivity.this, "Please enter a task title", Toast.LENGTH_SHORT).show();
                return;
            }
            
            task.title = title;
            task.description = description;
            task.updatedAt = System.currentTimeMillis();
            database.taskDao().update(task);
            
            int filterId = filterGroup.getCheckedRadioButtonId();
            if (filterId == R.id.filter_all) {
                loadAllTasks();
            } else if (filterId == R.id.filter_active) {
                loadActiveTasks();
            } else if (filterId == R.id.filter_completed) {
                loadCompletedTasks();
            }
            
            Toast.makeText(MainActivity.this, "Task updated", Toast.LENGTH_SHORT).show();
        });
        
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }
    
    private void updateTask(Task task) {
        task.updatedAt = System.currentTimeMillis();
        database.taskDao().update(task);
        
        int filterId = filterGroup.getCheckedRadioButtonId();
        if (filterId == R.id.filter_all) {
            loadAllTasks();
        } else if (filterId == R.id.filter_active) {
            loadActiveTasks();
        } else if (filterId == R.id.filter_completed) {
            loadCompletedTasks();
        }
    }
    
    private void deleteTask(Task task) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Task")
                .setMessage("Are you sure you want to delete this task?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    database.taskDao().delete(task);
                    
                    int filterId = filterGroup.getCheckedRadioButtonId();
                    if (filterId == R.id.filter_all) {
                        loadAllTasks();
                    } else if (filterId == R.id.filter_active) {
                        loadActiveTasks();
                    } else if (filterId == R.id.filter_completed) {
                        loadCompletedTasks();
                    }
                    
                    Toast.makeText(MainActivity.this, "Task deleted", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
