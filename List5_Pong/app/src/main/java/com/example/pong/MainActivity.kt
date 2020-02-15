package com.example.pong

import android.content.Context
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var sharedPref  : SharedPreferences
    private var PREFS_NAME = "gameState"
    private var isGameOn = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        sharedPref = getSharedPreferences( PREFS_NAME, Context.MODE_PRIVATE)
        isGameOn = sharedPref.getBoolean("gameOn", false)
    }

    fun startGame(view: View) {
        sharedPref = getSharedPreferences( PREFS_NAME, Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = sharedPref.edit()
        editor.putBoolean("gameOn", true)
        editor.putFloat("ballX",200f)
        editor.putFloat("ballY",400f)
        editor.putInt("pointsTop",0)
        editor.putInt("pointsBottom",0)
        editor!!.commit()
        game.getData()
        Thread {
            do {
                Thread.sleep(5)
                game.gameOn()
                isGameOn = sharedPref.getBoolean("gameOn", false)
            }while(isGameOn)
        }.start()
    }
}
