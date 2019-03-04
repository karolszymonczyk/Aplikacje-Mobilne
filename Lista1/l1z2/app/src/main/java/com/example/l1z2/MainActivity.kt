package com.example.l1z2

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {

    private var computerSign = ""
    private var yourSign = ""
    private var yourScore = 0
    private var computerScore = 0

    private var winRatio = 50.0

    private val df = DecimalFormat("#.##")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        df.roundingMode = RoundingMode.CEILING
    }

    private fun play() {
        computerSign = Sign.pickRandom().toString()
        findViewById<TextView>(R.id.tvComputer).text = computerSign

        checkWinner()

        findViewById<TextView>(R.id.tvYourScore).text = "$yourScore"
        findViewById<TextView>(R.id.tvComputerScore).text = "$computerScore"

    }

    @SuppressLint("SetTextI18n")
    private fun calculateWinRatio() {
        var yourScoreD = yourScore.toDouble()
        var computerScoreD = computerScore.toDouble()
        winRatio = (yourScoreD / (yourScoreD + computerScoreD)) * 100

        findViewById<ProgressBar>(R.id.progressBar).setProgress(winRatio.roundToInt(), true)
        findViewById<TextView>(R.id.winRatio).text = "${df.format(winRatio)}%"
    }

    @SuppressLint("SetTextI18n")
    private fun win(win: Int) {
        val info = findViewById<TextView>(R.id.tvInfo)
        when (win) {
            //win
            1 -> {
                info.text = "You win!"
                info.findViewById<TextView>(R.id.tvInfo).setTextColor(Color.parseColor("#FF14DC21"))
                calculateWinRatio()
            }
            0 -> {
                //tie
                info.text = "Tie!"
                findViewById<TextView>(R.id.tvInfo).setTextColor(Color.parseColor("#FF000000"))
            }
            else -> {
                //lost
                info.text = "You lost!"
                findViewById<TextView>(R.id.tvInfo).setTextColor(Color.parseColor("#FFE3181B"))
                calculateWinRatio()
            }
        }
    }

    private fun checkWinner() {
        val tvComputer = findViewById<TextView>(R.id.tvComputer)
        val tvYou = findViewById<TextView>(R.id.tvYou)

        tvComputer.setTypeface(null, Typeface.NORMAL)
        tvYou.setTypeface(null, Typeface.NORMAL)

        when (yourSign) {
            "ROCK" -> {
                when (computerSign) {
                    "PAPER" -> {
                        computerScore++
                        tvComputer.setTypeface(null, Typeface.BOLD)
                        win(-1)
                    }
                    "SCISSORS" -> {
                        yourScore++
                        tvYou.setTypeface(null, Typeface.BOLD)
                        win(1)
                    }
                    else -> win(0)
                }
            }
            "PAPER" -> {
                when (computerSign) {
                    "SCISSORS" -> {
                        computerScore++
                        tvComputer.setTypeface(null, Typeface.BOLD)
                        win(-1)
                    }
                    "ROCK" -> {
                        yourScore++
                        tvYou.setTypeface(null, Typeface.BOLD)
                        win(1)
                    }
                    else -> win(0)
                }
            }
            "SCISSORS" -> {
                when (computerSign) {
                    "ROCK" -> {
                        computerScore++
                        tvComputer.setTypeface(null, Typeface.BOLD)
                        win(-1)
                    }
                    "PAPER" -> {
                        yourScore++
                        tvYou.setTypeface(null, Typeface.BOLD)
                        win(1)
                    }
                    else -> win(0)
                }
            }
        }
    }

    fun bRockClick(view: View) {
        yourSign = "ROCK"
        findViewById<TextView>(R.id.tvYou).text = yourSign
        play()
    }

    fun bPaperClick(view: View) {
        yourSign = "PAPER"
        findViewById<TextView>(R.id.tvYou).text = yourSign
        play()
    }

    fun bScissorsClick(view: View) {
        yourSign = "SCISSORS"
        findViewById<TextView>(R.id.tvYou).text = yourSign
        play()
    }

    fun bRestartClick(view: View) {
        findViewById<TextView>(R.id.tvComputer).text = ""
        findViewById<TextView>(R.id.tvComputerScore).text = "0"
        findViewById<TextView>(R.id.tvYou).text = ""
        findViewById<TextView>(R.id.tvYourScore).text = "0"
        findViewById<TextView>(R.id.tvInfo).text = ""
        findViewById<TextView>(R.id.winRatio).text = "50%"
        yourScore = 0
        computerScore = 0
        winRatio = 50.0
        findViewById<ProgressBar>(R.id.progressBar).progress = 50
    }
}
