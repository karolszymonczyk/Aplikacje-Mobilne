package com.example.pong

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.LinearLayout
import android.widget.Button

class MainActivity : AppCompatActivity() {

    val GAME = 1

    var p1Size = 0
    var p2Size = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val width = getScreenWidth()

        setButtonLayout(b1p1, width / 2)
        setButtonLayout(b2p1, width / 4)
        setButtonLayout(b3p1, width / 8)

        setButtonLayout(b1p2, width / 8)
        setButtonLayout(b2p2, width / 4)
        setButtonLayout(b3p2, width / 2)


    }

    fun bPlay(view: View) {
        val intent = Intent(this, GameActivity::class.java)
        intent.putExtra("p1Size", p1Size)
        intent.putExtra("p2Size", p2Size)
        startActivityForResult(intent, GAME)
    }

    private fun getScreenWidth(): Int {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.widthPixels
    }

    fun clickP1(view: View) {
        resetButton(p1)
        p1Size = chooseSize(view.tag.toString())
        checkPlay()
        view.setBackgroundResource(R.drawable.my_button_clicked)
    }

    fun clickP2(view: View) {
        resetButton(p2)
        p2Size = chooseSize(view.tag.toString())
        checkPlay()
        view.setBackgroundResource(R.drawable.my_button_clicked)
    }

    private fun setButtonLayout(button: Button, width: Int) {
        val params = button.layoutParams
        params.width = width
        params.height = 30
        button.layoutParams = params
    }

    private fun resetButton(pl: LinearLayout) {
        pl.getChildAt(0).setBackgroundResource(R.drawable.my_button)
        pl.getChildAt(1).setBackgroundResource(R.drawable.my_button)
        pl.getChildAt(2).setBackgroundResource(R.drawable.my_button)
    }

    private fun checkPlay() {
        if (p1Size != 0 && p2Size != 0) {
            bPlay.isEnabled = true
        }
    }

    private fun chooseSize(tag: String): Int {
        var size = 0
        val width = getScreenWidth()
        when (tag) {
            "long" -> size = width / 2
            "medium" -> size = width / 4
            "short" -> size = width / 8
        }
        return size
    }
}
