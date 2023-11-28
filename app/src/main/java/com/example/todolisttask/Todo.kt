package com.example.todolisttask

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Todo(
    val name: String,
    val description: String,
    val isFinished: Boolean,
    
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)
