package com.example.snake2

import android.annotation.SuppressLint
import android.graphics.Canvas
import android.os.Build
import android.view.SurfaceHolder
import androidx.annotation.RequiresApi

class SurfaceThread(val surfaceHolder: SurfaceHolder, val surfaceView: GameSurfaceView): Thread() {
    private var myThreadRun = false

    fun setRunning(myThreadRun: Boolean){
        this.myThreadRun = myThreadRun
    }

    @SuppressLint("WrongCall")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun run() {
        this.surfaceHolder
        while (myThreadRun) {
            var canvas: Canvas? = null
            try {
                canvas = surfaceHolder.lockCanvas(null)
                synchronized(surfaceHolder) {
                    surfaceView.onDraw(canvas)
                }
                sleep(10)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            } finally {
                if (canvas != null) surfaceHolder.unlockCanvasAndPost(canvas)
            }
        }
    }
}