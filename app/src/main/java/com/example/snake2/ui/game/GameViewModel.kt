package com.example.snake2.ui.game

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.snake2.models.Snake
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GameViewModel() : ViewModel() {
    val liveData: MutableLiveData<GameModel> = MutableLiveData() //TODO liveDataGet: LiveData<GameFieldData> = liveData
    private val model: GameModel = GameModel()
    private val thread: GameThread = GameThread(this)

    var pause = true

    companion object {
        const val DIR_STOP = -1
        const val DIR_TOP = 0
        const val DIR_RIGHT = 1
        const val DIR_BOTTOM = 2
        const val DIR_LEFT = 3
        const val DIR_DEFAULT = DIR_TOP
        //var direction = DIR_TOP
    }

    fun setDimensions(width: Int, height: Int) {
        model.setDimensions(width, height)
        liveData.value = model
    }

    fun startGame()  {
        thread.setRunning(true)
        thread.start()
    }

    fun gameOver() {
        pauseGame()
        model.gameOver()
    }

    fun restartGame() {
        model.restartGame()
        resumeGame()
    }

    fun updateGame(timeToAddApples: Boolean) {
        if (timeToAddApples) model.addApple()
        model.snake.forEachIndexed { index, snake ->
            snake.move(index)
        }
        checkSnakeCollision()
        checkAppleCollision()
    }

    fun drawFrame() {
        liveData.postValue(model)
    }

    private fun Snake.move(index: Int) {
        val last = index == model.snake.lastIndex
        when (direction) {
            DIR_TOP -> moveTop(last, model.height)
            DIR_RIGHT -> moveRight(last, model.width)
            DIR_BOTTOM -> moveBottom(last, model.height)
            DIR_LEFT -> moveLeft(last, model.width)
        }
        if (turning) {
            turningProgress += GameModel.STEP
            if (turningProgress >= GameModel.SIZE + GameModel.STEP) {
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

    fun changeDirection(dir: Int) {
        CoroutineScope(Dispatchers.Default).launch {
            with (model.snake[0]) {
                if (direction == dir) return@launch
                when (direction) {
                    DIR_TOP -> if (dir == DIR_BOTTOM) return@launch
                    DIR_RIGHT -> if (dir == DIR_LEFT) return@launch
                    DIR_BOTTOM -> if (dir == DIR_TOP) return@launch
                    DIR_LEFT -> if (dir == DIR_RIGHT) return@launch
                }
                while (turning || isTransition()) {
                    delay(1)
                }
                changeDirection(dir)
            }
        }
    }

    fun resumeGame() {
        pause = false
    }

    fun pauseGame() {
        pause = true
    }

    fun isPaused() : Boolean {
        return pause
    }
}