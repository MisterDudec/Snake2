package com.example.snake2.ui.game

import androidx.annotation.MainThread
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.domain.config.Direction
import com.example.domain.usecase.Game
import com.example.domain.models.GameModel
import com.example.domain.models.Snake

class GameViewModel : ViewModel() {
    val liveData: MutableLiveData<GameModel> = MutableLiveData() //TODO liveDataGet: LiveData<GameFieldData> = liveData
    private val game: Game = Game()
    private val thread = GameThread(game, this)

    fun startGame()  {
        thread.setRunning(true)
        thread.start()
    }

    @MainThread
    fun setDimensions(width: Int, height: Int) {
        liveData.value = game.setDimensions(width, height)
    }

    fun changeDirection(direction: Direction) {
        game.changeDirection(direction)
    }

    fun drawFrame() {
        liveData.postValue(game.model)
    }

    fun restartGame() {
        game.restartGame()
    }

}