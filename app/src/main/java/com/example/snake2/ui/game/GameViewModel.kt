package com.example.snake2.ui.game

import androidx.lifecycle.*
import com.example.snake2.models.Snake
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class GameViewModel() : ViewModel() {
    //val liveData: MutableLiveData<GameModel> = MutableLiveData() //TODO liveDataGet: LiveData<GameFieldData> = liveData
    private val model: GameModel = GameModel()
    private val thread: GameThread = GameThread(this)

    private val _modelFlow = MutableSharedFlow<GameModel>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val modelFlow get() = _modelFlow

    private var pause = true

    companion object {
        const val DIR_STOP = -1
        const val DIR_TOP = 0
        const val DIR_RIGHT = 1
        const val DIR_BOTTOM = 2
        const val DIR_LEFT = 3
        const val DIR_DEFAULT = DIR_TOP
        //var direction = DIR_TOP
    }

    fun gameOver() {

    }

    fun configure(width: Int, height: Int) {
        model.setDimensions(width, height)
    }

    fun startGame()  {
        thread.setRunning(true)
        thread.start()
    }

    fun updateGame() {
        model.snake.forEachIndexed { index, snake ->
            snake.move(index)
        }
        checkSnakeCollision()
        checkAppleCollision()
        _modelFlow.tryEmit(model)
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
        var a: Int? = null

        for (i in 4 until model.snake.size) {
            val s = model.snake[i]

            with (model.snake[0].rect) {
                val b1 = s.rect.intersect(left, top, right, bottom)
                if (b1) a = i
            }
            if (a != null) break
        }
        if (a != null) {
            if (model.snakeEaten(a!!)) {
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
                    delay(10)
                }
                //with (rect) { gameFieldData.turns.add(Plug(Rect(left, top, right, bottom), 0)) }
                changeDirection(dir)
            }
        } //val changeDirectionJob: Job =
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