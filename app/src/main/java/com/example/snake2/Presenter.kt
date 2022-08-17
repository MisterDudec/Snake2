package com.example.snake2

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect

class Presenter {
    val gameFieldData = GameFieldData()
    val step = 10
    private val paint: Paint = Paint()

    init {
        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL
    }

    fun updateGame() {
        gameFieldData.snake[0].moveRight()
    }

    fun drawFrame(canvas: Canvas) {
        canvas.drawColor(Color.BLACK)
        paint.color = Color.RED
        for (i in gameFieldData.snake.indices)
            canvas.drawRect(gameFieldData.snake[i], paint)
    }

    private fun Rect.moveRight() {
        this.right += step
        this.left += step
    }

    private fun Rect.moveLeft() {
        this.right -= step
        this.left -= step
    }

    private fun Rect.moveTop() {
        this.top -= step
        this.bottom -= step
    }

    private fun Rect.moveBottom() {
        this.top += step
        this.bottom += step
    }

}