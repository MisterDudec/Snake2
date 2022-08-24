package com.example.snake2.models

import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import com.example.snake2.ui.game.GameFieldData.Companion.SIZE
import com.example.snake2.ui.game.GameFieldData.Companion.STEP
import com.example.snake2.ui.game.GameViewModel.Companion.DIR_BOTTOM
import com.example.snake2.ui.game.GameViewModel.Companion.DIR_LEFT
import com.example.snake2.ui.game.GameViewModel.Companion.DIR_RIGHT
import com.example.snake2.ui.game.GameViewModel.Companion.DIR_TOP
import org.jetbrains.annotations.TestOnly

data class Snake(override val rect: Rect, var direction: Int, val index: Int) : Cell (rect) {
    private var transition = false
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

    @TestOnly
    private fun setColor(color: Int) {
        paint.color = color
    }

    fun moveTop(last: Boolean, height: Int) {
        with (rect) {
            if (bottom >= 0) {
                top -= STEP
                if (!turning || last) bottom -= STEP
                if (isTransition() && bottom <= height) completeTransition()
            } else {
                top = height
                bottom = height + SIZE
            }
            if (top <= 5) startTransition()
        }
    }

    fun moveRight(last: Boolean, width: Int) {
        with (rect) {
            if (left <= width) {
                right += STEP
                if (!turning || last) left += STEP
                if (isTransition() && left >= 0) completeTransition()
            } else {
                startTransition()
                left = 0 - SIZE
                right = 0
            }
            if (right >= width - 5) startTransition()
        }
    }

    fun moveBottom(last: Boolean, height: Int) {
        with (rect) {
            if (top <= height) {
                bottom += STEP
                if (!turning || last) top += STEP
                if (isTransition() && top >= 0) completeTransition()
            } else {
                top = 0 - SIZE
                bottom = 0
            }
            if (bottom >= height - 5) startTransition()
        }
    }

    fun moveLeft(last: Boolean, width: Int) {
        with (rect) {
            if (right >= 0) {
                left -= STEP
                if (!turning || last) right -= STEP
                if (isTransition() && right <= width) completeTransition()
            } else {
                left = width
                right = width + SIZE
            }
            if (left <= 5) startTransition()
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
            DIR_TOP -> rect.bottom = rect.top + SIZE
            DIR_RIGHT -> rect.left = rect.right - SIZE
            DIR_BOTTOM -> rect.top = rect.bottom - SIZE
            DIR_LEFT -> rect.right = rect.left + SIZE
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