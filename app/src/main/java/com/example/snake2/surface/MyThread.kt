package com.example.snake2.surface

import android.content.Context
import android.graphics.Canvas
import android.os.SystemClock
import android.util.Log
import android.view.SurfaceHolder
import com.example.snake2.Presenter
import com.example.snake2.data.GameFieldData

/**
 * Created by Sex_predator on 27.03.2016.
 */
class MyThread(private val holder: SurfaceHolder, private val presenter: Presenter) : Thread() {
    private var running = false
    private val maxFrameSkip = 5
    private val ticksPerSecond = 30
    private val skipTicks = 1000 / ticksPerSecond

    private val getTickCount: Long get() = SystemClock.uptimeMillis()

    fun setRunning(running: Boolean) {
        this.running = running
    }

    override fun run() {
        var canvas: Canvas?
        var nextGameTick = getTickCount
        var interpolation: Float
        presenter.startGame()

        var drawLoops = 0
        while (running) {
            var loops = 0
            while (presenter.isPaused() && getTickCount > nextGameTick && loops < maxFrameSkip) {
                nextGameTick += skipTicks
                loops++
            }

            while(getTickCount > nextGameTick && loops < maxFrameSkip) {
                presenter.updateGame()
                nextGameTick += skipTicks
                Log.v("$this/run", "loops: $loops")
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
        }
    }

}