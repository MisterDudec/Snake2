package com.example.domain.gamestate

import com.example.domain.gamestate.GameState.*


object GameStateController {
    private var _state: GameState = Pause
    val gameState get() = _state

    var observer: GameStateControllerObserver? = null

    fun resumeGame() {
        _state = Play
        observer?.resumeGame()
    }

    fun pauseGame() {
        _state = Pause
        observer?.pauseGame()
    }

    fun gameOver() {
        _state = GameOver
        observer?.gameOver()
    }

}