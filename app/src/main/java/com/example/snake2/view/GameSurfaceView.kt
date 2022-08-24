package com.example.snake2.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.example.snake2.R
import com.example.snake2.ui.game.GameFieldData
import com.example.snake2.ui.game.GameThread


class GameSurfaceView(context: Context, attrs: AttributeSet?, defStyle: Int) :
    SurfaceView(context, attrs, defStyle), SurfaceHolder.Callback {
    var thread: GameThread? = null
    val paint = Paint()
    private val backgroundColor: Int
    private var snakeColor: Int
    private var appleColor: Int

    //private val gestureListener = GestureDetectorCompat(this.context, MyGestureListener())

    constructor(context: Context) : this(context, null, 0)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    init {
        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL
        backgroundColor = context.getColor(R.color.background_surface_view)
        snakeColor = context.getColor(R.color.snake)
        appleColor = context.getColor(R.color.apple)
        holder.addCallback(this)
    }



    override fun surfaceCreated(holder: SurfaceHolder) { //вызывается, когда surfaceView появляется на экране

    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {

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

    fun drawFrame(gameFieldData: GameFieldData) {
        var canvas: Canvas?
        canvas = null
        try {
            canvas = holder.lockCanvas() //получаем canvas
            synchronized(holder) {
                drawFrame(canvas, gameFieldData) //функция рисования //drawFrame(interpolation)
                Log.v("$this/run", "draw: ")
            }
        } catch (e: NullPointerException) {
            e.printStackTrace() //если canvas не доступен
        } finally {
            if (canvas != null) holder.unlockCanvasAndPost(canvas) //освобождаем canvas
        }
    }

    private fun drawFrame(canvas: Canvas, gameFieldData: GameFieldData) {
        canvas.drawColor(backgroundColor)

        paint.color = appleColor
        for (i in gameFieldData.apples.indices)
            canvas.drawRect(gameFieldData.apples[i].rect, paint)

        paint.color = snakeColor
        for (s in gameFieldData.snake)
            canvas.drawRect(s.rect, paint)
    }

    /*fun pause() {
        mRunning = false
        try {
            // Stop the thread (rejoin the main thread)
            mGameThread.join()
        } catch (e: InterruptedException) {
        }
    }

    fun resume() {
        mRunning = true
        mGameThread = Thread(this)
        mGameThread.start()
    }*/

   /* override fun onTouchEvent(event: MotionEvent?): Boolean {
        gestureListener.onTouchEvent(event)
        performClick()
        return super.onTouchEvent(event)
    }

    override fun performClick(): Boolean {
        super.performClick()
        //doSomething()
        return true
    }*/
}