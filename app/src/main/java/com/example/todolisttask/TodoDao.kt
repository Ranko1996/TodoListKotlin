package com.example.todolisttask

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {
    @Upsert()
    // Suspend run-a akciju u korutini odnosno suspendira bazu dok se ne zavrsi f-ja
    suspend fun upsertTodo(todo: Todo)

    @Delete
    suspend fun deleteTodo(todo: Todo)

    @Query("SELECT * FROM todo")
    fun getTodos(): Flow<List<Todo>>

    @Query("SELECT * FROM todo WHERE isFinished = 1 ORDER BY name ASC")
    fun getFinishedTodos(): Flow<List<Todo>>

    //    suspend fun updateTodoStatus(todoId: Int)
//    @Query("UPDATE todo SET isFinished = 0 WHERE id = :todoId")
    @Query("UPDATE todo SET isFinished = :isFinished WHERE id = :todoId")
    suspend fun updateTodoStatus(todoId: Int, isFinished: Boolean)

    @Query("SELECT * FROM todo WHERE isFinished = 0 ORDER BY name ASC")
    fun getUnfinishedTodos(): Flow<List<Todo>>
}