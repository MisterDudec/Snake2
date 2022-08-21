package com.example.snake2.surface

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.core.view.GestureDetectorCompat
import com.example.snake2.MyGestureListener
import com.example.snake2.Presenter


class GameSurfaceView(context: Context, attrs: AttributeSet?, defStyle: Int) :
    SurfaceView(context, attrs, defStyle), SurfaceHolder.Callback {
    var thread: GameThread? = null

    private val gestureListener = GestureDetectorCompat(this.context, MyGestureListener())


    constructor(context: Context) : this(context, null, 0)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    init {
        holder.addCallback(this)
    }

    override fun surfaceCreated(holder: SurfaceHolder) { //вызывается, когда surfaceView появляется на экране

    }

    fun startGame(presenter: Presenter) {
        thread = GameThread(holder, presenter)
        thread!!.setRunning(true)
        thread!!.start()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        //GameFieldData.setDimensions(holder)
        //когда view меняет свой размер
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) { //когда view исчезает из поля зрения
        var retry = true
        thread?.setRunning(false) //останавливает процесс
        while (retry) {
            try {
                thread?.join() //ждет окончательной остановки процесса
                retry = false
            } catch (e: InterruptedException) {
                e.printStackTrace()//не более чем формальность
            }
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