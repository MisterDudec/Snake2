package com.example.snake2.androidersurfaceview

import android.graphics.Canvas
import android.os.Build
import android.view.SurfaceHolder
import androidx.annotation.RequiresApi

class AndroidErSurfaceThread(val surfaceHolder: SurfaceHolder, val surfaceView: AndroidErSurfaceView): Thread() {
    private var running = false

    fun setRunning(running: Boolean){
        this.running = running
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun run() {
        this.surfaceHolder
        var canvas: Canvas? = null
        while (running) {
            try {
                canvas = surfaceHolder.lockCanvas(null)
                synchronized(surfaceHolder) {
                    //surfaceView.draw(canvas)
                    surfaceView.onDraw(canvas)
                }
                sleep(30)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            } finally {
                if (canvas != null) surfaceHolder.unlockCanvasAndPost(canvas)
            }
        }
    }

    /*override fun run() {
        var canvas: Canvas?
        while (runFlag) {
            // получаем текущее время и вычисляем разницу с предыдущим
            // сохраненным моментом времени
            val now = System.currentTimeMillis()
            val elapsedTime: Long = now - prevTime
            if (elapsedTime > 30) {
                // если прошло больше 30 миллисекунд - сохраним текущее время
                // и повернем картинку на 2 градуса.
                // точка вращения - центр картинки
                prevTime = now
                matrix.preRotate(2.0f, picture.getWidth() / 2, picture.getHeight() / 2)
            }
            canvas = null
            try {
                // получаем объект Canvas и выполняем отрисовку
                canvas = surfaceHolder.lockCanvas(null)
                synchronized(surfaceHolder) {
                    canvas.drawColor(Color.BLACK)
                    canvas.drawBitmap(picture, matrix, null)
                }
            } finally {
                if (canvas != null) {
                    // отрисовка выполнена. выводим результат на экран
                    surfaceHolder.unlockCanvasAndPost(canvas)
                }
            }
        }
    }*/
}