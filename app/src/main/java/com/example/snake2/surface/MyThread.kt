package com.example.snake2.surface

import android.content.Context
import android.graphics.Canvas
import android.os.SystemClock
import android.util.Log
import android.view.SurfaceHolder
import com.example.snake2.GameFieldData
import com.example.snake2.Presenter

/**
 * Created by Sex_predator on 27.03.2016.
 */
class MyThread(private val holder: SurfaceHolder) : Thread() {
    private var running = false
    private val maxFrameSkip = 5
    private val ticksPerSecond = 60
    private val skipTicks = 1000 / ticksPerSecond

    private val getTickCount: Long get() = SystemClock.uptimeMillis()

    fun setRunning(running: Boolean) {
        this.running = running
    }


    override fun run() {
        var canvas: Canvas?
        var nextGameTick = getTickCount
        var interpolation: Float
        val presenter = Presenter()

        var drawLoops = 0
        while (running) {
            var loops = 0
            while(getTickCount > nextGameTick && loops < maxFrameSkip) {
                presenter.updateGame() //!!!
                nextGameTick += skipTicks
                Log.d("run", "loops: $loops")
                loops++
            }

            interpolation = (getTickCount + skipTicks - nextGameTick).toFloat() // float( SKIP_TICKS );
            canvas = null
            try {
                canvas = holder.lockCanvas() //получаем canvas
                synchronized(holder) {
                    presenter.drawFrame(canvas) //функция рисования //drawFrame(interpolation)
                    Log.d("run", "draw: $drawLoops")
                    drawLoops++
                }
            } catch (e: NullPointerException) {
                e.printStackTrace() //если canvas не доступен
            } finally {
                if (canvas != null) holder.unlockCanvasAndPost(canvas) //освобождаем canvas
            }
        }
    }

    /*override fun run() {
        var canvas: Canvas?
        startTime = time
        while (running) {
            val curTime = time
            val elapsedTime = curTime - prevRedrawTime
            if (elapsedTime < redrawTime) continue //проверяет, прошло ли 10 мс
            canvas = null
            try {
                canvas = mSurfaceHolder.lockCanvas() //получаем canvas
                synchronized(mSurfaceHolder) {
                    drawFrame(canvas) //функция рисования
                }
            } catch (e: NullPointerException) {
                e.printStackTrace()*//*если canvas не доступен*//*
            } finally {
                if (canvas != null) mSurfaceHolder.unlockCanvasAndPost(canvas) //освобождаем canvas
            }
            prevRedrawTime = curTime
        }
    }*/

    /*private fun draw(canvas: Canvas) {
        //canvas.drawRect
        val curTime = time - startTime
        canvas.drawColor(Color.BLACK)
        val width = canvas.width //1080
        val height = canvas.height //1904

        val centerX = width / 2
        val centerY = height / 2
        val maxSize = (min(width, height) / 2).toFloat()
        val fraction = (curTime % animationTime).toFloat() / animationTime
        val color = argbEvaluator.evaluate(fraction, Color.RED, Color.BLACK) as Int
        paint.color = color
        canvas.drawCircle(centerX.toFloat(), centerY.toFloat(), maxSize * fraction, paint)
    }*/

    fun dip2px(context: Context, dpValue: Float): Int {
        val scale: Float = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }
}