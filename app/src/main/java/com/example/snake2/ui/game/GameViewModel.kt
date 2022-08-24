package com.example.snake2.ui.game

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.snake2.models.Snake
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GameViewModel(application: Application) : AndroidViewModel(application) {
    val liveData: MutableLiveData<GameFieldData> = MutableLiveData()
    val data: GameFieldData = GameFieldData(1080, 2160)
    val thread: GameThread = GameThread(this)

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
        liveData.value = data

        prepareGame()
        startGame()
    }

    fun prepareGame() {
        repeat(3) {
            data.addApple()
        }
    }

    fun startGame() {
        thread.setRunning(true)
        thread.start()
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

    fun updateGame() {
        data.snake.forEachIndexed { index, snake ->
            snake.move(index)
        }
        checkSnakeCollision()
        checkAppleCollision()
        liveData.postValue(data)
    }

    private fun Snake.move(index: Int) {
        val last = index == data.snake.lastIndex
        when (direction) {
            DIR_TOP -> moveTop(last, data.height)
            DIR_RIGHT -> moveRight(last, data.width)
            DIR_BOTTOM -> moveBottom(last, data.height)
            DIR_LEFT -> moveLeft(last, data.width)
        }
        if (turning) {
            turningProgress += GameFieldData.STEP
            if (turningProgress >= GameFieldData.SIZE + GameFieldData.STEP) {
                onChangedDirection()
                if (index < data.snake.size - 1)
                    data.snake[index + 1].changeDirection(direction)
            }
        }
    }

    fun changeDirection(dir: Int) {
        CoroutineScope(Dispatchers.Default).launch {
            with (data.snake[0]) {
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
        for (apple in data.apples) {
            if (apple.rect.intersect(data.snake[0].rect)) {
                data.appleEaten(apple)
                break
            }
        }
    }

    private fun checkSnakeCollision() {
        var a: Int? = null

        for (i in 4 until data.snake.size) {
            val s = data.snake[i]

            with (data.snake[0].rect) {
                val b1 = s.rect.intersect(left, top, right, bottom)
                if (b1) a = i
            }
            if (a != null) break
        }
        if (a != null) data.snakeEaten(a!!)
    }


}