package com.example.snake2.data

import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import com.example.snake2.data.GameFieldData.Companion.SIZE
import com.example.snake2.data.GameFieldData.Companion.STEP

data class Snake(override val rect: Rect, var direction: Int) : Cell (rect) {
    var transition = false
    var turning = false
    var turningProgress = SIZE + STEP
    val paint = Paint()

    init {
        paint.color = Color.RED
    }

    fun setColor(color: Int) {
        paint.color = color
    }

    fun changeDirection(dir: Int) {
        direction = dir
        turning = true
        turningProgress = 0
    }

    fun onChangedDirection() {
        turning = false
    }
}