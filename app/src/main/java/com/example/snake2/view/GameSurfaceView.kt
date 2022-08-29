package com.example.snake2.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.SurfaceView
import com.example.domain.config.Direction
import com.example.domain.config.SNAKE_SIZE
import com.example.domain.models.GameModel
import com.example.domain.models.Snake
import com.example.snake2.R
import com.example.snake2.activity.ViewModelObserver


class GameSurfaceView(context: Context, attrs: AttributeSet?, defStyle: Int) :
    SurfaceView(context, attrs, defStyle), ViewModelObserver {
    //private val paint = Paint()
    private val backgroundColor = context.getColor(R.color.background_surface_view)
    private var snakeColor = context.getColor(R.color.snake)
    private var appleColor = context.getColor(R.color.apple)

    private val paintSnake = Paint()
    private val paintSnakeBlur = Paint()

    private val paintApple = Paint()
    private val paintAppleBlur = Paint()

    init {
        paintSnake.isAntiAlias = true
        paintSnake.isDither = true
        paintSnake.style = Paint.Style.FILL
        paintSnake.color = snakeColor

        paintSnakeBlur.set(paintSnake)
        paintSnakeBlur.maskFilter = BlurMaskFilter(50f, BlurMaskFilter.Blur.OUTER)

        paintApple.set(paintSnake)
        paintApple.color = appleColor
        paintAppleBlur.set(paintSnakeBlur)
        paintAppleBlur.color = appleColor
    }

    override fun draw(gameModel: GameModel) {
        drawFrame(gameModel)
    }

    fun drawFrame(gameModel: GameModel) {
        //Log.d("threads", "drawFrame: ${Looper.myLooper()}")
        var canvas: Canvas?
        canvas = null
        try {
            canvas = holder.lockCanvas() //получаем canvas
            synchronized(holder) {
                drawFrame(canvas, gameModel) //функция рисования //drawFrame(interpolation)
                //Log.v("$this/run", "draw: ")
            }
        } catch (e: NullPointerException) {
            e.printStackTrace() //если canvas не доступен
        } finally {
            if (canvas != null) holder.unlockCanvasAndPost(canvas) //освобождаем canvas
        }
    }

    private fun drawFrame(canvas: Canvas, gameModel: GameModel) {
        Log.d("threads", "drawFrame(): ${Thread.currentThread()}")
        canvas.drawColor(backgroundColor)

        try {
            for (apple in gameModel.apples){
                canvas.drawRect(apple.rect, paintAppleBlur)
                canvas.drawRect(apple.rect, paintApple)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } //IndexOutOfBoundsException, ConcurrentModificationException //TODO resolve ConcurrentModificationException

        val path = Path()
        try {
            for (s in gameModel.snake) {
                path.addRect(RectF(s.rect), Path.Direction.CW)
            }
            canvas.drawPath(path, paintSnakeBlur)
            canvas.drawPath(path, paintSnake)
        } catch (e: ConcurrentModificationException) {
            e.printStackTrace()
        }
    }

    private fun Snake.blur() : Rect {
        val newRect = rect.copy()
        if (direction == Direction.Top || direction == Direction.Bottom) {
            with (newRect) {
                top = rect.top
                bottom = rect.bottom
                left = rect.left - SNAKE_SIZE / 2
                right = rect.right + SNAKE_SIZE / 2
            }
        } else {
            with (newRect) {
                top = rect.top - SNAKE_SIZE / 2
                bottom = rect.bottom + SNAKE_SIZE / 2
                left = rect.left
                right = rect.right
            }
        }
        return newRect
    }

    private fun Rect.copy() : Rect {
        return Rect(left, top, right, bottom)
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