package com.example.snake2

import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.core.view.GestureDetectorCompat

class MyGestureListener : GestureDetector.SimpleOnGestureListener() {

    override fun onDown(event: MotionEvent): Boolean {
        return super.onDown(event)
    }

    override fun onFling(event1: MotionEvent, event2: MotionEvent,
                         velocityX: Float, velocityY: Float): Boolean {
        if (velocityX > velocityY) {
            if (event2.x > event1.x) {
                Presenter.direction = Presenter.DIR_RIGHT
            } else if (event2.x < event1.x) {
                Presenter.direction = Presenter.DIR_LEFT
            }
        } else {
            if (event2.y > event1.y) {
                Presenter.direction = Presenter.DIR_BOTTOM
            } else if (event2.y < event1.y) {
                Presenter.direction = Presenter.DIR_TOP
            }
        }
        return true
    }
}