package com.example.pong

import android.content.Context

class MyPreferences(context: Context) {

    private val PREFERENCE_NAME = "Game"
    private val preference = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

    fun getPreferenceInt(name: String): Int {
        return preference.getInt(name, 0)
    }

    fun getPreferenceFloat(name: String): Float {
        return preference.getFloat(name, 0f)
    }

    fun setPreferenceInt(name: String, value: Int) {
        val editor = preference.edit()
        editor.putInt(name, value)
        editor.apply()
    }

    fun setPreferenceFloat(name: String, value: Float) {
        val editor = preference.edit()
        editor.putFloat(name, value)
        editor.apply()
    }
}