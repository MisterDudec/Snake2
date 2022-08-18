package com.example.snake2

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import com.example.snake2.GameFieldData.Companion.SCREEN_HEIGHT
import com.example.snake2.GameFieldData.Companion.SCREEN_WIDTH
import com.example.snake2.GameFieldData.Companion.SIZE
import java.lang.IndexOutOfBoundsException

class Presenter {
    private val gameFieldData = GameFieldData()
    private val step = 5
    private val paint: Paint = Paint()

    companion object {
        const val DIR_TOP = 0
        const val DIR_RIGHT = 1
        const val DIR_BOTTOM = 2
        const val DIR_LEFT = 3
        var direction = DIR_TOP
    }

    init {
        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL
    }

    fun updateGame() {
        for (i in gameFieldData.snake.indices) {
            try {
                with (gameFieldData.snake[i]) {
                    when (direction) {
                        DIR_TOP -> moveTop()
                        DIR_RIGHT -> moveRight()
                        DIR_BOTTOM -> moveBottom()
                        DIR_LEFT -> moveLeft()
                    }
                }
            } catch (e: IndexOutOfBoundsException) {
                e.printStackTrace()
            }
        }
    }

    fun drawFrame(canvas: Canvas) {
        canvas.drawColor(Color.BLACK)
        paint.color = Color.RED
        for (i in gameFieldData.snake.indices)
            canvas.drawRect(gameFieldData.snake[i], paint)
    }

    private var transition = false

    private fun Rect.moveTop() {
        if (top <= 0 && !transition) {
            val rect = Rect(left, SCREEN_HEIGHT, right, SCREEN_HEIGHT + SIZE)
            gameFieldData.snake.add(rect)
            transition = true
        }
        if (bottom >= 0) {
            top -= step
            bottom -= step
        } else {
            gameFieldData.snake.remove(this)
            transition = false
        }
    }

    /*private fun Rect.moveTop() {
        if (bottom >= 0) {
            top -= step
            bottom -= step
        } else {
            top = SCREEN_HEIGHT
            bottom = SCREEN_HEIGHT + SIZE
        }
    }*/

    private fun Rect.moveRight() {
        if (left <= SCREEN_WIDTH) {
            right += step
            left += step
        } else {
            left = 0 - SIZE
            right = 0
        }
    }

    private fun Rect.moveBottom() {
        if (top <= SCREEN_HEIGHT) {
            top += step
            bottom += step
        } else {
            top = 0 - SIZE
            bottom = 0
        }
    }

    private fun Rect.moveLeft() {
        if (right >= 0) {
            right -= step
            left -= step
        } else {
            left = SCREEN_WIDTH
            right = SCREEN_WIDTH + SIZE
        }
    }

    private fun Rect.move() {
        var oppositeSide: Int
        var moveSide: Int
        var border: Int

        when (direction) {
            DIR_TOP -> {
                moveSide = top
                oppositeSide = bottom
                border = 0
            }
            DIR_RIGHT -> moveRight()
            DIR_BOTTOM -> moveBottom()
            DIR_LEFT -> {
                oppositeSide = right
                moveSide = left
                border = SCREEN_WIDTH
            }
        }

        if (oppositeSide >= 0) {
            oppositeSide -= step
            moveSide -= step
        } else {
            moveSide = border
            oppositeSide = border + SIZE
        }

        right = oppositeSide
        left = moveSide
    }

}