package com.example.snake2.activity

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.domain.config.Direction
import com.example.domain.models.Counter
import com.example.domain.usecase.Game
import com.example.domain.models.Field
import com.example.snake2.threads.DrawingThread
import com.example.snake2.threads.GameThread

class GameViewModel : ViewModel() {
    private val _liveData: MutableLiveData<Field> = MutableLiveData()
    val liveData: LiveData<Field> get() = _liveData

    private val _counterLiveData: MutableLiveData<Counter> = MutableLiveData()
    val counterLiveData: LiveData<Counter> get() = _counterLiveData

    private val game: Game = Game()

    private val gameThread = GameThread(this, "GameThread")
    private val drawingThread = DrawingThread(this, "DrawingThread")
    var running = false

    private var viewModelObserver: ViewModelObserver? = null

    fun startThreads()  {
        if (!running) {
            running = true
            gameThread.start()
            drawingThread.start()
        }
    } //activity

    fun joinThreads() {
        if (running) {
            running = false
            gameThread.join()
            drawingThread.join()
        }
    }

    fun registerViewModelObserver(observer: ViewModelObserver) {
        viewModelObserver = observer
    }

    fun unregisterViewModelObserver() {
        viewModelObserver = null
    }

    @MainThread
    fun setDimensions(width: Int, height: Int) {
        _liveData.value = game.setDimensions(width, height)
    } //activity

    fun changeDirection(direction: Direction) {
        game.changeDirection(direction)
    } //gameFragment

    fun drawFrame() {
        viewModelObserver?.draw(game.model)
        _counterLiveData.postValue(game.model.counter)
        //_liveData.postValue(game.model)
    } //activity

    fun updateGame(timeToAddApples: Boolean) {
        game.updateGame(timeToAddApples)
    }

    fun restartGame() {
        game.restartGame()
    } //gameOverFragment

}