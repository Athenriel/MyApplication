package com.example.myapplication.ui.frame

import android.opengl.GLES32
import com.example.myapplication.interfaces.OpenGLRenderer
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.IntBuffer

/**
 * Created by Athenriel on 9/23/2022
 */
class LetterVFrame(private val renderer: OpenGLRenderer) {

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

    private val letterVVertices = floatArrayOf(
        //front
        -2f, 2f, 1f, //0
        -1f, 2f, 1f, //1
        0f, 0f, 1f, //2
        1f, 2f, 1f, //3
        2f, 2f, 1f, //4
        0f, -2f, 1f, //5
        //back
        -2f, 2f, -1f, //6
        -1f, 2f, -1f, //7
        0f, 0f, -1f, //8
        1f, 2f, -1f, //9
        2f, 2f, -1f, //10
        0f, -2f, -1f //11
    )

    private val letterVIndices = intArrayOf(
        //front
        0, 1, 2,
        2, 3, 4,
        4, 2, 5,
        5, 2, 0,
        //back
        6, 7, 8,
        8, 9, 10,
        10, 8, 11,
        11, 8, 6,
        //top 1
        0, 1, 6,
        1, 6, 7,
        //inner side 1
        1, 2, 7,
        2, 7, 8,
        //inner side 2
        2, 3, 8,
        3, 8, 9,
        //top 2
        3, 4, 9,
        4, 9, 10,
        //outer side 2
        4, 5, 10,
        5, 10, 11,
        //outer side 1
        0, 5, 11,
        11, 6, 0
    )

    private val letterVColor = floatArrayOf(
        //front
        0f, 0f, 1f, 1f, //0
        0f, 0f, 1f, 1f, //1
        0f, 0f, 1f, 1f, //2
        0f, 0f, 1f, 1f, //3
        0f, 0f, 1f, 1f, //4
        0f, 0f, 1f, 1f, //5
        //back
        0f, 1f, 1f, 1f, //6
        0f, 1f, 1f, 1f, //7
        0f, 1f, 1f, 1f, //8
        0f, 1f, 1f, 1f, //9
        0f, 1f, 1f, 1f, //10
        0f, 1f, 1f, 1f //11
    )

    init {
        indexBuffer = IntBuffer.allocate(letterVIndices.size)
        indexBuffer?.put(letterVIndices)
        indexBuffer?.position(0)

        // initialize vertex byte buffer for shape coordinates
        val bb: ByteBuffer =
            ByteBuffer.allocateDirect(letterVVertices.size * 4) // (# of coordinate values * 4 bytes per float)
        bb.order(ByteOrder.nativeOrder())
        vertexBuffer = bb.asFloatBuffer()
        vertexBuffer?.put(letterVVertices)
        vertexBuffer?.position(0)
        vertexCount = letterVVertices.size / COORDINATES_PER_VERTEX

        //initialize color byte buffer
        val cb: ByteBuffer =
            ByteBuffer.allocateDirect(letterVColor.size * 4) // (# of coordinate values * 4 bytes per float)
        cb.order(ByteOrder.nativeOrder())
        colorBuffer = cb.asFloatBuffer()
        colorBuffer?.put(letterVColor)
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
            letterVIndices.size,
            GLES32.GL_UNSIGNED_INT,
            indexBuffer
        )
    }

}