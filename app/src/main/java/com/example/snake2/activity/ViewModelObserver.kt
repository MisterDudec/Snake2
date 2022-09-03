package com.example.snake2.activity

import com.example.domain.models.Field

interface ViewModelObserver {
    fun draw(gameModel: Field)
}