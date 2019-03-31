package com.example.hangman

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var words: Array<String>? = null

    var word: String = ""
    var guessed: String = ""

    private var i: Int = 0

    private val images: IntArray = intArrayOf(
        R.drawable.hangman0,
        R.drawable.hangman1,
        R.drawable.hangman2,
        R.drawable.hangman3,
        R.drawable.hangman4,
        R.drawable.hangman5,
        R.drawable.hangman6
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        words = resources.getStringArray(R.array.words) //pobranie listy
        if (words == null) {
            println("Something went wrong...")
            return
        }
        textView.text = guessed
        this.randomWord()
    }

    private fun randomWord() {
        var random = words!!.random() //!! -> not null
        word = random
        for (i in 1..word.length) {
            guessed += "_ "
        }
        textView.text = guessed
    }

    fun buttonClick(view: View) {
        if (input.text.toString() == "") {
            input.setText("")
            return
        }
        var letter: Char = input.text.toString().single().toLowerCase()
        if (letter == ' ') {
            input.setText("")
            return
        }
        input.setText("")
        if (!checkLetter(letter)) {
            nextImage()
        }
    }

    private fun nextImage() {
        i++
        imageView.setImageResource(images[i])
        if (i == 6) {
            Toast.makeText(this@MainActivity, "You lost!", Toast.LENGTH_SHORT).show()
            showSolution()
        }
    }

    private fun checkLetter(letter: Char): Boolean {
        if (guessed.contains(letter)){
            Toast.makeText(this@MainActivity, "Letter \"$letter\" is already in the guess!", Toast.LENGTH_SHORT).show()
            return false
        }
        if (word.contains(letter)) {
            var i: Int = word.length - 1
            while (i != -1) {
                if (word[i] == letter) {
                    guessed = replaceLetter(i, letter)
                }
                i--
            }
            Toast.makeText(this@MainActivity, "Great!", Toast.LENGTH_SHORT).show()
            textView.text = guessed
            if (!guessed.contains("_")) {
                textView.setTextColor(Color.parseColor("#FF14DC21"))
                button.isEnabled = false
            }
            return true
        }
        Toast.makeText(this@MainActivity, "Letter \"$letter\" is not in the word", Toast.LENGTH_SHORT).show()
        return false
    }

    private fun replaceLetter(index: Int, letter: Char): String {
        var chars: CharArray = guessed.toCharArray()
        chars[2 * index] = letter

        return chars.joinToString("")
    }

    private fun showSolution() {
        var i: Int = word.length - 1
        while (i != -1) {
                guessed = replaceLetter(i, word[i])
            i--
        }
        textView.text = guessed
        textView.setTextColor(Color.parseColor("#FFE3181B"))
        button.isEnabled = false
    }

    fun bNextClick(view: View) {
        i = 0
        imageView.setImageResource(images[i])
        textView.setTextColor(Color.parseColor("#FF363535"))
        input.setText("")
        guessed = ""
        randomWord()
        button.isEnabled = true
    }
}

