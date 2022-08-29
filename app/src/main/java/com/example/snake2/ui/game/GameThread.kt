package com.example.snake2.ui.game

import android.os.SystemClock
import android.util.Log
import com.example.domain.config.ADD_APPLE_PERIOD
import com.example.domain.gamestate.GameState
import com.example.domain.gamestate.GameStateController
import com.example.domain.usecase.Game
import com.example.snake2.activity.GameViewModel


/**
 * Created by Sex_predator on 27.03.2016.
 */
open class GameThread(private val game: Game, private val viewModel: GameViewModel) : Thread() {
    internal var running = false
    private val maxFrameSkip = 5
    private var ticksPerSecond = TICKS_60
    private var skipTicks = 1000 / ticksPerSecond

    private val getTickCount: Long get() = SystemClock.uptimeMillis()

    companion object {
        const val TICKS_10 = 10
        const val TICKS_60 = 60
        const val TICKS_120 = 120
    }

    fun setRunning(running: Boolean) {
        this.running = running
    }

    fun changeTicks() {
        ticksPerSecond = when (ticksPerSecond) {
            //TICKS_30 -> TICKS_120
            TICKS_60 -> TICKS_10
            else -> TICKS_60
        }
        skipTicks = 1000 / ticksPerSecond
    }

    override fun run() { //var interpolation: Float
        var counter = 0
        var nextGameTick = getTickCount

        while (running) {
            var loops = 0


            //Log.v("$GameThread", "$getTickCount > $nextGameTick && $loops < $maxFrameSkip")
            while(getTickCount > nextGameTick && loops < maxFrameSkip) {
                val timeToAddApple = counter / 300000000 > ADD_APPLE_PERIOD
                if (timeToAddApple) counter = 0

                //Log.d("$GameThread", "time = ${counter / 100000}, bool = $timeToAddApple")

                game.updateGame(timeToAddApple)
                nextGameTick += skipTicks

                //Log.v("$this/run", "updateGame: $loops")
                loops++
            }

            //interpolation = (getTickCount + skipTicks - nextGameTick).toFloat() // float( SKIP_TICKS );
            //viewModel.drawFrame()

            while (GameStateController.gameState !is GameState.Play) {
                sleep(skipTicks.toLong())
                nextGameTick = getTickCount
                loops++
            }
            counter += skipTicks
        }
    }
}