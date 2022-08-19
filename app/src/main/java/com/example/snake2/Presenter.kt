package com.example.snake2

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.Log
import com.example.snake2.data.Apple
import com.example.snake2.data.GameFieldData
import com.example.snake2.data.GameFieldData.Companion.SIZE
import com.example.snake2.data.GameFieldData.Companion.STEP
import com.example.snake2.data.Plug
import com.example.snake2.data.Snake
import kotlinx.coroutines.*
import java.lang.IndexOutOfBoundsException
import kotlin.random.Random

class Presenter(private val gameFieldData: GameFieldData) {
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

    fun startGame() {
        gameFieldData.addApple()
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

        paint.color = Color.GREEN
        for (i in gameFieldData.apples.indices)
            canvas.drawRect(gameFieldData.apples[i].rect, paint)
    }

    private fun Snake.move(index: Int) {
        when (direction) {
            DIR_TOP -> moveTop()
            DIR_RIGHT -> moveRight()
            DIR_BOTTOM -> moveBottom()
            DIR_LEFT -> moveLeft()
        }
        if (turning) {
            turningProgress += STEP
            if (turningProgress >= SIZE + STEP) {
                turning = false
                if (index != gameFieldData.snake.size - 1) {
                    gameFieldData.snake[index + 1].changeDirection(direction)
                }
            }
            if (index == gameFieldData.snake.size - 1) {
                turning = false
                with (gameFieldData.turns) { removeAt(0) }
            }
        }
    }

    fun changeDirection(dir: Int) {
        CoroutineScope(Dispatchers.Default).launch {
            with (gameFieldData.snake[0]) {
                //if (turning) return@launch
                if (direction == dir) return@launch
                when (direction) {
                    DIR_TOP -> if (dir == DIR_BOTTOM) return@launch
                    DIR_RIGHT -> if (dir == DIR_LEFT) return@launch
                    DIR_BOTTOM -> if (dir == DIR_TOP) return@launch
                    DIR_LEFT -> if (dir == DIR_RIGHT) return@launch
                }
                Log.d("changeDirection", "turn")
                while (turning) {
                    delay(1)
                    Log.d("changeDirection", "turning = $turning")
                }
                with (rect) { gameFieldData.turns.add(Plug(Rect(left, top, right, bottom))) }
                changeDirection(dir)
                Log.d("changeDirection", "turned")
            }
        } //val changeDirectionJob: Job =
    }


    fun Snake.moveTop() {
        with (rect) {
            if (bottom >= 0) {
                top -= STEP
                bottom -= STEP
            } else {
                top = gameFieldData.height
                bottom = gameFieldData.height + SIZE
            }
        }
    }

    fun Snake.moveRight() {
        if (rect.left <= gameFieldData.width) {
            rect.right += STEP
            rect.left += STEP
        } else {
            rect.left = 0 - SIZE
            rect.right = 0
        }
    }

    fun Snake.moveBottom() {
        if (rect.top <= gameFieldData.height) {
            rect.top += STEP
            rect.bottom += STEP
        } else {
            rect.top = 0 - SIZE
            rect.bottom = 0
        }
    }

    fun Snake.moveLeft() {
        if (rect.right >= 0) {
            rect.right -= STEP
            rect.left -= STEP
        } else {
            rect.left = gameFieldData.width
            rect.right = gameFieldData.width + SIZE
        }
    }

    /*private fun SnakeBody.moveTop() {
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
    }*/

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