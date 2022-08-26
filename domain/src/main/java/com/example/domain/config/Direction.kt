package com.example.domain.config

sealed class Direction {

    object Top : Direction()
    object Right : Direction()
    object Bottom : Direction()
    object Left : Direction()

}
