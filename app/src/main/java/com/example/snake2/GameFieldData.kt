package com.example.snake2

import android.graphics.Canvas
import android.graphics.Rect

class GameFieldData {
    val snake = ArrayList<Rect>()
    var width = 20
    var height = 40


    init {
        val rect = Rect(0,0,100,100)
        snake.add(rect)
    }

    fun calculateWidth(canvas: Canvas) {
        height *= if (canvas.height > canvas.width) canvas.height / canvas.width
                else canvas.width / canvas.height
    }


}