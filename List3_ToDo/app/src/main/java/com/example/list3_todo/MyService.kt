package com.example.list3_todo

import android.annotation.SuppressLint
import android.app.*
import android.arch.persistence.room.Room
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.IBinder
import android.util.Log

@SuppressLint("Registered")
class MyService : Service() {

    private val TAG = "TODOTASK_service"
    var end = false

    override fun onCreate() {
        Log.d("TODOTASK", "OnStartCommand")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "OnStartCommand")

        if (intent != null) {
            var value = intent.getStringExtra("key")
            Log.d(TAG, "OnStartCommand: $value")
        }

        val db = Room.databaseBuilder(this, TaskDatabase::class.java, "todotasks.db").build()
        var tasks: List<ToDoTask> = listOf(ToDoTask("", "", "", "", true))
        AsyncTask.execute {
            tasks = db.toDoTaskDao().getAll()
        }

        Thread {

            for (i in 1..100) {
                if (end) {
                    break
                }
                AsyncTask.execute {
                    tasks = db.toDoTaskDao().getAll()
                }
                Log.d(TAG, "Loop: $i")
                Thread.sleep(5000)

                for (task in tasks) {
                    Log.i(TAG, task.name)
                    if (!task.notified) {
                        Log.i(TAG, "checking date")

                        if (task.date == "today") {
                            Log.i(TAG, "notify")
                            makeNotification(task)
                            task.notified = true
                            AsyncTask.execute {
                                db.toDoTaskDao().updateTasks(task)
                            }
                        }
                    }

                }
            }

        }.start()


        return START_STICKY
    }

    private val CHANNEL_ID = "myChannel"

    private fun makeNotification(task: ToDoTask) {

        val channel = NotificationChannel(CHANNEL_ID, CHANNEL_ID, NotificationManager.IMPORTANCE_DEFAULT)
        val manager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)

        val icon = R.drawable.logo
        val appName = resources.getString(R.string.app_name)

        val builder = Notification.Builder(this, CHANNEL_ID)
            .setContentTitle("$appName reminder")
            .setContentText("${task.name} - it's today")
            .setAutoCancel(true)
            .setSmallIcon(icon)

        val notificationIntent = Intent(this, MainActivity::class.java)
        val pending = PendingIntent.getActivity(this, 0, notificationIntent, 0)
        builder.setContentIntent(pending)

        val notification = builder.build()
        manager.notify(1234, notification)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }


    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "OnDestroy")
        end = true
        val broadcastIntent = Intent(this, Restarter::class.java)
        sendBroadcast(broadcastIntent)
    }

}
