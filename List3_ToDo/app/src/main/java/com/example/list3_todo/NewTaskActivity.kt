package com.example.list3_todo

import android.app.Activity
import android.arch.persistence.room.Room
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.Spinner
import kotlinx.android.synthetic.main.activity_new_task.*
import java.lang.Exception
import java.text.DateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class NewTaskActivity : Activity() {

    private lateinit var database: TaskDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_task)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        ArrayAdapter.createFromResource(
            this,
            R.array.dateArray,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            taskDateDropdown.adapter = adapter
        }
        AsyncTask.execute {
            try {
                database = Room.databaseBuilder(this, TaskDatabase::class.java, "todotasks.db").build()
            } catch (e: Exception) {
                Log.i("TODOTASK_database_newTask", e.message)
            }
        }
    }

    fun save(view: View) {
        if (allDataOK()) {
            val priority: RadioButton = findViewById(taskPriority.checkedRadioButtonId)
            val type: RadioButton = findViewById(taskType.checkedRadioButtonId)
            val task = ToDoTask(
                taskName.text.toString(),
                taskDateDropdown.selectedItem.toString(),
                priority.text.toString(),
                type.text.toString(),
                false
            )
            AsyncTask.execute {
                database.toDoTaskDao().insert(task)
            }
            val savingIntent = Intent()
            setResult(RESULT_OK, savingIntent)
            finish()
        }
    }

    private fun allDataOK(): Boolean {
        errorMessage.visibility = View.INVISIBLE
        return if (taskName.text.isEmpty()) {
            errorMessage.visibility = View.VISIBLE
            false
        } else {
            true
        }
    }
}
