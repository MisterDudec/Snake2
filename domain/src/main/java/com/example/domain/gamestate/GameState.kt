package com.example.domain.gamestate

sealed class GameState {
    object Pause : GameState()
    object Play : GameState()
    object GameOver : GameState()

}