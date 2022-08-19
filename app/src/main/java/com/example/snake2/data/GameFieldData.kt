package com.example.snake2.data

import android.graphics.Canvas
import android.graphics.Rect
import android.util.Log
import android.view.SurfaceHolder
import com.example.snake2.Presenter.Companion.DIR_BOTTOM
import com.example.snake2.Presenter.Companion.DIR_DEFAULT
import com.example.snake2.Presenter.Companion.DIR_RIGHT
import com.example.snake2.Presenter.Companion.DIR_TOP
import kotlin.random.Random

class GameFieldData(val width: Int, val height: Int) {
    val snake = ArrayList<Snake>()
    val turns = ArrayList<Plug>()
    val apples = ArrayList<Apple>()

    companion object {
        const val SIZE = 70
        const val STEP = 5
    }

    private val startLeft = width / 2
    private val startTop = height / 2

    init {
        val rect = Rect(startLeft, startTop,startLeft + SIZE,startTop + SIZE)
        snake.add(Snake(rect, DIR_DEFAULT))
        growSnake()
        growSnake()
        growSnake()
        growSnake()
        growSnake()
    }

    fun growSnake() {
        val rectangle: Rect
        with (snake[snake.size - 1]) {
            when (direction) {
                DIR_TOP -> with (rect) { rectangle = Rect(left, bottom, right, bottom + SIZE) }
                DIR_RIGHT -> with (rect) { rectangle = Rect(left - SIZE, top, left, bottom) }
                DIR_BOTTOM -> with (rect) { rectangle = Rect(left, top - SIZE, right, top) }
                else -> with (rect) { rectangle = Rect(right, top, right + SIZE, bottom) }
            }
            snake.add(Snake(rectangle, direction))
        }
    }

    fun addApple() {
        var t = 5000
        var r = 5000
        var b = -5000
        var l = -5000

        for (s in snake.indices) {
            with(snake[s].rect) {
                if (t > top) t = top
                if (r > right) r = right
                if (b < bottom) b = bottom
                if (l < left) l = left
            }
        }

        Log.d("GameFieldData/apple", "t = $t")
        Log.d("GameFieldData/apple", "r = $r")
        Log.d("GameFieldData/apple", "b = $b")
        Log.d("GameFieldData/apple", "l = $l")

        val boolY = Random.nextBoolean()
        val boolX = Random.nextBoolean()

        val ry1 = IntRange(2 * SIZE, t)
        val ry2 = IntRange(b, height - SIZE)
        val rx1 = IntRange(2 * SIZE, l)
        val rx2 = IntRange(r, width - SIZE)

        val y = if (boolY) ry1.random() - 2 * SIZE
                else ry2.random() + SIZE
        val x = if (boolX) rx1.random() - 2 * SIZE
                else rx2.random() + SIZE

        Log.d("GameFieldData/apple", "y = $y")
        Log.d("GameFieldData/apple", "x = $x")
        Log.d("GameFieldData/apple", "")
//        val y = if (boolY && t > 0) r1.random() - 2 * SIZE else Random.nextInt(b, SCREEN_HEIGHT) + SIZE
//        val x = if (boolX && l > 0) r2.random() - 2 * SIZE else Random.nextInt(r, SCREEN_WIDTH) + SIZE

        apples.add(Apple(Rect(x, y, x + SIZE, y + SIZE)))
    }

    /*fun calculateWidth(canvas: Canvas) {
        height *= if (canvas.height > canvas.width) canvas.height / canvas.width
                else canvas.width / canvas.height
    }*/
}