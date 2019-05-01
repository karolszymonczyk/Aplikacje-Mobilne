import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.Log
import android.view.MotionEvent
import com.example.pong.GameView

class Ball(private var x: Float, private var y: Float, var size: Float) {

    var ballX = x
    var ballY = y
    var dx = 15f
    var dy = 20f

    var prevdy = 10f

    private lateinit var gameView: GameView


    init {
        reset()
    }

    fun reset() {
        ballX = x
        ballY = y

        dy = if (prevdy > 0) 20f
        else -20f

        dx = 15f
        changeVerticalDirection()
    }

    fun stop() {
        prevdy = -prevdy
        dx = 0f
        dy = 0f
        ballX = x
        ballY = y
    }

    fun draw(canvas: Canvas) {
        val white = Paint()
        white.setARGB(255, 255, 255, 255)

        canvas.drawOval(RectF(ballX, ballY, ballX + size, ballY + size), white)
    }

    private fun changeHorizontalDirection() {
        dx = -dx
    }

    fun changeVerticalDirection() {
        dy = -dy
    }

    fun setGameView(gameView: GameView) {
        this.gameView = gameView
    }

    private fun bounce() {

        val width = gameView.width.toFloat()

        if (ballX <= 0 || ballX + size >= width) {
            changeHorizontalDirection()
            speedUp()
        }
    }

    private fun speedUp() {

        val a = if (dy < 40 && dy > -40) 1f
        else 0f

        if (dx > 0) dx += a
        else dx -= a

        if (dy > 0) dy += a
        else dy -= a
    }

    fun play() {
        ballX += dx
        ballY += dy

        bounce()
    }
}
