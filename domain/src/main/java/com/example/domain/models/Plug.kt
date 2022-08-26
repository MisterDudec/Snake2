package com.example.domain.models

import android.graphics.Rect

data class Plug(override val rect: Rect, var index: Int) : Cell(rect = rect) {

}
