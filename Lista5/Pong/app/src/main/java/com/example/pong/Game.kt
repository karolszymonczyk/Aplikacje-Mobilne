package com.example.pong

import Ball
import android.graphics.RectF

class Game(private val player1: Player, private val player2: Player, private val ball: Ball) {

    var p1Score: Int = 0
    var p2Score: Int = 0

    fun bounce() {

        val ballRect = RectF(ball.ballX, ball.ballY, ball.ballX + ball.size, ball.ballY + ball.size)
        val p1Rect =
            RectF(player1.playerX, player1.playerY, player1.playerX + player1.size, player1.playerY + player1.pHeight)
        val p2Rect =
            RectF(player2.playerX, player2.playerY, player2.playerX + player2.size, player2.playerY + player2.pHeight)

        if (ballRect.intersect(p1Rect) || ballRect.intersect(p2Rect)) {
            ball.changeVerticalDirection()
        }
    }

    fun checkGoal(): Boolean {

        if (ball.ballY < -ball.size / 2) {
            p2Score++
            return true
        } else if (ball.ballY + ball.size > player2.playerY + player2.pHeight + 5f + ball.size / 2) {
            p1Score++
            return true
        }

        return false
    }
}