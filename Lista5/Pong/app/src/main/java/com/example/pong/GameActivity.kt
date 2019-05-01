package com.example.pong

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager

@SuppressLint("Registered")
class GameActivity : AppCompatActivity() {

    var p1Size = 0
    var p2Size = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        super.onCreate(savedInstanceState)

        setDefaultPreferences()
    }

    override fun onResume() {
        super.onResume()

        p1Size = intent.getIntExtra("p1Size", 0)
        p2Size = intent.getIntExtra("p2Size", 0)

        setContentView(R.layout.game_activity)
    }

    override fun onPause() {
        super.onPause()

        val myPreferences = MyPreferences(this)
        myPreferences.setPreferenceInt("Load", 1)
    }

    private fun setDefaultPreferences() {
        val myPreferences = MyPreferences(this)
        myPreferences.setPreferenceInt("Load", 0)
    }
}