package com.example.list3_todo

import android.app.Activity
import android.app.ActivityManager
import android.arch.persistence.room.Room
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    val NEW_TASK_REQUEST = 4475
    val SETTINGS_REQUEST = 8711
    private var userTasks = ArrayList<ToDoTask>()
    private var sortMode = "custom"
    private var viewMode = "full view"
    var myAdapter: TaskListViewArrayAdapter? = null
    private lateinit var database: TaskDatabase
    private val TAG = "TODOTASK_"

    lateinit var mServiceIntent: Intent
    lateinit var mService: MyService

    private fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                Log.d("${TAG}service", "running")
                return true
            }
        }
        Log.d("${TAG}service", "not running")
        return false
    }

    override fun onDestroy() {
        stopService(mServiceIntent)
        Log.d("${TAG}_destroy", "done")
        super.onDestroy()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setViewMode()
        mService = MyService()
        mServiceIntent = Intent(this, mService.javaClass)

        if (!isMyServiceRunning(mService.javaClass)) {
            Log.d("${TAG}service", "starting")
            startService(mServiceIntent)
        }
        AsyncTask.execute {
            try {
                database = Room.databaseBuilder(this, TaskDatabase::class.java, "todotasks.db").build()
                Log.d("${TAG}database", "created")
            } catch (e: Exception) {
                Log.d("${TAG}database", e.message)
            }
        }
        setListViewContent()
        taskList.setOnItemLongClickListener { _, _, index, _ ->
            deleteTask(userTasks[index])
            true
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putCharSequence("view", viewMode)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        viewMode = savedInstanceState?.getCharSequence("view").toString()
        setViewMode()
        setListViewContent()
    }

    fun newTask(view: View) {
        val taskIntent = Intent(this, NewTaskActivity::class.java)
        startActivityForResult(taskIntent, NEW_TASK_REQUEST)
    }

    fun openSettings(view: View) {
        val settingsIntent = Intent(this, SettingsActivity::class.java)
        settingsIntent.putExtra("currentSettings", "$sortMode@#$viewMode")
        startActivityForResult(settingsIntent, SETTINGS_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == NEW_TASK_REQUEST) {
            Log.d("${TAG}result", "back from new task")
            if (resultCode == Activity.RESULT_OK) {
                Log.d("${TAG}result", "all good")
                setListViewContent()
//                myAdapter?.notifyDataSetChanged()
            }
        }
        if (requestCode == SETTINGS_REQUEST) {
            Log.d("${TAG}result", "back from settings")
            if (resultCode == Activity.RESULT_OK) {
                Log.d("${TAG}result", "all good")
                val settings = data?.getStringExtra("settings").toString().split("@#").toTypedArray()
                sortMode = settings[0]
                sortTasks()
                myAdapter?.notifyDataSetChanged()
                if (viewMode != settings[1]) {
                    viewMode = settings[1]
                    setViewMode()
                }
            }
        }

    }

    private fun sortTasks() {
        when (sortMode) {
            "date" -> userTasks.sortedBy { toDoTask -> toDoTask.date }
            "task type" -> userTasks.sortedBy { toDoTask -> toDoTask.type }
            "priority" -> userTasks.sortedBy { toDoTask -> toDoTask.priority }
            else -> userTasks.sortedBy { toDoTask -> toDoTask.id }
        }
        Log.d("${TAG}sort", "list sorted")
    }

    private fun setViewMode() {
        Log.d("${TAG}viewMode", "viewMode start")
        val res: Int = if (viewMode == "title only") {
            R.layout.task_layout_title_only
        } else {
            R.layout.task_layout_full_view
        }
        myAdapter = TaskListViewArrayAdapter(this, res, userTasks)
        taskList.adapter = myAdapter
        Log.d("${TAG}viewMode", "viewMode end")
    }

    private fun setListViewContent() {
        Log.d("${TAG}update", "update start")
        AsyncTask.execute {
            Log.d("${TAG}update", "update in progress")
            userTasks.removeAll(userTasks)
            userTasks.addAll(database.toDoTaskDao().getAll())
            Log.d("${TAG}update", "update end | ${userTasks.size} tasks")
            sortTasks()
            runOnUiThread { refresh() }
            Log.d("${TAG}update", "update finished")
        }
    }

    private fun deleteTask(task: ToDoTask) {
        Log.d("${TAG}delete", "delete start")
        AsyncTask.execute {
            Log.d("${TAG}delete", "delete in progress")
            database.toDoTaskDao().delete(task)
            Log.d("${TAG}delete", "delete end")
            setListViewContent()
            Log.d("${TAG}delete", "delete finished")
        }
    }

    private fun refresh() {
        Log.d("${TAG}refresh", "notify")
        myAdapter?.notifyDataSetChanged()
        Log.d("${TAG}refresh", "done")
    }
}
