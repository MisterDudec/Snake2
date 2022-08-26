package com.example.domain.models

import android.graphics.Rect
import android.util.Log
import com.example.domain.config.*
import com.example.domain.config.Direction.*

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

    var appleCounter = 0
    var liveCounter: Float = 1f

    private var _gameOver = false
    val gameOver get() = _gameOver

    fun setDimensions(width: Int, height: Int) {
        _width = width
        _height = height
        if (!isModelInitialized) initialize()
    }

    private fun initialize() {
        val startLeft = width / 2
        val startTop = height / 2 + SNAKE_SIZE * 10
        val rect = Rect(startLeft, startTop, startLeft + SNAKE_SIZE, startTop + SNAKE_SIZE)
        snake.add(Snake(rect, DEFAULT_DIRECTION, 0))
        repeat(START_SNAKE_LENGTH - 1) {
            growSnake()
        }
        repeat(START_APPLES_SIZE) {
            addApple()
        }
        isModelInitialized = true
    }

    fun restartGame() {
        _gameOver = false
        snake.removeAll(snake.toSet())
        apples.removeAll(apples.toSet())
        appleCounter = 0
        liveCounter = 1f
        initialize()
    }

    private fun growSnake() {
        val rectangle: Rect
        with (snake[snake.size - 1]) {
            when (direction) {
                Top -> with (rect) {
                    rectangle = Rect(
                        left,
                        bottom,
                        right,
                        bottom + SNAKE_SIZE
                    )
                }
                Right -> with (rect) {
                    rectangle = Rect(
                        left - SNAKE_SIZE,
                        top,
                        left,
                        bottom
                    )
                }
                Bottom -> with (rect) {
                    rectangle = Rect(
                        left,
                        top - SNAKE_SIZE,
                        right,
                        top
                    )
                }
                Left -> with (rect) {
                    rectangle = Rect(
                        right,
                        top,
                        right + SNAKE_SIZE,
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
            x = Random.nextInt(0, width - APPLE_SIZE)
            y = Random.nextInt(0, height - APPLE_SIZE)
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

        apples.add(Apple(Rect(x, y, x + APPLE_SIZE, y + APPLE_SIZE)))
    }

    fun appleEaten(apple: Apple) {
        apples.remove(apple)
        growSnake()
        if (apples.size <= 1) {
            repeat(START_APPLES_SIZE) {
                addApple()
            }
        }
        appleCounter++
        liveCounter += 0.25f
    }

    fun snakeEaten(s: Int) : Boolean {
        for(i in snake.size - 1 downTo s){
            snake.removeAt(i)
            liveCounter--
        }
        return liveCounter < 0
    }

    fun gameOver() {
        _gameOver = true
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