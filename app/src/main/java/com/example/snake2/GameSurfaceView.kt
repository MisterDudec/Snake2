package com.example.snake2

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.O)
class GameSurfaceView(context: Context, attrs: AttributeSet?, defStyle: Int) :
    SurfaceView(context, attrs, defStyle), SurfaceHolder.Callback {

    private var thread: SurfaceThread? = null
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    var cx: Float = 0F
    var cy: Float = 0F
    var offx = 10
    var offy = 10


    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context) : this(context, null, 0)

    init {
        holder.addCallback(this)
        thread = SurfaceThread(holder, this)
        focusable = FOCUSABLE
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 3F
        paint.color = Color.WHITE
    }

    override fun surfaceCreated(p0: SurfaceHolder) {
        thread?.setRunning(true)
        thread?.start()
    }

    override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {

    }

    override fun surfaceDestroyed(p0: SurfaceHolder) {
        var retry = true
        thread?.setRunning(false)
        while (retry) {
            try {
                thread?.join()
                retry = false
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }

    public override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawRGB(30,30,30)
        canvas?.drawCircle(cx, cy, 10F, paint)

        cx += offx
        if (cx > width || cx < 0) {
            offx *= -1
            cx += offx
        }
        cy += offy
        if (cy > height || cy < 0) {
            offy *= -1
            cy += offy
        }
    }
}