package com.example.snake2

import android.graphics.Canvas
import android.graphics.Rect

class GameFieldData {
    val snake = ArrayList<Rect>()
    var width = 20
    var height = 40

    companion object {
        var SCREEN_WIDTH = -1
        var SCREEN_HEIGHT = -1
        const val SIZE = 70
    }

    val startLeft = SCREEN_WIDTH / 2
    val startTop = SCREEN_HEIGHT / 2


    init {
        val rect = Rect(startLeft, startTop,startLeft + SIZE,startTop + SIZE)
        snake.add(rect)
    }

    fun calculateWidth(canvas: Canvas) {
        height *= if (canvas.height > canvas.width) canvas.height / canvas.width
                else canvas.width / canvas.height
    }
}