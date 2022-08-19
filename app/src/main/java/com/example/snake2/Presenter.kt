package com.example.snake2

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import com.example.snake2.GameFieldData.Companion.SCREEN_HEIGHT
import com.example.snake2.GameFieldData.Companion.SCREEN_WIDTH
import com.example.snake2.GameFieldData.Companion.SIZE
import com.example.snake2.GameFieldData.Companion.STEP
import java.lang.IndexOutOfBoundsException

class Presenter {
    private val gameFieldData = GameFieldData()
    private val paint: Paint = Paint()

    companion object {
        const val DIR_STOP = -1
        const val DIR_TOP = 0
        const val DIR_RIGHT = 1
        const val DIR_BOTTOM = 2
        const val DIR_LEFT = 3
        const val DIR_DEFAULT = DIR_TOP
        //var direction = DIR_TOP
    }

    init {
        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL
    }

    fun updateGame() {
        for (i in gameFieldData.snake.indices) {
            try {
                with (gameFieldData.snake[i]) {
                    move(gameFieldData.snake.indexOf(this))
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
            canvas.drawRect(gameFieldData.snake[i].rect, paint)
        for (i in gameFieldData.turns.indices)
            canvas.drawRect(gameFieldData.turns[i].rect, paint)
    }

    private fun SnakeBody.move(index: Int) {
        when (direction) {
            DIR_TOP -> moveTop()
            DIR_RIGHT -> moveRight()
            DIR_BOTTOM -> moveBottom()
            DIR_LEFT -> moveLeft()
        }
        if (turning) {
            turningProgress += STEP
            if (turningProgress >= SIZE) {
                turning = false
                if (index != gameFieldData.snake.size - 1) {
                    gameFieldData.snake[index + 1].changeDirection(direction)
                } else {
                    with (gameFieldData.turns) { removeAt(size - 1) }
                }
            }
        }
    }

    fun changeDirection(dir: Int) {
        with(gameFieldData.snake[0].rect) {
            gameFieldData.turns.add(SnakeBody(Rect(left, top, right, bottom), DIR_STOP))
            gameFieldData.snake[0].changeDirection(dir)
        }
    }

    private fun SnakeBody.moveTop() {
        if (rect.top <= 0 && !transition) {
            val snakeBody = SnakeBody(Rect(rect.left, SCREEN_HEIGHT, rect.right, SCREEN_HEIGHT + SIZE), DIR_TOP)
            gameFieldData.snake.add(snakeBody)
            transition = true
        }
        rect.top -= STEP
        rect.bottom -= STEP
        if (rect.bottom < 0) {
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

    private fun SnakeBody.moveRight() {
        if (rect.left <= SCREEN_WIDTH) {
            rect.right += STEP
            rect.left += STEP
        } else {
            rect.left = 0 - SIZE
            rect.right = 0
        }
    }

    private fun SnakeBody.moveBottom() {
        if (rect.top <= SCREEN_HEIGHT) {
            rect.top += STEP
            rect.bottom += STEP
        } else {
            rect.top = 0 - SIZE
            rect.bottom = 0
        }
    }

    private fun SnakeBody.moveLeft() {
        if (rect.right >= 0) {
            rect.right -= STEP
            rect.left -= STEP
        } else {
            rect.left = SCREEN_WIDTH
            rect.right = SCREEN_WIDTH + SIZE
        }
    }

    /*private fun Rect.move() {
        var oppositeSide: Int
        var moveSide: Int
        var moveBorder: Int
        var oppositeBorder: Int
        var diff: Int

        when (direction) {
            DIR_TOP -> {
                moveSide = top
                oppositeSide = bottom
                moveBorder = 0
                oppositeBorder = SCREEN_HEIGHT
                diff = + SIZE
            }
            DIR_RIGHT -> moveRight()
            DIR_BOTTOM -> moveBottom()
            DIR_LEFT -> {
                oppositeSide = right
                moveSide = left
                moveBorder = SCREEN_WIDTH
            }
        }

        if (moveSide <= 0 && !transition) {
            val rect = Rect(left, oppositeBorder, right, oppositeBorder + diff)
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

        if (oppositeSide >= 0) {
            oppositeSide -= step
            moveSide -= step
        } else {
            moveSide = moveBorder
            oppositeSide = moveBorder + SIZE
        }

        right = oppositeSide
        left = moveSide
    }*/

}