package com.example.pong

import android.graphics.Canvas
import android.graphics.Paint

class Player(private var x: Float, private var y: Float, var size: Float) {

    var playerX = x - size / 2
    var playerY = y

    var pHeight: Float = 30f

    fun setX(x: Float) {
        playerX = x - size / 2
    }

    fun reset() {
        playerX = x - size / 2
    }


    fun draw(canvas: Canvas) {
        val col = Paint()
        col.setARGB(255, 249, 68, 68)

        canvas.drawRoundRect(playerX, playerY, playerX + size, playerY + pHeight, 10f, 10f, col)
    }
}