package com.example.list2_hangingman

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    var currentWord = " "
    var alreadyGuessed:String = ""
    var mistakesCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        newGame()
    }

    fun submit(view: View) {
        view.isClickable = false
        input.isEnabled = false
        val inputLetter = input.text.toString()
        input.text = null
        input.text.clear()
        view.isClickable = true
        input.isEnabled = true
        if (inputLetter.length != 1) {
            Toast.makeText(this, "Put exactly one letter!", Toast.LENGTH_SHORT).show()
        } else {
            val letter:Char = inputLetter[0]
            when (letter) {
                in alreadyGuessed -> Toast.makeText(this, "You have tried this letter before", Toast.LENGTH_SHORT).show()
                in currentWord -> goodGuess(letter)
                else -> badGuess(letter)
            }
        }
    }

    private fun badGuess(letter: Char) {
        mistakesCount++
        addWrongLetter(letter)
        alreadyGuessed += letter
        when (mistakesCount) {
            1 -> hanging_man.setImageResource(R.drawable.hanging_man_02)
            2 -> hanging_man.setImageResource(R.drawable.hanging_man_03)
            3 -> hanging_man.setImageResource(R.drawable.hanging_man_04)
            4 -> hanging_man.setImageResource(R.drawable.hanging_man_05)
            5 -> hanging_man.setImageResource(R.drawable.hanging_man_06)
            6 -> hanging_man.setImageResource(R.drawable.hanging_man_07)
            7 -> hanging_man.setImageResource(R.drawable.hanging_man_08)
            else -> hanging_man.setImageResource(R.drawable.hanging_man_01)
        }
        if (mistakesCount == 7) {
            gameOver(false)
        }

    }

    private fun gameOver(hasWon: Boolean) {
        submit_btn.isClickable = false
        input.isEnabled = false
        when (hasWon) {
            true -> Toast.makeText(this, "Congratulations, you won!", Toast.LENGTH_SHORT).show()
            false -> {
                Toast.makeText(this, "Sorry, you lost", Toast.LENGTH_SHORT).show()
                answer.text = currentWord
            }
        }

    }

    private fun goodGuess(letter: Char) {
        alreadyGuessed += letter
        val answerText = answer.text
        var new:String = ""
        for (i in 0 until currentWord.length) {
            new += if (currentWord[i] == letter) {
                letter
            } else {
                answerText[i]
            }
        }
        answer.text = new
        if ('_' !in new) {
            gameOver(true)
        }
    }

    fun newGame(view: View) {
        newGame()
    }

    private fun newGame() {
        currentWord = " "
        setNewWord(newWord())
        clearWrongLetters()
        alreadyGuessed = ""
        mistakesCount = 0
        submit_btn.isClickable = true
        input.isEnabled = true
        hanging_man.setImageResource(R.drawable.hanging_man_01)
    }

    private fun newWord() : String {
        val words: Array<String> = resources.getStringArray(R.array.words)
        val r = Random()
        val index = r.nextInt(120)
        return words[index]
    }

    private fun setNewWord(word: String) {
        Log.d("am2019", "Word: $word")
        currentWord = word
        var answerText = "_"
        for (i in 2..currentWord.length) {
            answerText += "_"
        }
        answer.text = answerText

    }

    private fun clearWrongLetters() {
        wrongLetters.text = "Wrong letters: "
    }

    private fun addWrongLetter(letter: Char) {
        var wrong:String = wrongLetters.text.toString()
        wrong += "$letter "
        wrongLetters.text = wrong
    }
}
