package com.example.domain.models

class Counter {
    private var _apples = 0
    private var _lives: Float = 1f

    val apples get() = _apples
    val lives get() = _lives

    fun restartGame() {
        _apples = 0
        _lives = 1f
    }

    fun appleEaten() {
        _apples++
        _lives += 0.25f
    }

    fun snakeEaten() {
        _lives--
    }

    fun isDead() : Boolean {
        return _lives < 0
    }
}