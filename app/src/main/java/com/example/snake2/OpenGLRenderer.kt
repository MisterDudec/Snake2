package com.example.snake2

import android.opengl.EGLConfig
import android.opengl.GLES20.*
import android.opengl.GLSurfaceView
import javax.microedition.khronos.opengles.GL10




class OpenGLRenderer : GLSurfaceView.Renderer {
    override fun onDrawFrame(arg0: GL10?) {
        glClear(GL_COLOR_BUFFER_BIT)
    }

    override fun onSurfaceCreated(p0: GL10?, p1: javax.microedition.khronos.egl.EGLConfig?) {
        glClearColor(0f, 1f, 0f, 1f)
    }

    override fun onSurfaceChanged(arg0: GL10?, width: Int, height: Int) {
        glViewport(0, 0, width, height)
    }
}