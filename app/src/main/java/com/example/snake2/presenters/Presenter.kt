package com.example.snake2.presenters

import android.graphics.Canvas
import android.graphics.Paint
import com.example.snake2.data.GameFieldData
import com.example.snake2.data.GameFieldData.Companion.SIZE
import com.example.snake2.data.GameFieldData.Companion.STEP
import com.example.snake2.data.Snake
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Presenter(
    private val gameFieldData: GameFieldData,
    private val backgroundColor: Int,
    private val snakeColor: Int,
    private val appleColor: Int
) {
    private val logName = "Presenter"
    private val paint: Paint = Paint()
    var pause = false

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

    fun pauseResumeGame() {
        pause = !pause
    }

    fun isPaused() : Boolean {
        return pause
    }

    fun startGame() {
        repeat(3) {
            gameFieldData.addApple()
        }
    }

    fun updateGame() {
        gameFieldData.snake.forEachIndexed { index, snake ->
            snake.move(index)
        }
        checkSnakeCollision()
        checkAppleCollision()
    }

    fun drawFrame(canvas: Canvas) {
        canvas.drawColor(backgroundColor)

        paint.color = appleColor
        for (i in gameFieldData.apples.indices)
            canvas.drawRect(gameFieldData.apples[i].rect, paint)

        paint.color = snakeColor
        for (s in gameFieldData.snake)
            canvas.drawRect(s.rect, paint)

    }

    private fun Snake.move(index: Int) {
        val last = index == gameFieldData.snake.lastIndex
        when (direction) {
            DIR_TOP -> moveTop(last, gameFieldData.height)
            DIR_RIGHT -> moveRight(last, gameFieldData.width)
            DIR_BOTTOM -> moveBottom(last, gameFieldData.height)
            DIR_LEFT -> moveLeft(last, gameFieldData.width)
        }
        if (turning) {
            turningProgress += STEP
            if (turningProgress >= SIZE + STEP) {
                onChangedDirection()
                if (index < gameFieldData.snake.size - 1)
                    gameFieldData.snake[index + 1].changeDirection(direction)
            }
        }
    }

    fun changeDirection(dir: Int) {
        CoroutineScope(Dispatchers.Default).launch {
            with (gameFieldData.snake[0]) {
                if (direction == dir) return@launch
                when (direction) {
                    DIR_TOP -> if (dir == DIR_BOTTOM) return@launch
                    DIR_RIGHT -> if (dir == DIR_LEFT) return@launch
                    DIR_BOTTOM -> if (dir == DIR_TOP) return@launch
                    DIR_LEFT -> if (dir == DIR_RIGHT) return@launch
                }
                while (turning || isTransition()) {
                    delay(10)
                }
                //with (rect) { gameFieldData.turns.add(Plug(Rect(left, top, right, bottom), 0)) }
                changeDirection(dir)
            }
        } //val changeDirectionJob: Job =
    }

    private fun checkAppleCollision() {
        for (apple in gameFieldData.apples) {
            if (apple.rect.intersect(gameFieldData.snake[0].rect)) {
                gameFieldData.appleEaten(apple)
                break
            }
        }
    }

    private fun checkSnakeCollision() {
        if (!gameFieldData.selfEating) {
            var a: Int? = null

            for (i in 4 until gameFieldData.snake.size) {
                val s = gameFieldData.snake[i]

                with (gameFieldData.snake[0].rect) {
                    val b1 = s.rect.intersect(left, top, right, bottom)
                    if (b1) a = i
                }
                if (a != null) break
            }
            if (a != null) gameFieldData.snakeEaten(a!!)
        }
    }

}