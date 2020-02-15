package com.example.list3_todo

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

@Database( entities = [(ToDoTask::class)], version = 1)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun toDoTaskDao() : ToDoTaskDAO
}