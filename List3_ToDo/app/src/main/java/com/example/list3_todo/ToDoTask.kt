package com.example.list3_todo

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "Tasks")
data class ToDoTask (
    @ColumnInfo(name = "name") var name : String,
    @ColumnInfo(name = "date") var date : String,
    @ColumnInfo(name = "priority") var priority : String,
    @ColumnInfo(name = "type") var type : String,
    @ColumnInfo(name = "notified") var notified: Boolean,
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true) var id : Long = 0
)