package com.example.snake2.surfaceview

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

    private lateinit var thread: SurfaceThread
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    var cx: Float = 0F
    var cy: Float = 0F
    var offx = 10
    var offy = 10

    constructor(context: Context) : this(context, null, 0)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    init {
        holder.addCallback(this)
        focusable = FOCUSABLE
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 3F
        paint.color = Color.WHITE
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        thread = SurfaceThread(holder, this)
        thread.setRunning(true)
        thread.start()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {

    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        var retry = true
        thread.setRunning(false)
        while (retry) {
            try {
                thread.join()
                retry = false
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }

    public override fun onDraw(canvas: Canvas?) {
        //super.onDraw(canvas)
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

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
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

/**
Лучше, чтобы передача UI поля потоку происходила посредством WeakReference (или где-то
устанавливался бы референс в null). Иначе, в случае зависания потока будет мемори лик
целой активити со всем наполнением потому как потоки не обязательно сразу удаляются
мусоросборщиком, а могут еще жить неопределенное время. В этом случае, если запускать
приложение несколько раз, размер свободного пространства на хипе может быстро сократиться,
что может привести к ООМ эксепшену.

Пишу это не потому что охото придраться к коду, а потому, что в своем большом проекте уже
большое кличество времени потратили на избавление от подобных мемори ликов.
 */