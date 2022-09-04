package com.example.snake2.threads

import android.os.SystemClock
import android.util.Log
import com.example.snake2.activity.GameViewModel


class DrawingThread(private val viewModel: GameViewModel, name: String) : AbstractThread(name) {

    override fun run() {
        var nextGameTick = getTickCount
        while (viewModel.running) {
            var loops = 0
            while(getTickCount > nextGameTick && loops < maxFrameSkip) {
                viewModel.drawFrame()
                nextGameTick += skipTicks
                loops++
            }
        }
    }
}