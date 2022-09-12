package com.example.myapplication.ui.frame

import android.opengl.GLES32
import com.example.myapplication.ui.renderer.OpenGLPentagonPrismRenderer
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.IntBuffer
import kotlin.math.cos
import kotlin.math.sin

/**
 * Created by Athenriel on 9/13/2022
 */
class PentagonPrismFrame(private val renderer: OpenGLPentagonPrismRenderer) {

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

    private val point0X = 0f
    private val point0Y = 0f
    private val point1X = 0f
    private val point1Y = 1f
    private val point2X = sin(2 * Math.PI / 5).toFloat()
    private val point2Y = cos(2 * Math.PI / 5).toFloat()
    private val point3X = sin(4 * Math.PI / 5).toFloat()
    private val point3Y = -cos(Math.PI / 5).toFloat()
    private val point4X = -point3X
    private val point4Y = point3Y
    private val point5X = -point2X
    private val point5Y = point2Y
    private val front = 1f
    private val back = -1f

    private val pentagonPrismVertices = floatArrayOf(
        //front face
        point0X, point0Y, front, //0
        point1X, point1Y, front, //1
        point2X, point2Y, front, //2
        point3X, point3Y, front, //3
        point4X, point4Y, front, //4
        point5X, point5Y, front, //5
        //back face
        point0X, point0Y, back, //6
        point1X, point1Y, back, //7
        point2X, point2Y, back, //8
        point3X, point3Y, back, //9
        point4X, point4Y, back, //10
        point5X, point5Y, back, //11
        //side 1
        point1X, point1Y, front, //12
        point1X, point1Y, back, //13
        point2X, point2Y, back, //14
        point2X, point2Y, front, //15
        //side 2
        point2X, point2Y, front, //16
        point2X, point2Y, back, //17
        point3X, point3Y, back, //18
        point3X, point3Y, front, //19
        //side 3
        point3X, point3Y, front, //20
        point3X, point3Y, back, //21
        point4X, point4Y, back, //22
        point4X, point4Y, front, //23
        //side 4
        point4X, point4Y, front, //24
        point4X, point4Y, back, //25
        point5X, point5Y, back, //26
        point5X, point5Y, front, //27
        //side 5
        point5X, point5Y, front, //28
        point5X, point5Y, back, //29
        point1X, point1Y, back, //30
        point1X, point1Y, front, //31
    )

    private val pentagonPrismIndices = intArrayOf(
        0, 1, 2, 0, 2, 3, 0, 3, 4, 0, 4, 5, 0, 5, 1, //front face
        6, 7, 8, 6, 8, 9, 6, 9, 10, 6, 10, 11, 6, 11, 7, //back face
        12, 13, 14, 12, 14, 15, //side 1
        16, 17, 18, 16, 18, 19, //side 2
        20, 21, 22, 20, 22, 23, //side 3
        24, 25, 26, 24, 26, 27, //side 4
        28, 29, 30, 28, 30, 31 //side 5
    )

    private val pentagonPrismColor = floatArrayOf(
        //front face
        0f, 0f, 1f, 1f,
        0f, 0f, 1f, 1f,
        0f, 0f, 1f, 1f,
        0f, 0f, 1f, 1f,
        0f, 0f, 1f, 1f,
        0f, 0f, 1f, 1f,
        //back face
        1f, 0f, 0f, 1f,
        1f, 0f, 0f, 1f,
        1f, 0f, 0f, 1f,
        1f, 0f, 0f, 1f,
        1f, 0f, 0f, 1f,
        1f, 0f, 0f, 1f,
        //side 1
        0f, 1f, 0f, 1f,
        0f, 1f, 0f, 1f,
        0f, 1f, 0f, 1f,
        0f, 1f, 0f, 1f,
        //side 2
        1f, 1f, 0f, 1f,
        1f, 1f, 0f, 1f,
        1f, 1f, 0f, 1f,
        1f, 1f, 0f, 1f,
        //side 3
        0f, 1f, 1f, 1f,
        0f, 1f, 1f, 1f,
        0f, 1f, 1f, 1f,
        0f, 1f, 1f, 1f,
        //side 4
        1f, 0f, 1f, 1f,
        1f, 0f, 1f, 1f,
        1f, 0f, 1f, 1f,
        1f, 0f, 1f, 1f,
        //side 5
        1f, 1f, 1f, 1f,
        1f, 1f, 1f, 1f,
        1f, 1f, 1f, 1f,
        1f, 1f, 1f, 1f,
    )

    init {
        indexBuffer = IntBuffer.allocate(pentagonPrismIndices.size)
        indexBuffer?.put(pentagonPrismIndices)
        indexBuffer?.position(0)

        // initialize vertex byte buffer for shape coordinates
        val bb: ByteBuffer =
            ByteBuffer.allocateDirect(pentagonPrismVertices.size * 4) // (# of coordinate values * 4 bytes per float)
        bb.order(ByteOrder.nativeOrder())
        vertexBuffer = bb.asFloatBuffer()
        vertexBuffer?.put(pentagonPrismVertices)
        vertexBuffer?.position(0)
        vertexCount = pentagonPrismVertices.size / COORDINATES_PER_VERTEX

        //initialize color byte buffer
        val cb: ByteBuffer =
            ByteBuffer.allocateDirect(pentagonPrismColor.size * 4) // (# of coordinate values * 4 bytes per float)
        cb.order(ByteOrder.nativeOrder())
        colorBuffer = cb.asFloatBuffer()
        colorBuffer?.put(pentagonPrismColor)
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
            pentagonPrismIndices.size,
            GLES32.GL_UNSIGNED_INT,
            indexBuffer
        )
    }

}