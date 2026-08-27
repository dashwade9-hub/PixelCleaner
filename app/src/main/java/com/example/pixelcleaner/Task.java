package com.example.pixelcleaner;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tasks")
public class Task {
    @PrimaryKey(autoGenerate = true)
    public int id;
    
    public String title;
    public String description;
    public boolean isCompleted;
    public long createdAt;
    public long updatedAt;
    
    public Task(String title, String description) {
        this.title = title;
        this.description = description;
        this.isCompleted = false;
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
    }
    
    public Task() {
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
    }
}
