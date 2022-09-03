package com.example.snake2.threads

abstract class AbstractThread(name: String) : Thread(name) {
    protected val maxFrameSkip = 5
    protected var ticksPerSecond = TICKS_60
    protected var skipTicks = 1000 / ticksPerSecond

    companion object {
        const val TICKS_10 = 10
        const val TICKS_60 = 60
        const val TICKS_120 = 120
    }

    fun changeTicks() {
        ticksPerSecond = when (ticksPerSecond) {
            //TICKS_30 -> TICKS_120
            TICKS_60 -> TICKS_10
            else -> TICKS_60
        }
        skipTicks = 1000 / ticksPerSecond
    }
}