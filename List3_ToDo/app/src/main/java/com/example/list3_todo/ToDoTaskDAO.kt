package com.example.list3_todo

import android.arch.persistence.room.*

@Dao
interface ToDoTaskDAO {

    @Query("select * from Tasks")
    fun getAll() : List<ToDoTask>

    @Insert
    fun insert(vararg task : ToDoTask)

    @Delete
    fun delete(vararg task: ToDoTask)

    @Update
    fun updateTasks(vararg tasks: ToDoTask)
}