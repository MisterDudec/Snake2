package com.example.snake2.threads

import android.os.SystemClock
import android.util.Log
import com.example.snake2.activity.GameViewModel


class DrawingThread(private val viewModel: GameViewModel, name: String) : AbstractThread(name) {

    private val getTickCount: Long get() = SystemClock.uptimeMillis()

    override fun run() {
        var nextGameTick = getTickCount
        while (viewModel.running) {
            var loops = 0
            while(getTickCount > nextGameTick && loops < maxFrameSkip) {
                //Log.d(this.name, "drawing")
                viewModel.drawFrame()
                nextGameTick += skipTicks
                loops++
            }
        }
    }
}