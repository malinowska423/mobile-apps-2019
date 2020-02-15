package com.example.pong

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.TextView

class GameView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    var ballX = 200f
    var ballY = 400f
    var dx = 5f
    var dy = 5f
    val ballSize = 50f
    var posTop = 0f
    var posBottom = 0f
    var pointsTop = 0
    var pointsBottom = 0
    lateinit var sharedPref  : SharedPreferences
    private var PREFS_NAME = "gameState"

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (canvas == null) return

        val red = Paint()
        red.setARGB(255,255,0,0)
        canvas.drawOval(RectF(ballX, ballY,ballX+ballSize,ballY+ballSize), red)
        val blue = Paint()
        blue.setARGB(255, 0, 0, 255)
        canvas.drawRect(RectF(posTop, 0f, posTop+(width/3f), 50f), blue)
        val green = Paint()

        green.setARGB(255,0,255,0)
        val playerBottom = RectF(posBottom, height-50f, posBottom+(width/3f), height+0f)
        canvas.drawRect(playerBottom, green)
        val black = Paint()
        black.setARGB(50,0,0,0)
        black.textSize = 200f
        black.strokeWidth = 20f
        canvas.drawText("$pointsTop:$pointsBottom", width/3f, height/2f, black)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.y < height/2f) {
            posTop = when {
                event.x > 5*width/6f -> 2 * width / 3f
                event.x < width/6f -> 0f
                else -> event.x - (width/6f)
            }
        } else {
            posBottom = when {
                event.x > 5*width/6f -> 2 * width / 3f
                event.x < width/6f -> 0f
                else -> event.x - (width/6f)
            }
        }
        postInvalidate()
        return true
    }

    fun gameOn() {
        ballX+=dx
        ballY+=dy

        if (ballX <= 0 || ballX+ballSize >= width) {
            dx = -dx
        }
        if (ballY <= 50f || ballY+ballSize >= height-50f) {
            dy = -dy
        }
        //goal to top
        if (ballY <= 50f && !(ballX >= posTop && ballX <= posTop + (width/3f))) {
            pointsBottom++
            saveData()
        }
        //goal to bottom
        if (ballY >= height-100f && !(ballX >= posBottom && ballX <= posBottom + (width/3f))) {
            pointsTop++
            saveData()
        }
        postInvalidate()
        if (pointsTop >= 5 || pointsBottom >= 5) {
            sharedPref = this.context.getSharedPreferences( PREFS_NAME, Context.MODE_PRIVATE)
            val editor : SharedPreferences.Editor = sharedPref.edit()
            editor.putBoolean("gameOn", false)
            editor!!.commit()
        }
    }

    fun getData() {
        sharedPref = this.context.getSharedPreferences( PREFS_NAME, Context.MODE_PRIVATE)
        ballX = sharedPref.getFloat("ballX", 200f)
        ballY = sharedPref.getFloat("ballY", 400f)
        pointsTop = sharedPref.getInt("pointsTop", 0)
        pointsBottom = sharedPref.getInt("pointsBottom", 0)
    }

    private fun saveData() {
        sharedPref = this.context.getSharedPreferences( PREFS_NAME, Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = sharedPref.edit()
//        editor.putFloat("ballX",ballX)
//        editor.putFloat("ballY",ballY)
        editor.putInt("pointsTop",pointsTop)
        editor.putInt("pointsBottom",pointsBottom)
        editor!!.commit()
    }
}