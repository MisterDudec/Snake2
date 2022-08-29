package com.example.snake2.activity

import android.os.SystemClock
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

    private val drawingThread = DrawingThread(this)

    class DrawingThread(private val viewModel: GameViewModel) : Thread() {
        private val maxFrameSkip = 5
        private var ticksPerSecond = GameThread.TICKS_60
        private var skipTicks = 1000 / ticksPerSecond

        private val getTickCount: Long get() = SystemClock.uptimeMillis()

        override fun run() {
            var nextGameTick = getTickCount
            while (viewModel.thread.running) {
                var loops = 0
                while(getTickCount > nextGameTick && loops < maxFrameSkip) {
                    viewModel.drawFrame()
                    nextGameTick += skipTicks
                    loops++
                }
            }
        }
    }

    private var viewModelObserver: ViewModelObserver? = null

    fun startGame()  {
        thread.setRunning(true)
        thread.start()
        drawingThread.start()
    } //activity

    fun setObserver(observer: ViewModelObserver) {
        viewModelObserver = observer
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
        //_liveData.postValue(game.model)
    } //activity

    fun restartGame() {
        game.restartGame()
    } //gameOverFragment

}