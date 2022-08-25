package com.example.snake2.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import android.view.SurfaceView
import androidx.fragment.app.viewModels
import com.example.snake2.R
import com.example.snake2.ui.game.GameModel
import com.example.snake2.ui.game.GameViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable.join
import kotlinx.coroutines.launch


class GameSurfaceView(context: Context, attrs: AttributeSet?, defStyle: Int) :
    SurfaceView(context, attrs, defStyle) {
    val paint = Paint()
    private val backgroundColor = context.getColor(R.color.background_surface_view)
    private var snakeColor = context.getColor(R.color.snake)
    private var appleColor = context.getColor(R.color.apple)


    init {
        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL
    }

    suspend fun drawFrame(gameFieldData: GameModel) {
        Log.d("threads", "drawFrame: ${Looper.myLooper()}")
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

    private fun drawFrame(canvas: Canvas, gameFieldData: GameModel) {
        Log.d("threads", "drawFrame(): ${Looper.myLooper()}")
        if (canvas == null) return
        canvas.drawColor(backgroundColor)

        paint.color = appleColor
        for (i in gameFieldData.apples.indices)
            canvas.drawRect(gameFieldData.apples[i].rect, paint)

        //TODO resolve ConcurrentModificationException
        paint.color = snakeColor
        try {
            for (s in gameFieldData.snake)
                canvas.drawRect(s.rect, paint)
        } catch (e: ConcurrentModificationException) {
            e.printStackTrace()
        }


    }

    constructor(context: Context) : this(context, null, 0)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    //private val gestureListener = GestureDetectorCompat(this.context, MyGestureListener())

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