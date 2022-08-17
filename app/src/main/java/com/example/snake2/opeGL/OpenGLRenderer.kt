package com.example.snake2.opeGL

import android.content.Context
import android.opengl.GLES20.*
import android.opengl.GLSurfaceView
import com.example.snake2.R
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.opengles.GL10


class OpenGLRenderer(private val context: Context) : GLSurfaceView.Renderer {
    private var programId: Int = -1
    private var vertexData: FloatBuffer
    private var uColorLocation: Int = -1
    private var aPositionLocation: Int = -1
    val vertices: FloatArray
    var did = true

    init {
        vertices = floatArrayOf(
            /*-0.9f, 0.8f, -0.9f, 0.2f, -0.5f, 0.8f,
            -0.6f, 0.2f, -0.2f, 0.2f, -0.2f, 0.8f,
            0.1f, 0.8f, 0.1f, 0.2f, 0.5f, 0.8f,
            0.1f, 0.2f, 0.5f, 0.2f, 0.5f, 0.8f*/
            //0.1f, 0.1f,     0.1f, -0.1f,    -0.1f, -0.1f,    -0.1f, 0.1f
            0f, 0f,     0f, 0.1f,    0.1f, 0.1f,    0.1f, 0f
        )
        /*if (did) {
            for (i in vertices.indices) {
                vertices[i] *= 3f
            }
        }
        did = false*/
        //val vertices = floatArrayOf(-0.5f, -0.2f, 0.0f, 0.2f, 0.5f, -0.2f)

        vertexData = ByteBuffer.allocateDirect(vertices.size * 4)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
        vertexData.put(vertices)
    }

    override fun onSurfaceCreated(p0: GL10?, p1: javax.microedition.khronos.egl.EGLConfig?) {
        glClearColor(0f, 0f, 0f, 1f)
        val vertexShaderId = ShaderUtils.createShader(context, GL_VERTEX_SHADER, R.raw.vertex_shader)
        val fragmentShaderId = ShaderUtils.createShader(context, GL_FRAGMENT_SHADER, R.raw.fragment_shader)
        programId = ShaderUtils.createProgram(vertexShaderId, fragmentShaderId)
        glUseProgram(programId)
        bindData()
    }

    override fun onSurfaceChanged(arg0: GL10?, width: Int, height: Int) {
        glViewport(0, 0, width, height)
    }

    private fun bindData() {
        uColorLocation = glGetUniformLocation(programId, "u_Color")
        glUniform4f(uColorLocation, 0.0f, 0.0f, 1.0f, 1.0f)
        aPositionLocation = glGetAttribLocation(programId, "a_Position")
        vertexData.position(0)
        glVertexAttribPointer(aPositionLocation, 2, GL_FLOAT, false, 0, vertexData)
        glEnableVertexAttribArray(aPositionLocation)
    }

    override fun onDrawFrame(arg0: GL10?) {
        glClear(GL_COLOR_BUFFER_BIT)
        glDrawArrays(GL_TRIANGLE_STRIP, 0, vertices.size / 2);
    }
}