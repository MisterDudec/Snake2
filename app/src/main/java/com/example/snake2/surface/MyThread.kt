package com.example.snake2.surface

import android.animation.ArgbEvaluator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.SurfaceHolder
import kotlin.math.min


/**
 * Created by Sex_predator on 27.03.2016.
 */
class MyThread(private val mSurfaceHolder: SurfaceHolder) : Thread() {
    private val redrawTime = 10 //частота обновления экрана - 10 мс
    private val animationTime = 1500 //анимация - 1,5 сек
    private var running = false //запущен ли процесс
    private var startTime: Long = 0 //время начала анимации
    private var prevRedrawTime: Long = 0 //предыдущее время перерисовки
    private val paint: Paint = Paint()
    private val argbEvaluator: ArgbEvaluator

    init {
        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL
        argbEvaluator = ArgbEvaluator()
    }

    fun setRunning(running: Boolean) { //запускает и останавливает процесс
        this.running = running
        prevRedrawTime = time
    }

    private val time: Long get() = System.nanoTime() / 1000000

    override fun run() {
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
                    draw(canvas) //функция рисования
                }
            } catch (e: NullPointerException) {
                e.printStackTrace()/*если canvas не доступен*/
            } finally {
                if (canvas != null) mSurfaceHolder.unlockCanvasAndPost(canvas) //освобождаем canvas
            }
            prevRedrawTime = curTime
        }
    }

    private fun draw(canvas: Canvas) {
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
    }

    private fun drawFrame(canvas: Canvas) {
        var rect = Rect()
        //rect.
    }

    fun dip2px(context: Context, dpValue: Float): Int {
        val scale: Float = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }
}