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
        when (index % 2) {
            0 -> setColor(Color.GREEN)
            1 -> setColor(Color.CYAN)
            2 -> setColor(Color.BLUE)
        }
    }

    fun setColor(color: Int) {
        paint.color = color
    }



    fun moveTop(last: Boolean, height: Int) {
        with (rect) {
            if (top >= 0) {
                top -= STEP
                if (!turning || last) bottom -= STEP
            } else {
                top = height
                bottom = height + SIZE
            }
        }
    }

    fun moveRight(last: Boolean, width: Int) {
        with (rect) {
            if (right <= width) {
                right += STEP
                if (!turning || last) left += STEP
            } else {
                left = 0 - SIZE
                right = 0
            }
        }
    }

    fun moveBottom(last: Boolean, height: Int) {
        with (rect) {
            if (bottom <= height) {
                bottom += STEP
                if (!turning || last) top += STEP
            } else {
                top = 0 - SIZE
                bottom = 0
            }
        }
    }

    fun moveLeft(last: Boolean, width: Int) {
        with (rect) {
            if (left >= 0) {
                left -= STEP
                if (!turning || last) right -= STEP
                if (isTransition() && right <= width) completeTransition()
            } else {
                startTransition()
                left = width
                right = width + SIZE
            }
        }
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

    fun startTransition() {
        transition = true
    }

    fun completeTransition() {
        transition = false
    }

    fun isTransition(): Boolean {
        return transition
    }
}