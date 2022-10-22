package com.example.myapplication.ui.frame

import android.opengl.GLES20
import com.example.myapplication.interfaces.OpenGLRenderer
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import kotlin.math.cos
import kotlin.math.sin


/**
 * Created by Athenriel on 9/12/2022
 */
class EllipseFrame(private val renderer: OpenGLRenderer) {

    private var vertexBuffer: FloatBuffer? = null
    private var mProgram = 0
    private var mPositionHandle = 0
    private var mColorHandle = 0
    private val data = FloatArray(126)

    companion object {
        private const val VERTEX_SHADER_CODE = "attribute vec4 vPosition;" +
                "void main() {" +
                "gl_PointSize = 5.0;" +
                "gl_Position = vPosition;" +
                "}"
        private const val FRAGMENT_SHADER_CODE = "precision mediump float;" +
                "uniform vec4 vColor;" +
                "void main() {" +
                "gl_FragColor = vColor;" +
                "}"
    }

    init {
        // prepare shaders and OpenGL program
        val vertexShader: Int = renderer.loadShader(
            GLES20.GL_VERTEX_SHADER, VERTEX_SHADER_CODE
        )
        val fragmentShader: Int = renderer.loadShader(
            GLES20.GL_FRAGMENT_SHADER, FRAGMENT_SHADER_CODE
        )

        mProgram = GLES20.glCreateProgram() // create empty OpenGL Program
        GLES20.glAttachShader(mProgram, vertexShader) // add the vertex shader to program
        GLES20.glAttachShader(mProgram, fragmentShader) // add the fragment shader to program
        GLES20.glLinkProgram(mProgram) // create OpenGL program executables
    }

    private fun fillCircle(x: Float, y: Float, r: Float, scaleX: Float) {
        var i = 0
        var angle = 0f
        val twicePI = 2f * 3.1415926f
        val angleStepSize = 0.1f

        // go through all angles from 0 to 2 * PI radians
        while (angle < twicePI) {
            // calculate x, y from a vector with known length and angle
            data[i++] = x + scaleX * r * cos(angle.toDouble()).toFloat()
            data[i++] = y + r * sin(angle.toDouble()).toFloat()
            angle += angleStepSize
        }

        // initialize vertex byte buffer for shape coordinates
        val bb: ByteBuffer =
            ByteBuffer.allocateDirect( // (number of coordinate values * 4 bytes per float)
                i * 4
            )
        // use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder())

        // create a floating point buffer from the ByteBuffer
        vertexBuffer = bb.asFloatBuffer()

        // add the coordinates to the FloatBuffer
        vertexBuffer?.put(data)

        // set the buffer to read the first coordinate
        vertexBuffer?.position(0)
    }

    private fun drawCircle(x: Float, y: Float, r: Float, scaleX: Float, color: FloatArray) {
        fillCircle(x, y, r, scaleX)

        // Prepare the balloon coordinate data
        GLES20.glVertexAttribPointer(mPositionHandle, 2, GLES20.GL_FLOAT, false, 0, vertexBuffer)

        // get handle to fragment shader's vColor member
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor")

        // Set color for drawing the triangle
        GLES20.glUniform4fv(mColorHandle, 1, color, 0)
        renderer.checkGlError("mColorHandle")

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 63)
    }

    fun draw(scaleX: Float) {
        // Add program to OpenGL environment
        GLES20.glUseProgram(mProgram)

        // get handle to vertex shader's vPosition member
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition")

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle)

        drawCircle(0f, 0f, 0.2f, scaleX, floatArrayOf(0.5f, 0.5f, 1f, 0f))
        drawCircle(0f, 0f, 0.195f, scaleX, floatArrayOf(0.5f, 0.2f, 1f, 0f))

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle)
    }


}