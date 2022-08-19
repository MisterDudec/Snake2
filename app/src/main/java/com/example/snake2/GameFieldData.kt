package com.example.snake2

import android.graphics.Canvas
import android.graphics.Rect
import com.example.snake2.Presenter.Companion.DIR_BOTTOM
import com.example.snake2.Presenter.Companion.DIR_DEFAULT
import com.example.snake2.Presenter.Companion.DIR_RIGHT
import com.example.snake2.Presenter.Companion.DIR_TOP

class GameFieldData {
    val snake = ArrayList<SnakeBody>()
    val turns = ArrayList<SnakeBody>()
    var width = 20
    var height = 40

    companion object {
        var SCREEN_WIDTH = -1
        var SCREEN_HEIGHT = -1
        const val SIZE = 70
        const val STEP = 5
    }

    val startLeft = SCREEN_WIDTH / 2
    val startTop = SCREEN_HEIGHT / 2

    init {
        val rect = Rect(startLeft, startTop,startLeft + SIZE,startTop + SIZE)
        snake.add(SnakeBody(rect, DIR_DEFAULT))
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
            snake.add(SnakeBody(rectangle, direction))
        }
    }

    fun calculateWidth(canvas: Canvas) {
        height *= if (canvas.height > canvas.width) canvas.height / canvas.width
                else canvas.width / canvas.height
    }
}