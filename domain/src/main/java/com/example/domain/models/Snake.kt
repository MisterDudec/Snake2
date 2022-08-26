package com.example.domain.models

import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import com.example.domain.config.Direction
import com.example.domain.config.Direction.*
import com.example.domain.config.SNAKE_SIZE
import com.example.domain.config.SNAKE_STEP
import org.jetbrains.annotations.TestOnly

data class Snake(override val rect: Rect, var direction: Direction, val index: Int) : Cell (rect) {
    private var transition = false
    var turning = false
    var turningProgress = SNAKE_SIZE + SNAKE_STEP
    val paint = Paint()

    init {
        when (index % 2) {
            0 -> setColor(Color.GREEN)
            1 -> setColor(Color.CYAN)
            2 -> setColor(Color.BLUE)
        }
    }

    @TestOnly
    private fun setColor(color: Int) {
        paint.color = color
    }

    fun moveTop(last: Boolean, height: Int) {
        with (rect) {
            if (bottom >= 0) {
                top -= SNAKE_STEP
                if (!turning || last) bottom -= SNAKE_STEP
                if (isTransition() && bottom <= height) completeTransition()
            } else {
                top = height
                bottom = height + SNAKE_SIZE
            }
            if (top <= 5) startTransition()
        }
    }

    fun moveRight(last: Boolean, width: Int) {
        with (rect) {
            if (left <= width) {
                right += SNAKE_STEP
                if (!turning || last) left += SNAKE_STEP
                if (isTransition() && left >= 0) completeTransition()
            } else {
                startTransition()
                left = 0 - SNAKE_SIZE
                right = 0
            }
            if (right >= width - 5) startTransition()
        }
    }

    fun moveBottom(last: Boolean, height: Int) {
        with (rect) {
            if (top <= height) {
                bottom += SNAKE_STEP
                if (!turning || last) top += SNAKE_STEP
                if (isTransition() && top >= 0) completeTransition()
            } else {
                top = 0 - SNAKE_SIZE
                bottom = 0
            }
            if (bottom >= height - 5) startTransition()
        }
    }

    fun moveLeft(last: Boolean, width: Int) {
        with (rect) {
            if (right >= 0) {
                left -= SNAKE_STEP
                if (!turning || last) right -= SNAKE_STEP
                if (isTransition() && right <= width) completeTransition()
            } else {
                left = width
                right = width + SNAKE_SIZE
            }
            if (left <= 5) startTransition()
        }
    }

    fun changeDirection(direction: Direction) {
        this.direction = direction
        turning = true
        turningProgress = 0
    }

    fun onChangedDirection() {
        turning = false
        when (direction) {
            Top -> rect.bottom = rect.top + SNAKE_SIZE
            Right -> rect.left = rect.right - SNAKE_SIZE
            Bottom -> rect.top = rect.bottom - SNAKE_SIZE
            Left -> rect.right = rect.left + SNAKE_SIZE
        }
    }

    private fun startTransition() {
        transition = true
    }

    private fun completeTransition() {
        transition = false
    }

    fun isTransition(): Boolean {
        return transition
    }

    /*fun moveTop(last: Boolean, height: Int) {
        with (rect) {
            if (top >= 0) {
                top -= STEP
                if (!turning || last) bottom -= STEP
                if (isTransition() && bottom <= height) completeTransition()
            } else {
                startTransition()
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
                if (isTransition() && left >= 0) completeTransition()
            } else {
                startTransition()
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
                if (isTransition() && top >= 0) completeTransition()
            } else {
                startTransition()
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
    }*/
}