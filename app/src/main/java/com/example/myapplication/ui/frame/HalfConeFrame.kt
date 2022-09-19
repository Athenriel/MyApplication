package com.example.myapplication.ui.frame

import android.opengl.GLES32
import com.example.myapplication.model.ThreeDFloatsModel
import com.example.myapplication.ui.renderer.OpenGLHalfConeRenderer
import com.example.myapplication.utils.GraphicUtils
import java.lang.reflect.Array
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.IntBuffer
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

/**
 * Created by Athenriel on 9/23/2022
 */
class HalfConeFrame(private val renderer: OpenGLHalfConeRenderer) {

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

    private var indexesSize = 0

    init {
        val radius = 2f
        val latitudes = 30
        val longitudes = 30
        val dist = 4f
        val highValue = 27
        val mediumValue = 25
        val lowValue = 10

        val verticesTempArray = FloatArray(65535)
        val colorTempArray = FloatArray(65535)
        var vertexIndex = 0
        var colorIndex = 0

        for (i in 0..latitudes) {
            var tColor = -0.5f
            val tColorIncrement = 1f / (longitudes + 1)
            when (i) {
                highValue -> {
                    val theta = i * Math.PI / latitudes
                    val sinTheta = sin(theta)
                    val cosTheta = cos(theta)
                    for (j in 0..longitudes) {
                        val phi = j * 2 * Math.PI / longitudes
                        val sinPhi = sin(phi)
                        val cosPhi = cos(phi)
                        val x = cosPhi * sinTheta
                        val y = cosTheta
                        val z = sinPhi * sinTheta
                        verticesTempArray[vertexIndex++] = radius * x.toFloat()
                        verticesTempArray[vertexIndex++] = radius * y.toFloat() + dist
                        verticesTempArray[vertexIndex++] = radius * z.toFloat()
                        colorTempArray[colorIndex++] = 1f
                        colorTempArray[colorIndex++] = 0f
                        colorTempArray[colorIndex++] = abs(tColor) + 0.5f
                        colorTempArray[colorIndex++] = 1f
                    }
                }
                mediumValue -> {
                    val theta = i * Math.PI / latitudes
                    val sinTheta = sin(theta)
                    val cosTheta = cos(theta)
                    for (j in 0..longitudes) {
                        val phi = j * 2 * Math.PI / longitudes
                        val sinPhi = sin(phi)
                        val cosPhi = cos(phi)
                        val x = cosPhi * sinTheta
                        val y = cosTheta
                        val z = sinPhi * sinTheta
                        verticesTempArray[vertexIndex++] = radius * x.toFloat() / 2
                        verticesTempArray[vertexIndex++] = radius * y.toFloat() / 2 + dist * 0.2f
                        verticesTempArray[vertexIndex++] = radius * z.toFloat() / 2
                        colorTempArray[colorIndex++] = 1f
                        colorTempArray[colorIndex++] = 0f
                        colorTempArray[colorIndex++] = 0f
                        colorTempArray[colorIndex++] = 1f
                    }
                }
                lowValue -> {
                    val theta = i * Math.PI / latitudes
                    val sinTheta = sin(theta)
                    val cosTheta = cos(theta)
                    for (j in 0..longitudes) {
                        val phi = j * 2 * Math.PI / longitudes
                        val sinPhi = sin(phi)
                        val cosPhi = cos(phi)
                        val x = cosPhi * sinTheta
                        val y = cosTheta
                        val z = sinPhi * sinTheta

                    }
                }
            }
            tColor += tColorIncrement
        }

        val verticesArray = verticesTempArray.copyOf()
        val colorArray = colorTempArray.copyOf()

        val indexesList = arrayListOf<Int>()
        val pLength = longitudes + 1
        for (j in 0 until longitudes) {
            indexesList.add(j)
            indexesList.add(j + pLength)
            indexesList.add(j + 1)

            indexesList.add(j + 1)
            indexesList.add(j + pLength + 1)
            indexesList.add(j + pLength)

            indexesList.add(j + pLength)
            indexesList.add(j + pLength * 2)
            indexesList.add(j + pLength + 1)

            indexesList.add(j + pLength + 1)
            indexesList.add(j + pLength * 2 + 1)
            indexesList.add(j + pLength * 2)

            indexesList.add(j)
            indexesList.add(j + pLength * 3)
            indexesList.add(j + 1)

            indexesList.add(j + 1)
            indexesList.add(j + pLength * 3 + 1)
            indexesList.add(j + pLength * 3)
        }

        indexesSize = indexesList.size

        indexBuffer = IntBuffer.allocate(indexesList.size)
        indexBuffer?.put(indexesList.toIntArray())
        indexBuffer?.position(0)

        // initialize vertex byte buffer for shape coordinates
        val bb: ByteBuffer =
            ByteBuffer.allocateDirect(verticesArray.size * 4) // (# of coordinate values * 4 bytes per float)
        bb.order(ByteOrder.nativeOrder())
        vertexBuffer = bb.asFloatBuffer()
        vertexBuffer?.put(verticesArray)
        vertexBuffer?.position(0)
        vertexCount = verticesArray.size / COORDINATES_PER_VERTEX

        //initialize color byte buffer
        val cb: ByteBuffer =
            ByteBuffer.allocateDirect(colorArray.size * 4) // (# of coordinate values * 4 bytes per float)
        cb.order(ByteOrder.nativeOrder())
        colorBuffer = cb.asFloatBuffer()
        colorBuffer?.put(colorArray)
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
            indexesSize,
            GLES32.GL_UNSIGNED_INT,
            indexBuffer
        )
    }

}