package com.example.list1_maze

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import java.util.*

class MainActivity : AppCompatActivity() {

    private var N = 10
    private var points = 0
    private var wayThrough = ""
    private var maze = Array(N) { i -> i }
    private var index = 0
    private var goodWay = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        createMaze(N)
    }

    private fun wayCode(way: Int): String {
        var code = ""
        if (way == 1) {
            code = "UP "
        }
        else if (way == 0){
            code = "RIGHT "
        }
        return code
    }

    private fun createMaze(n: Int) {
        maze = Array(n) {(Random().nextInt(100)%2)}
        goodWay = ""
        maze.forEach { i -> goodWay += wayCode(i) }
    }

    private fun goFurther(way: Int) {

        if (way == maze[index]) {
            points++

            wayThrough += wayCode(way)
            findViewById<TextView>(R.id.maze).text = "$wayThrough"
            index++
            if (index == maze.size) {
                findViewById<TextView>(R.id.maze).text = "$wayThrough\nFINISHED!"
                findViewById<Button>(R.id.buttonUp).isClickable = false
                findViewById<Button>(R.id.buttonRight).isClickable = false
                findViewById<Button>(R.id.buttonHint).isClickable = false
                Toast.makeText(this, "Congratulations!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Good!", Toast.LENGTH_SHORT).show()
            }
        } else {
            points--
            Toast.makeText(this, "Wrong!", Toast.LENGTH_SHORT).show()
        }

        findViewById<TextView>(R.id.points).text = "Points: $points"
    }

    fun clickUp(view: View) {
        goFurther(1)
    }

    fun clickRight(view: View) {
        goFurther(0)
    }

    fun newMaze(view: View) {
        createMaze(N)
        points = 0
        index = 0
        wayThrough = ""
        findViewById<Button>(R.id.buttonUp).isClickable = true
        findViewById<Button>(R.id.buttonRight).isClickable = true
        findViewById<Button>(R.id.buttonHint).isClickable = true
        findViewById<TextView>(R.id.points).text = "Points: $points"
        findViewById<TextView>(R.id.maze).text = "$wayThrough"

    }

    fun hint(view: View) {
        points--
        Toast.makeText(this, goodWay, Toast.LENGTH_LONG).show()
        findViewById<TextView>(R.id.points).text = "Points: $points"
    }

}
