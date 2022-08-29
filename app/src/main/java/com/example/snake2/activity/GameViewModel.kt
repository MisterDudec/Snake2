package com.example.snake2.activity

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.domain.config.Direction
import com.example.domain.usecase.Game
import com.example.domain.models.GameModel
import com.example.snake2.ui.game.GameThread

class GameViewModel : ViewModel() {
    private val _liveData: MutableLiveData<GameModel> = MutableLiveData()
    val liveData: LiveData<GameModel> get() = _liveData

    private val game: Game = Game()
    private val thread = GameThread(game, this)

    fun startGame()  {
        thread.setRunning(true)
        thread.start()
    } //activity

    @MainThread
    fun setDimensions(width: Int, height: Int) {
        _liveData.value = game.setDimensions(width, height)
    } //activity

    fun changeDirection(direction: Direction) {
        game.changeDirection(direction)
    }//gameFragment

    fun drawFrame() {
        _liveData.postValue(game.model)
    }//activity

    fun restartGame() {
        game.restartGame()
    }//gameOverFragment

}