package com.example.list2_tictactoe

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TableRow
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    var player = ' '
    var moves = 0
    var gameOver = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setBoardDisabled(true, true)
    }

    fun newGame(view: View) {
        Log.d("am2019", "New game")
        player = ' '
        buttonO.isEnabled = true
        buttonX.isEnabled = true
        moves = 0
        gameOver = false
        setBoardDisabled(true, true)
    }

    fun choosePlayer(view: View) {
        player = if (view == buttonO) {
            'O'
        } else {
            'X'
        }
        Log.d("am2019", "Chosen player: $player")
        buttonX.isEnabled = false
        buttonO.isEnabled = false
        setBoardDisabled(false, false)
    }

    private fun setBoardDisabled(disabled : Boolean, clear: Boolean) {
        for (row in 0 until board.childCount) {
            for (col in 0..4) {
                val btn = getButtonAt(row, col)
                btn.isClickable = !disabled
                if (clear) {
                    btn.text = null
                }
            }
        }
    }


    private fun haveWon() : Boolean {
        var cols = Array(5) {0}
        var rows = Array(5) {0}
        var diagonals = Array(2) {0}
        for (i in 0 until board.childCount) {
                for (x in 0..4) {
                    val btn = getButtonAt(i,x)
                    val field = btn.text
                    if (field == player.toString()) {
                        if (rows[i] >= 0) {
                            rows[i]++
                            if (rows[i] == 5) {
                                return true
                            }
                        }
                        if (cols[x] >= 0) {
                            cols[x]++
                            if (cols[x] == 5) {
                                return true
                            }
                        }
                        if (i == x) {
                            if (diagonals[0] >= 0) {
                                diagonals[0]++
                                if (diagonals[0] == 5) {
                                    return true
                                }
                            }
                        }
                        if (i == 4-x) {
                            if (diagonals[1] >= 0) {
                                diagonals[1]++
                                if (diagonals[1] == 5) {
                                    return true
                                }
                            }
                        }
                    }
                }
        }

        Log.d("am2019", "rows: ${printArray(rows)} | cols: ${printArray(cols)} | dia: ${printArray(diagonals)}")

        return false
    }

    private fun printArray(arr: Array<Int>) : String {
        var output:String = "["
        for (i in 0 until arr.size) {
            output += "${arr[i]} "
        }
        return "$output]"
    }



    fun pickMove(view: View) {
        saveMove(view)
        if (! gameOver) {
            botMove()
        }
    }

    private fun saveMove(view: View) {
        moves++
        val id = view.id
        Log.d("am2019", "Player $player picked button $id")
        val btn = view as Button
        btn.text = player.toString()
        btn.isClickable = false
        when {
            haveWon() -> {
                Toast.makeText(this, "Congratulations!\nPlayer $player wins!", Toast.LENGTH_LONG).show()
                setBoardDisabled(true, false)
                gameOver = true
            }
            moves == 25 -> {
                Toast.makeText(this, "Tie. Game over!", Toast.LENGTH_SHORT).show()
                gameOver = true
            }
            else -> player = if (player == 'X') {
                'O'
            } else {
                'X'
            }
        }
    }

    private fun botMove() {
        var picked = false
        if (moves < 20) {
            while (!picked) {
                val r = Random()
                val col = r.nextInt(5)
                val row = r.nextInt(5)
                val btn = getButtonAt(row, col)
                if (btn.text.isEmpty()) {
                    Log.d("am2019", "(Bot) Player $player picked [$row, $col]")
                    saveMove(btn)
                    picked = true
                }
            }
        } else {
            for (i in 0..4) {
                for (j in 0..4) {
                    val btn = getButtonAt(i,j)
                    if (btn.text.isEmpty()) {
                        Log.d("am2019", "(Bot) Player $player picked [$i, $j]")
                        saveMove(btn)
                        return
                    }
                }
            }
        }


    }

    private fun getButtonAt(row: Int, col:Int) : Button {
        return ((board.getChildAt(row) as TableRow).getChildAt(0) as LinearLayout).getChildAt(col) as Button
    }
}
