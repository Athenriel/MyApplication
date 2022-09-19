package com.example.myapplication.ui.frame

import android.opengl.GLES32
import com.example.myapplication.ui.renderer.OpenGLLetterARenderer
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.IntBuffer

/**
 * Created by Athenriel on 9/19/2022
 */
class LetterAFrame(private val renderer: OpenGLLetterARenderer) {

    private var indexBuffer: IntBuffer? = null
    private var vertexBuffer: FloatBuffer? = null
    private var colorBuffer: FloatBuffer? = null
    private var mProgram = 0
    private var mPositionHandle = 0
    private var mMVPMatrixHandle = 0
    private var mColorHandle = 0
    private var vertexCount = 0 // number of vertices

    companion object {
        private const val VERTEX_SHADER_CODE = "attribute vec3 aVertexPosition;" +
                "attribute vec4 aVertexColor;" +
                "uniform mat4 uMVPMatrix;" +
                "varying vec4 vColor;" +
                "void main() {" +
                "gl_Position = uMVPMatrix * vec4(aVertexPosition, 1.0);" +
                "vColor=aVertexColor;" +
                "}"
        private const val FRAGMENT_SHADER_CODE = "precision mediump float;" +
                "varying vec4 vColor;" +
                "void main() {" +
                "gl_FragColor = vColor;" +
                "}"
        private const val COORDINATES_PER_VERTEX =
            3 // number of coordinates per vertex in this array
        private const val COLOR_PER_VERTEX =
            4 // number of color values per vertex in this array
        private const val VERTEX_STRIDE = COORDINATES_PER_VERTEX * 4 // 4 bytes per vertex
        private const val COLOR_STRIDE = COLOR_PER_VERTEX * 4 // 4 bytes per vertex
    }

    private val letterAVertices = floatArrayOf(
        -0.2f, 1.0f, -0.3f,
        -0.2f, 1.0f, 0.3f,
        0.2f, 1.0f, -0.3f,
        0.2f, 1.0f, 0.3f,
        -1.0f, -1.0f, -0.5f,
        -1.0f, -1.0f, 0.5f,
        -0.6f, -1.0f, -0.5f,
        -0.6f, -1.0f, 0.5f,
        0.6f, -1.0f, 0.5f,
        0.6f, -1.0f, -0.5f,
        1.0f, -1.0f, 0.5f,
        1.0f, -1.0f, -0.5f,
        0.0f, 0.8f, 0.3f,
        0.0f, 0.8f, -0.3f,
        0.25f, 0.1f, 0.382f,
        0.25f, 0.1f, -0.382f,
        -0.25f, 0.1f, 0.382f,
        -0.25f, 0.1f, -0.382f,
        0.32f, -0.1f, 0.41f,
        0.32f, -0.1f, -0.41f,
        -0.32f, -0.1f, 0.41f,
        -0.32f, -0.1f, -0.41f
    )

    private val letterAIndices = intArrayOf(
        0, 1, 2, 2, 3, 1,
        0, 4, 5, 5, 1, 0,
        4, 5, 6, 6, 7, 5,
        1, 5, 7, 7, 3, 1,
        0, 4, 6, 6, 2, 0,
        3, 10, 11, 11, 3, 2,
        8, 9, 10, 10, 11, 9,
        3, 10, 8, 8, 3, 1,
        2, 11, 9, 9, 2, 0,
        12, 13, 6, 6, 7, 12,
        12, 8, 9, 9, 13, 12,
        14, 15, 16, 16, 17, 15,
        19, 18, 20, 20, 21, 19,
        14, 18, 20, 20, 16, 14,
        15, 19, 21, 21, 17, 15
    )

