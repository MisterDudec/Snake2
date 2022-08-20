package com.example.snake2

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import com.example.snake2.data.Apple
import com.example.snake2.data.GameFieldData
import com.example.snake2.data.GameFieldData.Companion.SIZE
import com.example.snake2.data.GameFieldData.Companion.STEP
import com.example.snake2.data.Plug
import com.example.snake2.data.Snake
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Presenter(private val gameFieldData: GameFieldData) {
    private val logName = "Presenter"
    private val paint: Paint = Paint()
    private var pause = false

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
        canvas.drawColor(Color.BLACK)

        paint.color = Color.GREEN
        for (i in gameFieldData.apples.indices)
            canvas.drawRect(gameFieldData.apples[i].rect, paint)

        for (s in gameFieldData.snake)
            canvas.drawRect(s.rect, s.paint)

        paint.color = Color.RED
        //for (turn in gameFieldData.turns) canvas.drawRect(turn.rect, paint)

    }

    private fun Snake.move(index: Int) {
        if (turning) {
            turningProgress += STEP
            if (turningProgress >= SIZE + STEP) {
                turning = false
                gameFieldData.turns
                if (index != gameFieldData.snake.size - 1)
                    gameFieldData.snake[index + 1].changeDirection(direction)
            }
            if (index == gameFieldData.snake.size - 1) {
                turning = false
                with (gameFieldData.turns) { removeAt(0) }
            }
        }
        when (direction) {
            DIR_TOP -> moveTop()
            DIR_RIGHT -> moveRight()
            DIR_BOTTOM -> moveBottom()
            DIR_LEFT -> moveLeft()
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
                while (turning) {
                    delay(1)
                }
                with (rect) { gameFieldData.turns.add(Plug(Rect(left, top, right, bottom), 0)) }

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
                    if (b1) {
                        a = i
                    }
                }
                if (a != null) {
                    gameFieldData.snakeEaten(a!!)
                    break
                }
            }
        }
    }

    /*private fun checkSnakeCollisionOld() {
        var a: Snake? = null

        for (i in 1 until gameFieldData.snake.size) {

        }
        for (snake in gameFieldData.snake) {
            val rangeX = IntRange(snake.rect.left, snake.rect.right)
            val rangeY = IntRange(snake.rect.top, snake.rect.bottom)

            with (gameFieldData.snake[0].rect) {

                val b1 = rangeX.contains(centerX() - SIZE / 2) && rangeY.contains(centerY() - SIZE / 2)
                val b2 = rangeX.contains(centerX() + SIZE / 2) && rangeY.contains(centerY() + SIZE / 2)
                if (b1 || b2) {
                    a = snake
                }
            }
        }
        if (a != null) gameFieldData.snakeEaten(a!!)
    }*/

    private fun Snake.moveTop() {
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

    private fun Snake.moveRight() {
        if (rect.left <= gameFieldData.width) {
            rect.right += STEP
            rect.left += STEP
        } else {
            rect.left = 0 - SIZE
            rect.right = 0
        }
    }

    private fun Snake.moveBottom() {
        if (rect.top <= gameFieldData.height) {
            rect.top += STEP
            rect.bottom += STEP
        } else {
            rect.top = 0 - SIZE
            rect.bottom = 0
        }
    }

    private fun Snake.moveLeft() {
        if (rect.right >= 0) {
            rect.right -= STEP
            rect.left -= STEP
        } else {
            rect.left = gameFieldData.width
            rect.right = gameFieldData.width + SIZE
        }
    }

    private fun checkAppleCollision1() {
        var a: Apple? = null
        for (apple in gameFieldData.apples) {
            val rangeX = IntRange(apple.rect.left, apple.rect.right)
            val rangeY = IntRange(apple.rect.top, apple.rect.bottom)

            with (gameFieldData.snake[0].rect) {

                val b1 = rangeX.contains(centerX() - SIZE / 2) && rangeY.contains(centerY() - SIZE / 2)
                val b2 = rangeX.contains(centerX() + SIZE / 2) && rangeY.contains(centerY() + SIZE / 2)
                if (b1 || b2) {
                    a = apple
                }
            }
        }
        if (a != null) gameFieldData.appleEaten(a!!)
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