package com.example.snake2.surfaceview

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.SurfaceView
import com.example.domain.config.Direction
import com.example.domain.config.GLOW
import com.example.domain.config.SNAKE_SIZE_DEFAULT
import com.example.domain.models.Field
import com.example.domain.models.Snake
import com.example.snake2.R
import com.example.snake2.activity.ViewModelObserver

class GameSurfaceView(context: Context, attrs: AttributeSet?) :
    SurfaceView(context, attrs), ViewModelObserver {


    private val backgroundColor = context.getColor(R.color.background_surface_view)
    private var snakeColor = context.getColor(R.color.snake)
    private var appleColor = context.getColor(R.color.apple)

    private val paintSnake = Paint()
    private val paintSnakeBlur = Paint()

    private val paintApple = Paint()
    private val paintAppleBlur = Paint()

    val path = Path()

    init {
        paintSnake.style = Paint.Style.FILL
        paintSnake.color = snakeColor

        paintSnakeBlur.set(paintSnake)
        paintSnakeBlur.maskFilter = BlurMaskFilter(30f, BlurMaskFilter.Blur.SOLID)

        paintApple.set(paintSnake)
        paintApple.color = appleColor

        paintAppleBlur.set(paintSnakeBlur)
        paintAppleBlur.color = appleColor
    }

    override fun draw(gameModel: Field) {
        drawFrameRect(gameModel)
    }

    fun drawFrameRect(gameModel: Field) {
        var canvas: Canvas? = null
        try {
            canvas = holder.lockCanvas()
            if (GLOW) drawFrameGlowPath(canvas, gameModel)
            else drawFrameRect(canvas, gameModel)
        } catch (e: NullPointerException) {
            e.printStackTrace()
        } finally {
            if (canvas != null) holder.unlockCanvasAndPost(canvas)
        }
    }

    private fun drawFrameRect(canvas: Canvas, gameModel: Field) {
        canvas.drawColor(backgroundColor)

        for (apple in gameModel.apples)
            canvas.drawRect(apple.rect, paintApple)

        for (s in gameModel.snake)
            canvas.drawRect(s.rect, paintSnake)
    }

    private fun drawFramePath(canvas: Canvas, gameModel: Field) {
        canvas.drawColor(backgroundColor)

        for (apple in gameModel.apples)
            canvas.drawRect(apple.rect, paintApple)

        path.reset()
        for (s in gameModel.snake)
            path.addRect(RectF(s.rect), Path.Direction.CW)

        canvas.drawPath(path, paintSnake)
    }

    private fun drawFrameGlowPath(canvas: Canvas, gameModel: Field) {
        canvas.drawColor(backgroundColor)

        for (apple in gameModel.apples){
            repeat(4) {
                canvas.drawRect(apple.rect, paintAppleBlur)
            }

            //canvas.drawRect(apple.rect, paintApple)
        }

        path.reset()
        for (s in gameModel.snake) {
            path.addRect(RectF(s.rect), Path.Direction.CW)
            //canvas.drawRect(s.rect, paintSnake)
        }
        canvas.drawPath(path, paintSnakeBlur)
        //canvas.drawPath(path, paintSnake)
    }

    private fun drawFrameGlowRect(canvas: Canvas, gameModel: Field) {
        canvas.drawColor(backgroundColor)

        for (apple in gameModel.apples){
            repeat(4) {
                canvas.drawRect(apple.rect, paintAppleBlur)
            }

            canvas.drawRect(apple.rect, paintApple)
        }

        for (s in gameModel.snake) {
            canvas.drawRect(s.rect, paintSnakeBlur)
        }
        /*for (s in gameModel.snake) {
            canvas.drawRect(s.rect, paintSnake)
        }*/
        /*
            path.reset()
            for (s in gameModel.snake) {
                path.addRect(RectF(s.rect), Path.Direction.CW)
                canvas.drawRect(s.rect, paintSnake)
            }
            canvas.drawPath(path, paintSnakeBlur)
         */
    }

    private fun drawFrameLightning(canvas: Canvas, gameModel: Field) {
        canvas.drawColor(backgroundColor)

        for (apple in gameModel.apples){
            repeat(4) {
                canvas.drawRect(apple.rect, paintAppleBlur)
            }

            canvas.drawRect(apple.rect, paintApple)
        }

        for (s in gameModel.snake) {
            canvas.drawRect(s.rect, paintSnakeBlur)
        }
        for (s in gameModel.snake) {
            canvas.drawRect(s.rect, paintSnake)
        }
        /*
            path.reset()
            for (s in gameModel.snake) {
                path.addRect(RectF(s.rect), Path.Direction.CW)
                canvas.drawRect(s.rect, paintSnake)
            }
            canvas.drawPath(path, paintSnakeBlur)
         */
    }

    private fun Snake.blur() : Rect {
        val newRect = rect.copy()
        if (direction == Direction.Top || direction == Direction.Bottom) {
            with (newRect) {
                top = rect.top
                bottom = rect.bottom
                left = rect.left - SNAKE_SIZE_DEFAULT / 2
                right = rect.right + SNAKE_SIZE_DEFAULT / 2
            }
        } else {
            with (newRect) {
                top = rect.top - SNAKE_SIZE_DEFAULT / 2
                bottom = rect.bottom + SNAKE_SIZE_DEFAULT / 2
                left = rect.left
                right = rect.right
            }
        }
        return newRect
    }

    private fun Rect.copy() : Rect {
        return Rect(left, top, right, bottom)
    }
}