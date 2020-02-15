package com.example.list3_todo

import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : Activity() {
    var sortMode:String = ""
    var viewMode:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        val settings = intent.getStringExtra("currentSettings").toString().split("@#").toTypedArray()
        sortMode = settings[0]
        viewMode = settings[1]
        val sortId:Int = when (sortMode) {
            "date" -> R.id.dateSort
            "priority" -> R.id.prioritySort
            "task type" -> R.id.typeSort
            else -> R.id.customSort
        }
        sortGroup.check(sortId)
        val viewId:Int = when (viewMode) {
            "title only" -> R.id.titleOnly
            else -> R.id.fullView
        }
        layoutGroup.check(viewId)
    }

    fun save(view: View) {
        val settingsIntent = Intent()
        sortMode = findViewById<RadioButton>(sortGroup.checkedRadioButtonId).text.toString()
        viewMode = findViewById<RadioButton>(layoutGroup.checkedRadioButtonId).text.toString()
        settingsIntent.putExtra("settings", "$sortMode@#$viewMode")
        setResult(Activity.RESULT_OK, settingsIntent)
        finish()
    }
}
