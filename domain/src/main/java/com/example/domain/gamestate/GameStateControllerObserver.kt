package com.example.domain.gamestate

interface GameStateControllerObserver {
    fun resumeGame()
    fun pauseGame()
    fun gameOver()
}