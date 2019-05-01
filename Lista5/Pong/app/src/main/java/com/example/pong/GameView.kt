package com.example.pong

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.graphics.*
import Ball
import android.annotation.SuppressLint
import java.util.*
import kotlin.concurrent.schedule
import android.graphics.DashPathEffect
import android.text.TextPaint

class GameView(context: Context, attributeSet: AttributeSet) : SurfaceView(context, attributeSet),
    SurfaceHolder.Callback {

    private val thread: GameThread

    private lateinit var game: Game

    private lateinit var ball: Ball
    private lateinit var player1: Player
    private lateinit var player2: Player

    private var p1Size = (context as GameActivity).p1Size
    private var p2Size = (context as GameActivity).p2Size

    private val myPreferences = MyPreferences(context)


    init {
        holder.addCallback(this)
        thread = GameThread(holder, this)
    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        thread.setRunning(false)
        thread.join()
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {

        if (myPreferences.getPreferenceInt("Load") == 0) {
            setNewGame()
        } else {
            loadGame()
        }

        thread.setRunning(true)
        thread.start()
    }

    fun update() {

        game.bounce()
        if (game.checkGoal()) {

            player1.reset()
            player2.reset()
            ball.stop()

            Timer().schedule(500) {
                ball.reset()
            }
        }
        ball.play()

        saveGame()
    }

    private fun saveGame() {
        myPreferences.setPreferenceFloat("BallX", ball.ballX)
        myPreferences.setPreferenceFloat("BallY", ball.ballY)
        myPreferences.setPreferenceFloat("BallDx", ball.dx)
        myPreferences.setPreferenceFloat("BallDy", ball.dy)
        myPreferences.setPreferenceFloat("PrevDy", ball.prevdy)

        myPreferences.setPreferenceFloat("Player1X", player1.playerX)
        myPreferences.setPreferenceFloat("Player2X", player2.playerX)

        myPreferences.setPreferenceInt("p1Score", game.p1Score)
        myPreferences.setPreferenceInt("p2Score", game.p2Score)
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        if (canvas == null) return

        drawBackground(canvas)

        ball.draw(canvas)

        player1.draw(canvas)
        player2.draw(canvas)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        for (i in 0 until event.pointerCount) {
            if (event.y < height / 2) {     //top
                setPlayer(player1, event)
            } else {                        //bottom
                setPlayer(player2, event)
            }
        }
        return true
    }

    private fun setPlayer(player: Player, event: MotionEvent) {
        when {
            event.x + player.size / 2 > width -> player.setX(width - player.size / 2)
            event.x - player.size / 2 < 0f -> player.setX(0f + player.size / 2)
            else -> player.setX(event.x)
        }
    }

    private fun setNewGame() {
        ball = Ball(width / 2f, height / 2f, 60f)
        ball.setGameView(this)

        player1 = Player(width / 2f, 20f, p1Size.toFloat())
        player2 = Player(width / 2f, height - 45f, p2Size.toFloat())

        game = Game(player1, player2, ball)
    }

    private fun loadGame() {
        setNewGame()
        ball.ballX = myPreferences.getPreferenceFloat("BallX")
        ball.ballY = myPreferences.getPreferenceFloat("BallY")

        ball.dx = myPreferences.getPreferenceFloat("BallDx")
        ball.dy = myPreferences.getPreferenceFloat("BallDy")
        ball.prevdy = myPreferences.getPreferenceFloat("PrevDy")

        player1.playerX = myPreferences.getPreferenceFloat("Player1X")

        player2.playerX = myPreferences.getPreferenceFloat("Player2X")

        game.p1Score = myPreferences.getPreferenceInt("p1Score")
        game.p2Score = myPreferences.getPreferenceInt("p2Score")
    }

    private fun drawBackground(canvas: Canvas?) {
        canvas?.drawColor(Color.parseColor("#FF04658F"))
        val dashPaint = Paint()
        dashPaint.setARGB(255, 166, 193, 247)
        dashPaint.style = Paint.Style.STROKE
        dashPaint.strokeWidth = 30f
        dashPaint.pathEffect = DashPathEffect(floatArrayOf(40f, 40f, 40f, 40f), 0f)
        canvas?.drawLine(0f, height / 2 + 20f, width.toFloat(), height / 2 + 20f, dashPaint)

        val textPaint = TextPaint()

        textPaint.color = Color.parseColor("#a6c1f7")
        textPaint.textSize = 400f
        textPaint.alpha = 100
        textPaint.textAlign = Paint.Align.CENTER

        val xP = width / 2f
        val yP = 3 * height / 4f - (textPaint.descent() + textPaint.ascent()) / 2f //todo ?

        canvas?.rotate(-180f, width / 2f, height / 2f)
        canvas?.drawText("${game.p1Score}", xP, yP, textPaint)
        canvas?.rotate(180f, width / 2f, height / 2f)

        canvas?.drawText("${game.p2Score}", xP, yP, textPaint)

    }
}