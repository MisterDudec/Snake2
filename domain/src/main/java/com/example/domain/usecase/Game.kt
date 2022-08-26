package com.example.domain.usecase

import com.example.domain.config.*
import com.example.domain.gamestate.GameStateController
import com.example.domain.models.GameModel
import com.example.domain.models.Snake
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Game {
    val model: GameModel = GameModel()

    //var pause = true

    fun setDimensions(width: Int, height: Int): GameModel {
        model.setDimensions(width, height)
        return model
    }

    fun updateGame(timeToAddApples: Boolean) {
        if (timeToAddApples) model.addApple()
        model.snake.forEachIndexed { index, snake ->
            snake.move(index)
        }
        checkSnakeCollision()
        checkAppleCollision()
    }

    fun gameOver() {

        GameStateController.gameOver()
        model.gameOver()
    }

    fun restartGame() {
        model.restartGame()
        GameStateController.resumeGame()
    }

    private fun Snake.move(index: Int) {
        val last = index == model.snake.lastIndex
        when (direction) {
            Direction.Top -> moveTop(last, model.height)
            Direction.Right -> moveRight(last, model.width)
            Direction.Bottom -> moveBottom(last, model.height)
            Direction.Left -> moveLeft(last, model.width)
        }
        if (turning) {
            turningProgress += SNAKE_STEP

            if (turningProgress >= SNAKE_SIZE + SNAKE_STEP) {
                onChangedDirection()
                if (index < model.snake.size - 1)
                    model.snake[index + 1].changeDirection(direction)
            }
        }
    }

    private fun checkAppleCollision() {
        for (apple in model.apples) {
            if (apple.rect.intersect(model.snake[0].rect)) {
                model.appleEaten(apple)
                break
            }
        }
    }

    private fun checkSnakeCollision() {
        var eatenRect: Int? = null

        for (i in 4 until model.snake.size) {
            val s = model.snake[i]

            with (model.snake[0].rect) {
                val b1 = s.rect.intersect(left, top, right, bottom)
                if (b1) eatenRect = i
            }
            if (eatenRect != null) break
        }
        if (eatenRect != null) {
            if (model.snakeEaten(eatenRect!!)) {
                gameOver()
            }
        }
    }

    fun changeDirection(dir: Direction) {
        CoroutineScope(Dispatchers.Default).launch {
            with (model.snake[0]) {
                if (direction == dir) return@launch
                when (direction) {
                    Direction.Top -> if (dir == Direction.Bottom) return@launch
                    Direction.Right -> if (dir == Direction.Left) return@launch
                    Direction.Bottom -> if (dir == Direction.Top) return@launch
                    Direction.Left -> if (dir == Direction.Right) return@launch
                }
                while (turning || isTransition()) {
                    delay(1)
                }
                changeDirection(dir)
            }
        }
    }


}