    private val letterAColor = floatArrayOf(
        0.0f, 0.0f, 1.0f, 1.0f,
        0.0f, 0.0f, 1.0f, 1.0f,
        0.0f, 0.0f, 1.0f, 1.0f,
        0.0f, 0.0f, 1.0f, 1.0f,
        0.0f, 1.0f, 0.0f, 1.0f,
        0.0f, 1.0f, 0.0f, 1.0f,
        0.0f, 1.0f, 0.0f, 1.0f,
        0.0f, 1.0f, 0.0f, 1.0f,
        0.0f, 1.0f, 0.0f, 1.0f,
        0.0f, 1.0f, 0.0f, 1.0f,
        0.0f, 1.0f, 0.0f, 1.0f,
        0.0f, 1.0f, 0.0f, 1.0f,
        0.0f, 0.0f, 1.0f, 1.0f,
        0.0f, 0.0f, 1.0f, 1.0f,
        0.0f, 0.0f, 1.0f, 1.0f,
        0.0f, 0.0f, 1.0f, 1.0f,
        0.0f, 0.0f, 1.0f, 1.0f,
        0.0f, 0.0f, 1.0f, 1.0f,
        0.0f, 1.0f, 0.0f, 1.0f,
        0.0f, 1.0f, 0.0f, 1.0f,
        0.0f, 1.0f, 0.0f, 1.0f,
        0.0f, 1.0f, 0.0f, 1.0f
    )

    init {
        indexBuffer = IntBuffer.allocate(letterAIndices.size)
        indexBuffer?.put(letterAIndices)
        indexBuffer?.position(0)

        // initialize vertex byte buffer for shape coordinates
        val bb: ByteBuffer =
            ByteBuffer.allocateDirect(letterAVertices.size * 4) // (# of coordinate values * 4 bytes per float)
        bb.order(ByteOrder.nativeOrder())
        vertexBuffer = bb.asFloatBuffer()
        vertexBuffer?.put(letterAVertices)
        vertexBuffer?.position(0)
        vertexCount = letterAVertices.size / COORDINATES_PER_VERTEX

        //initialize color byte buffer
        val cb: ByteBuffer =
            ByteBuffer.allocateDirect(letterAColor.size * 4) // (# of coordinate values * 4 bytes per float)
        cb.order(ByteOrder.nativeOrder())
        colorBuffer = cb.asFloatBuffer()
        colorBuffer?.put(letterAColor)
        colorBuffer?.position(0)

        // prepare shaders and OpenGL program
        val vertexShader = renderer.loadShader(
            GLES32.GL_VERTEX_SHADER,
            VERTEX_SHADER_CODE
        )
        val fragmentShader = renderer.loadShader(
            GLES32.GL_FRAGMENT_SHADER,
            FRAGMENT_SHADER_CODE
        )
        mProgram = GLES32.glCreateProgram() // create empty OpenGL Program
        GLES32.glAttachShader(mProgram, vertexShader) // add the vertex shader to program
        GLES32.glAttachShader(mProgram, fragmentShader) // add the fragment shader to program
        GLES32.glLinkProgram(mProgram) // link the  OpenGL program to create an executable
        GLES32.glUseProgram(mProgram) // Add program to OpenGL environment

        // get handle to vertex shader's aVertexColor member
        mColorHandle = GLES32.glGetAttribLocation(mProgram, "aVertexColor")
        // get handle to vertex shader's aVertexPosition member
        mPositionHandle = GLES32.glGetAttribLocation(mProgram, "aVertexPosition")
        // Enable a handle to the triangle vertices
        GLES32.glEnableVertexAttribArray(mColorHandle)
        // Enable a handle to the triangle vertices
        GLES32.glEnableVertexAttribArray(mPositionHandle)
        // get handle to shape's transformation matrix
        mMVPMatrixHandle = GLES32.glGetUniformLocation(mProgram, "uMVPMatrix")
        renderer.checkGlError("glGetUniformLocation")
    }

    fun draw(mvpMatrix: FloatArray) {
        // Apply the projection and view transformation
        GLES32.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0)
        renderer.checkGlError("glUniformMatrix4fv")
        //set the attribute of the vertex to point to the vertex buffer
        GLES32.glVertexAttribPointer(
            mPositionHandle, COORDINATES_PER_VERTEX,
            GLES32.GL_FLOAT, false, VERTEX_STRIDE, vertexBuffer
        )
        GLES32.glVertexAttribPointer(
            mColorHandle, COLOR_PER_VERTEX,
            GLES32.GL_FLOAT, false, COLOR_STRIDE, colorBuffer
        )
        // Draw the triangle
        GLES32.glDrawElements(
            GLES32.GL_TRIANGLES,
            letterAIndices.size,
            GLES32.GL_UNSIGNED_INT,
            indexBuffer
        )
    }

}