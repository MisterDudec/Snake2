package com.example.snake2.threads

import android.os.SystemClock
import android.util.Log
import com.example.domain.config.ADD_APPLE_PERIOD_DEFAULT
import com.example.domain.gamestate.GameState
import com.example.domain.gamestate.GameStateController
import com.example.domain.usecase.Game
import com.example.snake2.activity.GameViewModel


/**
 * Created by Sex_predator on 27.03.2016.
 */
open class GameThread(private val viewModel: GameViewModel, name: String) : AbstractThread(name) {

    private val getTickCount: Long get() = SystemClock.uptimeMillis()

    override fun run() { //var interpolation: Float
        var counter = 0
        var nextGameTick = getTickCount

        while (viewModel.running) {
            var loops = 0

            //Log.d(this.name, "updating")

            //Log.v("$GameThread", "$getTickCount > $nextGameTick && $loops < $maxFrameSkip")
            while(getTickCount > nextGameTick && loops < maxFrameSkip) {
                val timeToAddApple = counter / 300000000 > ADD_APPLE_PERIOD_DEFAULT
                if (timeToAddApple) counter = 0

                //Log.d("$GameThread", "time = ${counter / 100000}, bool = $timeToAddApple")

                viewModel.updateGame(timeToAddApple)
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