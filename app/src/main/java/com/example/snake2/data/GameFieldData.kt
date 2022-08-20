package com.example.snake2.data

import android.graphics.Rect
import android.util.Log
import com.example.snake2.Presenter.Companion.DIR_BOTTOM
import com.example.snake2.Presenter.Companion.DIR_DEFAULT
import com.example.snake2.Presenter.Companion.DIR_RIGHT
import com.example.snake2.Presenter.Companion.DIR_TOP
import kotlin.random.Random

class GameFieldData(val width: Int, val height: Int) {
    private val logTag = "GameFieldData"
    val snake = ArrayList<Snake>()
    val turns = ArrayList<Plug>()
    val apples = ArrayList<Apple>()
    var selfEating = false

    companion object {
        const val SIZE = 70
        const val STEP = 5
    }

    private val startLeft = width / 2
    private val startTop = height / 2

    init {
        val rect = Rect(startLeft, startTop,startLeft + SIZE,startTop + SIZE)
        snake.add(Snake(rect, DIR_DEFAULT))
        repeat(5) {
            growSnake()
        }
    }

    private fun growSnakeTest() {
        val r: Rect
        with (snake[snake.size - 1]) {
            r = when (direction) {
                DIR_TOP     -> with (rect) { Rect(left, bottom + STEP, right, bottom + SIZE + STEP) }
                DIR_RIGHT   -> with (rect) { Rect(left - SIZE - STEP, top, left - STEP, bottom) }
                DIR_BOTTOM  -> with (rect) { Rect(left, top - SIZE - STEP, right, top - STEP) }
                else        -> with (rect) { Rect(right + STEP, top, right + SIZE + STEP, bottom) }
            }
            snake.add(Snake(r, direction))
        }
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
            snake.add(Snake(rectangle, direction))
        }
    }

    fun addApple() {
        var x: Int
        var y: Int

        loop@while (true) {
            x = Random.nextInt(0, width - SIZE)
            y = Random.nextInt(0, height - SIZE)

            for (s in snake) {
                val b1 = s.rect.intersect(x - SIZE, y - SIZE, x + 2*SIZE, y + 2*SIZE)

                if (!b1) {
                    break@loop
                }
            }
        }

        apples.add(Apple(Rect(x, y, x + SIZE, y + SIZE)))
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

    fun addApple1() {
        var t = 5000
        var r = 5000
        var b = -5000
        var l = -5000

        for (s in snake.indices) {
            with(snake[s].rect) {
                if (t > top) t = top
                if (r > right) r = right
                if (b < bottom) b = bottom
                if (l < left) l = left
            }
        }
        val boolY = Random.nextBoolean()
        val boolX = Random.nextBoolean()

        val ry1 = IntRange(2 * SIZE, t)
        val ry2 = IntRange(b, height - SIZE)
        val rx1 = IntRange(2 * SIZE, l)
        val rx2 = IntRange(r, width - SIZE)

        val y = if (boolY) ry1.random() - 2 * SIZE
        else ry2.random() + SIZE
        val x = if (boolX) rx1.random() - 2 * SIZE
        else rx2.random() + SIZE

//        val y = if (boolY && t > 0) r1.random() - 2 * SIZE else Random.nextInt(b, SCREEN_HEIGHT) + SIZE
//        val x = if (boolX && l > 0) r2.random() - 2 * SIZE else Random.nextInt(r, SCREEN_WIDTH) + SIZE

        apples.add(Apple(Rect(x, y, x + SIZE, y + SIZE)))
    }

    fun addApple2() {
        var t = height; var r = width
        var b = 0; var l = 0

        for (s in snake.indices) {
            with(snake[s].rect) {
                if (t > top) t = top
                if (r > right) r = right
                if (b < bottom) b = bottom
                if (l < left) l = left
            }
        }

        printLogs(t, r, b, l)

        try {
            val y = if (Random.nextBoolean()) Random.nextInt(0, t)
            else Random.nextInt(b, height - SIZE)
            val x = if (Random.nextBoolean()) Random.nextInt(0, l)
            else Random.nextInt(r, width - SIZE)

            printLogsXY(y, x)

            apples.add(Apple(Rect(x, y, x + SIZE, y + SIZE)))
        } catch (e: Exception) {
            Log.d(logTag, e.printStackTrace().toString())
        }
    }

    fun addApple3() {
        var x = 0; var y = 0
        var xb = false
        var yb = false

        val marg = 1.5 * SIZE;
        while (!xb || !yb) {
            xb = false; yb = false
            x = Random.nextInt(0, width - SIZE)
            y = Random.nextInt(0, height - SIZE)

            for (s in snake) {
                val b1 = x - marg <= s.rect.centerX() || x + marg >= s.rect.centerX()
                val b2 = y - marg <= s.rect.centerY() || y + marg >= s.rect.centerY()

                if (b1 && b2) {
                    xb = true
                    yb = true
                } else {
                    xb = false
                    yb = false
                }
            }
        }

        apples.add(Apple(Rect(x, y, x + SIZE, y + SIZE)))
    }

    /*fun calculateWidth(canvas: Canvas) {
        height *= if (canvas.height > canvas.width) canvas.height / canvas.width
                else canvas.width / canvas.height
    }*/
}