package com.example.snake2.models

import android.graphics.Rect

data class Apple(override val rect: Rect) : Cell(rect = rect) {

}