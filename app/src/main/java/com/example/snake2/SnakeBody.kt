package com.example.snake2

import android.graphics.Rect
import com.example.snake2.GameFieldData.Companion.SIZE

class SnakeBody(val rect: Rect, var direction: Int) {
    var transition = false
    var turning = false
    var turningProgress = SIZE

    fun changeDirection(dir: Int) {
        direction = dir
        turning = true
        turningProgress = 0
    }

    fun onChangedDirection() {
        turning = false
    }

    /*fun move() {
        if (!turning) {
            when (direction) {
                Presenter.DIR_TOP -> moveTop()
                Presenter.DIR_RIGHT -> moveRight()
                Presenter.DIR_BOTTOM -> moveBottom()
                Presenter.DIR_LEFT -> moveLeft()
            }
        }
    }*/
/*
    private fun moveTop() {
        if (rect.top <= 0 && !transition) {
            val snakeBody = SnakeBody(Rect(rect.left,
                GameFieldData.SCREEN_HEIGHT, rect.right, GameFieldData.SCREEN_HEIGHT + GameFieldData.SIZE
            ), Presenter.DIR_TOP)

            gameFieldData.snake.add(snakeBody)
            transition = true
        }
        rect.top -= step
        rect.bottom -= step
        if (rect.bottom < 0) {
            gameFieldData.snake.remove(this)
            transition = false
        }
    }

    *//*private fun Rect.moveTop() {
        if (bottom >= 0) {
            top -= step
            bottom -= step
        } else {
            top = SCREEN_HEIGHT
            bottom = SCREEN_HEIGHT + SIZE
        }
    }*//*

    private fun moveRight() {
        if (left <= GameFieldData.SCREEN_WIDTH) {
            right += step
            left += step
        } else {
            left = 0 - GameFieldData.SIZE
            right = 0
        }
    }

    private fun moveBottom() {
        if (top <= GameFieldData.SCREEN_HEIGHT) {
            top += step
            bottom += step
        } else {
            top = 0 - GameFieldData.SIZE
            bottom = 0
        }
    }

    private fun moveLeft() {
        if (right >= 0) {
            right -= step
            left -= step
        } else {
            left = GameFieldData.SCREEN_WIDTH
            right = GameFieldData.SCREEN_WIDTH + GameFieldData.SIZE
        }
    }*/
/*
    fun changeDirection() {

    }*/
}