package com.example.snake2.activity

import com.example.domain.models.GameModel

interface ViewModelObserver {
    fun draw(gameModel: GameModel)
}