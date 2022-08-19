package com.example.snake2.surface

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.Button
import androidx.core.view.GestureDetectorCompat
import com.example.snake2.GameFieldData
import com.example.snake2.MyGestureListener
import com.example.snake2.Presenter


class MySurfaceView(context: Context, attrs: AttributeSet?, defStyle: Int) :
    SurfaceView(context, attrs, defStyle), SurfaceHolder.Callback {
    private var mMyThread: MyThread? = null
    private val gestureListener = GestureDetectorCompat(this.context, MyGestureListener())


    constructor(context: Context) : this(context, null, 0)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    init {
        holder.addCallback(this)
    }

    override fun surfaceCreated(holder: SurfaceHolder) { //вызывается, когда surfaceView появляется на экране
        //запускает процесс в отдельном потоке
    }

    fun startGame(presenter: Presenter) {
        setDimensions()
        mMyThread = MyThread(holder, presenter)
        mMyThread!!.setRunning(true)
        mMyThread!!.start()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        setDimensions()
        //когда view меняет свой размер
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) { //когда view исчезает из поля зрения
        var retry = true
        mMyThread?.setRunning(false) //останавливает процесс
        while (retry) {
            try {
                mMyThread?.join() //ждет окончательной остановки процесса
                retry = false
            } catch (e: InterruptedException) {
                e.printStackTrace()//не более чем формальность
            }
        }
    }

    private fun setDimensions () {
        var canvas: Canvas? = null
        try {
            canvas = holder.lockCanvas() //получаем canvas
            synchronized(holder) {
                GameFieldData.SCREEN_WIDTH = canvas.width
                GameFieldData.SCREEN_HEIGHT = canvas.height
            }
        } catch (e: NullPointerException) {
            e.printStackTrace() //если canvas не доступен
        } finally {
            if (canvas != null) holder.unlockCanvasAndPost(canvas) //освобождаем canvas
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        gestureListener.onTouchEvent(event)
        performClick()
        return super.onTouchEvent(event)
    }

    override fun performClick(): Boolean {
        super.performClick()
        //doSomething()
        return true
    }
}