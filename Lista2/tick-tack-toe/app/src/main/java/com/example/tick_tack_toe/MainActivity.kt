package com.example.tick_tack_toe

import android.graphics.Color
import android.graphics.Typeface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.Switch
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    //todo jak jest remis z botem to zmienia na drugi znak i cały czas go pokazuje
    //todo jak się wygrywa albo przegrywa to na pierwszą rundę pogrubia drugi znak
    //todo jak bot wygra to się zamieniają znaki

    //żeby nie restartowała się to zrobione w Manifest
    //        <activity android:name=".MainActivity"
    //        android:configChanges="orientation|screenSize">

    private var board = arrayOf<Array<Int>>()

//    private var buttons: ArrayList<Button> = arrayListOf()

    private var scoreX: Int = 0
    private var scoreY: Int = 0
    private var end: Boolean = false

    private var sign = "X"
    private val signs = arrayOf("X", "O")

    private var lmX = 0 //last move
    private var lmY = 0

    private var playerSign = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        screen.setOnTouchListener(object : View.OnTouchListener { //do następnej planszy
            override fun onTouch(v: View, m: MotionEvent): Boolean {
                if (end) {
//                    buttons.forEach { it.text = "" }
                    clearBoard()
                    changeButtons(true)
                    end = false
                    textView_x.setTextColor(Color.parseColor("#FF000000"))
                    textView_y.setTextColor(Color.parseColor("#FF000000"))
                }
                return true
            }
        })

        val sw = findViewById<Switch>(R.id.switchBot)
        sw.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked) {
                playerSign = sign
            }
        }

        for (i in 0..4) {
            var array = arrayOf<Int>()
            for (j in 0..4) {
                array += 0
            }
            board += array
        }
        chooseFirst()
    }

    private fun resetBoard() {
        for (i in 0..4) {
            for (j in 0..4) {
                board[i][j] = 0
            }
        }
    }

    private fun chooseFirst() {
        val r = Random()
        sign = signs[r.nextInt(2)]
        if (sign == "X") {
            textView_x.setTypeface(null, Typeface.BOLD)
            textView_y.setTypeface(null, Typeface.NORMAL)
        } else {
            textView_y.setTypeface(null, Typeface.BOLD)
            textView_x.setTypeface(null, Typeface.NORMAL)
        }
    }

    fun buttonClick(view: View) {

        val button: Button = findViewById(view.id)
        if (button.text != "") {
            return
        }
        button.text = sign

        val x: Int = button.toString().takeLast(3).take(1).toInt()
        val y: Int = button.toString().takeLast(2).take(1).toInt()

        setBoard(x, y)

        lmX = x
        lmY = y

        if (fullBoard()) { //jak jest pełna już plansza
            end = true
            changeButtons(false)
            resetBoard()
            Toast.makeText(this, "Tie!", Toast.LENGTH_SHORT).show()
            if(switchBot.isChecked && sign != playerSign) {
                changeSign()
            }
        }

        if (!end && switchBot.isChecked) {
            botMove()
        }
    }

    private fun changeSign() {
        if (sign == "X") {
            textView_x.setTypeface(null, Typeface.NORMAL)
            textView_y.setTypeface(null, Typeface.BOLD)
            sign = "O"
        } else {
            textView_y.setTypeface(null, Typeface.NORMAL)
            textView_x.setTypeface(null, Typeface.BOLD)
            sign = "X"
        }
    }

    private fun fullBoard(): Boolean {

        for (i in 0..4) {
            for (j in 0..4) {
                if (board[i][j] == 0) {
                    return false
                }
            }
        }
        return true
    }

    private fun setBoard(x: Int, y: Int) {
        if (sign == "X") { //X = 1
            board[x][y] = 1
            checkWin()
            if (!(end && switchBot.isChecked) || sign != playerSign) {
                textView_x.setTypeface(null, Typeface.NORMAL)
                textView_y.setTypeface(null, Typeface.BOLD)
                sign = "O"
            }
        } else { // O = -1
            board[x][y] = -1
            checkWin()
            if (!(end && switchBot.isChecked) || sign != playerSign) {
                textView_y.setTypeface(null, Typeface.NORMAL)
                textView_x.setTypeface(null, Typeface.BOLD)
                sign = "X"
            }
        }
    }

    private fun checkWin() {

        var x: Int
        var y: Int
        var d1 = 0
        var d2 = 0

        for (i in 0..4) { //sprawdzanie lini
            x = 0
            y = 0
            for (j in 0..4) {
                if (board[i][j] != 0) {
                    if (j == i) { //do sprawdzania jednej przekątnej
                        d1 += board[i][j]
                    }
                    if (j + i == 4) { //do sprawedzania drugiej przekątnej
                        d2 += board[i][j]
                    }
                    x += board[i][j]
                }
                if (board[j][i] != 0) {
                    y += board[j][i]
                }
                if (x == 5 || x == -5 || y == 5 || y == -5) {
                    endGame()
                    return
                } else if (d1 == 5 || d1 == -5 || d2 == 5 || d2 == -5) {
                    endGame()
                    return
                }
            }
        }
    }

    private fun endGame() {
        changeButtons(false)
        Toast.makeText(this, "\t\t\t$sign wins!\nTap score to play", Toast.LENGTH_SHORT).show()

        if (sign == "X") {
            scoreX++
            tvScoreX.text = scoreX.toString()
            textView_x.setTextColor(Color.parseColor("#FF14DC21"))
        } else {
            scoreY++
            tvScoreO.text = scoreY.toString()
            textView_y.setTextColor(Color.parseColor("#FF14DC21"))
        }

//        println("O : $scoreY       X : $scoreX")

        end = true
        resetBoard()
    }

    fun bResetClick(view: View) {
        scoreX = 0
        scoreY = 0

        end = false

        tvScoreX.text = scoreX.toString()
        tvScoreO.text = scoreY.toString()

//        buttons.forEach { it.text = "" }
        resetBoard()
        clearBoard()

        textView_x.setTextColor(Color.parseColor("#FF000000"))
        textView_y.setTextColor(Color.parseColor("#FF000000"))

        changeButtons(true)

        chooseFirst()
    }

    private fun clearBoard() {

        var button: Button?

        for (i in 0..4) {
            button = findViewById(board0.getChildAt(i).id)
            button.text = ""
            button = findViewById(board1.getChildAt(i).id)
            button.text = ""
            button = findViewById(board2.getChildAt(i).id)
            button.text = ""
            button = findViewById(board3.getChildAt(i).id)
            button.text = ""
            button = findViewById(board4.getChildAt(i).id)
            button.text = ""
        }
    }

    private fun changeButtons(enable: Boolean) {

        for (i in 0..4) {
            board0.getChildAt(i).isEnabled = enable
            board1.getChildAt(i).isEnabled = enable
            board2.getChildAt(i).isEnabled = enable
            board3.getChildAt(i).isEnabled = enable
            board4.getChildAt(i).isEnabled = enable
        }
    }

    private fun botMove() {

        val poss = getPossibilities()

        var x = 0
        var y = 0

        if (poss.isEmpty()) {
            for (i in 0..4) {
                for (j in 0..4) {
                    if (board[i][j] == 0) {
                        x = i
                        y = j
                    }
                }
            }

        } else {
            val choice = poss.random()
            x = choice.take(1).toInt()
            y = choice.takeLast(1).toInt()

//        Log.d("tag", "x : $x     y : $y")
//            println("pozycje : $poss")
//            println(choice)
        }

        var button: Button? = null

        when (x) {
            0 -> button = findViewById(board0.getChildAt(y).id)
            1 -> button = findViewById(board1.getChildAt(y).id)
            2 -> button = findViewById(board2.getChildAt(y).id)
            3 -> button = findViewById(board3.getChildAt(y).id)
            4 -> button = findViewById(board4.getChildAt(y).id)
        }

        if (button != null) {
            button.text = sign
            setBoard(x, y)
        }
    }

    private fun getPossibilities(): MutableList<String> {
        var poss: MutableList<String> = ArrayList()

        for (x in 0..4) {
            for (y in 0..4) {
                if (board[x][y] == 0) {
                    when (y) {
                        lmY - 1 -> {
                            when (x) {
                                lmX - 1 -> poss.add(x.toString() + y.toString())
                                lmX -> poss.add(x.toString() + y.toString())
                                lmX + 1 -> poss.add(x.toString() + y.toString())
                            }
                        }
                        lmY -> {
                            when (x) {
                                lmX - 1 -> poss.add(x.toString() + y.toString())
                                lmX + 1 -> poss.add(x.toString() + y.toString())
                            }
                        }
                        lmY + 1 -> {
                            when (x) {
                                lmX - 1 -> poss.add(x.toString() + y.toString())
                                lmX -> poss.add(x.toString() + y.toString())
                                lmX + 1 -> poss.add(x.toString() + y.toString())
                            }
                        }
                    }
                }
            }
        }

        return poss
    }

//    override fun onRestoreInstanceState(savedInstanceState: Bundle) { //możliwe że nie jest to potrzebne
//        super.onRestoreInstanceState(savedInstanceState)
//
//        val savedScoreX = savedInstanceState.getCharSequence("scoreX")
//        scoreX = savedScoreX.toString().toInt()
//        tvScoreX.text = scoreX.toString()
//
//        val savedScoreY = savedInstanceState.getCharSequence("scoreY")
//        scoreY = savedScoreY.toString().toInt()
//        tvScoreO.text = scoreY.toString()
//    }
//
//    override fun onSaveInstanceState(outState: Bundle) { //możliwe że nie jest to potrzebne
//        super.onSaveInstanceState(outState)
//
//        outState.putCharSequence("scoreX", scoreX.toString())
//        outState.putCharSequence("scoreY", scoreY.toString())
//    }
}
