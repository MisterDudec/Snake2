package com.example.snake2.data

import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import com.example.snake2.Presenter
import com.example.snake2.data.GameFieldData.Companion.SIZE
import com.example.snake2.data.GameFieldData.Companion.STEP

data class Snake(override val rect: Rect, var direction: Int, val index: Int) : Cell (rect) {
    var transition = false
    var turning = false
    var turningProgress = SIZE + STEP
    val paint = Paint()

    init {
        when (index % 3) {
            0 -> setColor(Color.GREEN)
            1 -> setColor(Color.CYAN)
            2 -> setColor(Color.BLUE)
        }
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
        when (direction) {
            Presenter.DIR_TOP -> rect.bottom = rect.top + SIZE
            Presenter.DIR_RIGHT -> rect.left = rect.right - SIZE
            Presenter.DIR_BOTTOM -> rect.top = rect.bottom - SIZE
            Presenter.DIR_LEFT -> rect.right = rect.left + SIZE
        }
    }
}