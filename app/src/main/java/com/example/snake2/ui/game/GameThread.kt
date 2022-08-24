package com.example.snake2.ui.game

import android.os.SystemClock
import android.util.Log

/**
 * Created by Sex_predator on 27.03.2016.
 */
class GameThread(private val viewModel: GameViewModel) : Thread() {
    private var running = false
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

    override fun run() {
        var nextGameTick = getTickCount
        while (running) {
            var loops = 0

            Log.v("$GameThread", "$getTickCount > $nextGameTick && $loops < $maxFrameSkip")
            while(getTickCount > nextGameTick && loops < maxFrameSkip) {
                viewModel.updateGame()
                nextGameTick += skipTicks
                Log.v("$this/run", "updateGame: $loops")
                loops++
            }

            while (viewModel.isPaused()) {
                sleep(skipTicks.toLong())
                nextGameTick = getTickCount
                loops++
            }
        }
    }
}