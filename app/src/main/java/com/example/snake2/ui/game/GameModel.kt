package com.example.snake2.ui.game

import android.graphics.Rect
import android.util.Log
import com.example.snake2.models.Apple
import com.example.snake2.models.Snake
import com.example.snake2.ui.game.GameViewModel.Companion.DIR_DEFAULT
import com.example.snake2.ui.game.GameViewModel.Companion.DIR_TOP
import com.example.snake2.ui.game.GameViewModel.Companion.DIR_RIGHT
import com.example.snake2.ui.game.GameViewModel.Companion.DIR_BOTTOM

import kotlin.random.Random

class GameModel {
    private val logTag = "GameFieldData"
    val snake = ArrayList<Snake>()
    val apples = ArrayList<Apple>()
    private var _width: Int? = null
    private var _height: Int? = null
    val width get() = _width!!
    val height get() = _height!!
    private var isModelInitialized = false

    companion object {
        const val SIZE = 70
        const val STEP = 5
    }

    fun setDimensions(width: Int, height: Int) {
        _width = width
        _height = height
        if (!isModelInitialized) initialize(width, height)
    }

    private fun initialize(width: Int, height: Int) {
        val startLeft = width / 2
        val startTop = height / 2 + SIZE * 5
        val rect = Rect(startLeft, startTop, startLeft + SIZE, startTop + SIZE)
        snake.add(Snake(rect, DIR_DEFAULT, 0))
        repeat(3) {
            growSnake()
        }
        repeat(3) {
            addApple()
        }
        isModelInitialized = true
    }

    private fun growSnake() {
        val rectangle: Rect
        with (snake[snake.size - 1]) {
            when (direction) {
                DIR_TOP -> with (rect) {
                    rectangle = Rect(
                        left,
                        bottom,
                        right,
                        bottom + SIZE
                    )
                }
                DIR_RIGHT -> with (rect) {
                    rectangle = Rect(
                        left - SIZE,
                        top,
                        left,
                        bottom
                    )
                }
                DIR_BOTTOM -> with (rect) {
                    rectangle = Rect(
                        left,
                        top - SIZE,
                        right,
                        top
                    )
                }
                else -> with (rect) {
                    rectangle = Rect(
                        right,
                        top,
                        right + SIZE,
                        bottom
                    )
                }
            }
            snake.add(Snake(rectangle, direction, snake.size))
        }
    }

    fun addApple() {
        var x: Int
        var y: Int
        var ok: Boolean

        while (true) {
            x = Random.nextInt(0, width - SIZE)
            y = Random.nextInt(0, height - SIZE)
            ok = true
            /*for (s in snake) {
                synchronized(s) {
                    val b1 = s.rect.intersect(x - SIZE, y - SIZE, x + 2*SIZE, y + 2*SIZE)

                    if (b1) {
                        ok = false
                    }
                }
            }*/
            if (ok) break
        }

        apples.add(Apple(Rect(x, y, x + SIZE, y + SIZE)))
    }

    fun appleEaten(apple: Apple) {
        apples.remove(apple)
        growSnake()
        if (apples.size <= 0) {
            repeat(3) {
                addApple()
            }
        }
    }

    fun snakeEaten(s: Int) {
        for(i in snake.size - 1 downTo s){
            snake.removeAt(i)
        }
    }

    private fun printLogs(t: Int, r: Int, b: Int, l: Int) {
        Log.d("$logTag/apple", "t = $t")
        Log.d("$logTag/apple", "r = $r")
        Log.d("$logTag/apple", "b = $b")
        Log.d("$logTag/apple", "l = $l")
    }

    private fun printLogsXY(y: Int, x: Int) {
        Log.d("$logTag/apple", "y = $y")
        Log.d("$logTag/apple", "x = $x")
        Log.d("$logTag/apple", "")
    }

    /*fun calculateWidth(canvas: Canvas) {
        height *= if (canvas.height > canvas.width) canvas.height / canvas.width
                else canvas.width / canvas.height
    }*/
}