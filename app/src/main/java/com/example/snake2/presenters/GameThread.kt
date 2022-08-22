package com.example.snake2.presenters

import android.graphics.Canvas
import android.os.SystemClock
import android.util.Log
import android.view.SurfaceHolder

/**
 * Created by Sex_predator on 27.03.2016.
 */
class GameThread(private val holder: SurfaceHolder, private val presenter: Presenter) : Thread() {
    private var running = false
    private val maxFrameSkip = 5
    private var ticksPerSecond = TICKS_30
    private var skipTicks = 1000 / ticksPerSecond

    private val getTickCount: Long get() = SystemClock.uptimeMillis()

    companion object {
        const val TICKS_5 = 5
        const val TICKS_30 = 30
        const val TICKS_120 = 120
    }

    fun setRunning(running: Boolean) {
        this.running = running
    }

    fun changeTicks() {
        ticksPerSecond = when (ticksPerSecond) {
            //TICKS_30 -> TICKS_120
            TICKS_30 -> TICKS_5
            else -> TICKS_30
        }
        skipTicks = 1000 / ticksPerSecond
    }

    override fun run() {
        presenter.startGame()
        runOneTime()
        var canvas: Canvas?
        var nextGameTick = getTickCount
        var interpolation: Float

        var drawLoops = 0
        var loops = 0
        while (running) {
            if (presenter.isPaused()) {
                sleep(skipTicks.toLong())
                nextGameTick = getTickCount
                continue
            }

            Log.d("$GameThread", "$getTickCount > $nextGameTick && $loops < $maxFrameSkip")
            while(getTickCount > nextGameTick && loops < maxFrameSkip) {
                presenter.updateGame()
                nextGameTick += skipTicks
                Log.v("$this/run", "updateGame: $loops")
                loops++
            }

            interpolation = (getTickCount + skipTicks - nextGameTick).toFloat() // float( SKIP_TICKS );
            canvas = null
            try {
                canvas = holder.lockCanvas() //получаем canvas
                synchronized(holder) {
                    presenter.drawFrame(canvas) //функция рисования //drawFrame(interpolation)
                    Log.v("$this/run", "draw: $drawLoops")
                    drawLoops++
                }
            } catch (e: NullPointerException) {
                e.printStackTrace() //если canvas не доступен
            } finally {
                if (canvas != null) holder.unlockCanvasAndPost(canvas) //освобождаем canvas
            }
            loops = 0
        }
    }

    private fun runOneTime() {
        presenter.updateGame()
        var canvas: Canvas?
        canvas = null
        try {
            canvas = holder.lockCanvas() //получаем canvas
            synchronized(holder) {
                presenter.drawFrame(canvas) //функция рисования //drawFrame(interpolation)
                Log.v("$this/run", "draw First")
            }
        } catch (e: NullPointerException) {
            e.printStackTrace() //если canvas не доступен
        } finally {
            if (canvas != null) holder.unlockCanvasAndPost(canvas) //освобождаем canvas
        }
    }
}