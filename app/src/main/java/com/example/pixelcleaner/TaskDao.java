package com.example.pixelcleaner;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TaskDao {
    @Insert
    void insert(Task task);
    
    @Update
    void update(Task task);
    
    @Delete
    void delete(Task task);
    
    @Query("SELECT * FROM tasks ORDER BY createdAt DESC")
    List<Task> getAllTasks();
    
    @Query("SELECT * FROM tasks WHERE isCompleted = 0 ORDER BY createdAt DESC")
    List<Task> getActiveTasks();
    
    @Query("SELECT * FROM tasks WHERE isCompleted = 1 ORDER BY createdAt DESC")
    List<Task> getCompletedTasks();
    
    @Query("SELECT * FROM tasks WHERE id = :id")
    Task getTaskById(int id);
    
    @Query("DELETE FROM tasks")
    void deleteAllTasks();
}
