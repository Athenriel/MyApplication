package com.example.myapplication.ui.frame

import android.opengl.GLES32
import com.example.myapplication.interfaces.OpenGLRenderer
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.IntBuffer

/**
 * Created by Athenriel on 9/19/2022
 */
class LetterAToStereoFrame(private val renderer: OpenGLRenderer) {

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
                "float depth = 1.0 - gl_FragCoord.z;" + //to show closer surface to be brighter, and further away surface darker
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
        -0.2f, 1f, -0.3f, //0
        -0.2f, 1f, 0.3f, //1
        0.2f, 1f, -0.3f, //2
        0.2f, 1f, 0.3f, //3
        -1f, -1f, -0.5f, //4
        -1f, -1f, 0.5f, //5
        -0.6f, -1f, -0.5f, //6
        -0.6f, -1f, 0.5f, //7
        0.6f, -1f, 0.5f, //8
        0.6f, -1f, -0.5f, //9
        1f, -1f, 0.5f, //10
        1f, -1f, -0.5f, //11
        0f, 0.8f, 0.3f, //12
        0f, 0.8f, -0.3f, //13
        0.25f, 0.1f, 0.382f, //14
        0.25f, 0.1f, -0.382f, //15
        -0.25f, 0.1f, 0.382f, //16
        -0.25f, 0.1f, -0.382f, //17
        0.32f, -0.1f, 0.41f, //18
        0.32f, -0.1f, -0.41f, //19
        -0.32f, -0.1f, 0.41f, //20
        -0.32f, -0.1f, -0.41f //21
    )

    private val letterAIndices = intArrayOf(
        1, 0, 2, 1, 3, 2, //top
        4, 0, 5, 5, 1, 0, //left
        4, 5, 6, 6, 7, 5, //left bottom
        1, 5, 7, 7, 3, 1, //left front
        4, 0, 6, 2, 6, 0, //left back
        3, 10, 11, 11, 3, 2, //right
        8, 9, 10, 10, 11, 9, //right bottom
        10, 3, 8, 8, 3, 1, //right front
        2, 11, 9, 2, 9, 0, //right back
        6, 12, 13, 7, 6, 12, //left inner
        9, 8, 12, 9, 13, 12, //right inner
        14, 15, 16, 15, 17, 16, //inner top
        19, 18, 20, 20, 21, 19, //inner bottom
        18, 14, 20, 16, 20, 14, //inner front
        15, 19, 21, 21, 17, 15 //inner back
    )

    private val letterAColor = floatArrayOf(
        0.0f, 0.0f, 1.0f, 1.0f, //0
        0.0f, 0.0f, 1.0f, 1.0f, //1
        0.0f, 0.0f, 1.0f, 1.0f, //2
        0.0f, 0.0f, 1.0f, 1.0f, //3
        0.0f, 1.0f, 0.0f, 1.0f, //4
        0.0f, 1.0f, 0.0f, 1.0f, //5
        0.0f, 1.0f, 0.0f, 1.0f, //6
        0.0f, 1.0f, 0.0f, 1.0f, //7
        0.0f, 1.0f, 0.0f, 1.0f, //8
        0.0f, 1.0f, 0.0f, 1.0f, //9
        0.0f, 1.0f, 0.0f, 1.0f, //10
        0.0f, 1.0f, 0.0f, 1.0f, //11
        0.0f, 0.0f, 1.0f, 1.0f, //12
        0.0f, 0.0f, 1.0f, 1.0f, //13
        0.0f, 0.0f, 1.0f, 1.0f, //14
        0.0f, 0.0f, 1.0f, 1.0f, //15
        0.0f, 0.0f, 1.0f, 1.0f, //16
        0.0f, 0.0f, 1.0f, 1.0f, //17
        0.0f, 1.0f, 0.0f, 1.0f, //18
        0.0f, 1.0f, 0.0f, 1.0f, //19
        0.0f, 1.0f, 0.0f, 1.0f, //20
        0.0f, 1.0f, 0.0f, 1.0f //21
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
        GLES32.glUseProgram(mProgram)
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