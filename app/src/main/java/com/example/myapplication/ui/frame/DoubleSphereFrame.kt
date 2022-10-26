package com.example.myapplication.ui.frame

import android.opengl.GLES32
import com.example.myapplication.interfaces.OpenGLRenderer
import com.example.myapplication.utils.GraphicUtils
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.IntBuffer
import kotlin.math.abs

/**
 * Created by Athenriel on 9/23/2022
 */
class DoubleSphereFrame(private val renderer: OpenGLRenderer) {

    private var index1Buffer: IntBuffer? = null
    private var vertex1Buffer: FloatBuffer? = null
    private var color1Buffer: FloatBuffer? = null

    private var index2Buffer: IntBuffer? = null
    private var vertex2Buffer: FloatBuffer? = null
    private var color2Buffer: FloatBuffer? = null

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

    private var indexes1Size = 0
    private var indexes2Size = 0

    init {
        val radius = 2f
        val latitudes = 50
        val longitudes = 50
        val dist = 3f

        val vertices1List = GraphicUtils.getVerticesForSphere(radius, latitudes, longitudes, dist)
        val vertices2List = GraphicUtils.getVerticesForSphere(radius, latitudes, longitudes, -dist)
        val indexes1Array = GraphicUtils.getTrianglesIndexesForSphere(latitudes, longitudes)
        val indexes2Array = GraphicUtils.getTrianglesIndexesForSphere(latitudes, longitudes)

        val vertices1Array = FloatArray(vertices1List.size * 3)
        var verticesIndex = 0
        for (threeDModel in vertices1List) {
            vertices1Array[verticesIndex++] = threeDModel.x
            vertices1Array[verticesIndex++] = threeDModel.y
            vertices1Array[verticesIndex++] = threeDModel.z
        }

        verticesIndex = 0
        val vertices2Array = FloatArray(vertices2List.size * 3)
        for (threeDModel in vertices2List) {
            vertices2Array[verticesIndex++] = threeDModel.x
            vertices2Array[verticesIndex++] = threeDModel.y
            vertices2Array[verticesIndex++] = threeDModel.z
        }

        val color1Array = arrayListOf<Float>()
        for (i in 0..latitudes) {
            var tColor = -0.5f
            val tColorIncrement = 1f / (longitudes + 1)
            for (j in 0..longitudes) {
                color1Array.add(1f)
                color1Array.add(abs(tColor) + 0.5f)
                color1Array.add(1f)
                color1Array.add(1f)
                tColor += tColorIncrement
            }
        }

        val color2Array = arrayListOf<Float>()
        for (i in 0..latitudes) {
            var tColor = -0.5f
            val tColorIncrement = 1f / (longitudes + 1)
            for (j in 0..longitudes) {
                color2Array.add(abs(tColor) + 0.5f)
                color2Array.add(1f)
                color2Array.add(1f)
                color2Array.add(1f)
                tColor += tColorIncrement
            }
        }

        indexes1Size = indexes1Array.size

        index1Buffer = IntBuffer.allocate(indexes1Array.size)
        index1Buffer?.put(indexes1Array.toIntArray())
        index1Buffer?.position(0)

        // initialize vertex byte buffer for shape coordinates
        val bb1: ByteBuffer =
            ByteBuffer.allocateDirect(vertices1Array.size * 4) // (# of coordinate values * 4 bytes per float)
        bb1.order(ByteOrder.nativeOrder())
        vertex1Buffer = bb1.asFloatBuffer()
        vertex1Buffer?.put(vertices1Array)
        vertex1Buffer?.position(0)
        vertexCount = vertices1Array.size / COORDINATES_PER_VERTEX

        //initialize color byte buffer
        val cb1: ByteBuffer =
            ByteBuffer.allocateDirect(color1Array.size * 4) // (# of coordinate values * 4 bytes per float)
        cb1.order(ByteOrder.nativeOrder())
        color1Buffer = cb1.asFloatBuffer()
        color1Buffer?.put(color1Array.toFloatArray())
        color1Buffer?.position(0)

        indexes2Size = indexes2Array.size

        index2Buffer = IntBuffer.allocate(indexes2Array.size)
        index2Buffer?.put(indexes2Array.toIntArray())
        index2Buffer?.position(0)

        // initialize vertex byte buffer for shape coordinates
        val bb2: ByteBuffer =
            ByteBuffer.allocateDirect(vertices2Array.size * 4) // (# of coordinate values * 4 bytes per float)
        bb2.order(ByteOrder.nativeOrder())
        vertex2Buffer = bb2.asFloatBuffer()
        vertex2Buffer?.put(vertices2Array)
        vertex2Buffer?.position(0)
        vertexCount = vertices2Array.size / COORDINATES_PER_VERTEX

        //initialize color byte buffer
        val cb2: ByteBuffer =
            ByteBuffer.allocateDirect(color2Array.size * 4) // (# of coordinate values * 4 bytes per float)
        cb2.order(ByteOrder.nativeOrder())
        color2Buffer = cb2.asFloatBuffer()
        color2Buffer?.put(color2Array.toFloatArray())
        color2Buffer?.position(0)

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
            GLES32.GL_FLOAT, false, VERTEX_STRIDE, vertex1Buffer
        )
        GLES32.glVertexAttribPointer(
            mColorHandle, COLOR_PER_VERTEX,
            GLES32.GL_FLOAT, false, COLOR_STRIDE, color1Buffer
        )
        // Draw the triangle
        GLES32.glDrawElements(
            GLES32.GL_TRIANGLES,
            indexes1Size,
            GLES32.GL_UNSIGNED_INT,
            index1Buffer
        )

        //set the attribute of the vertex to point to the vertex buffer
        GLES32.glVertexAttribPointer(
            mPositionHandle, COORDINATES_PER_VERTEX,
            GLES32.GL_FLOAT, false, VERTEX_STRIDE, vertex2Buffer
        )
        GLES32.glVertexAttribPointer(
            mColorHandle, COLOR_PER_VERTEX,
            GLES32.GL_FLOAT, false, COLOR_STRIDE, color2Buffer
        )
        // Draw the triangle
        GLES32.glDrawElements(
            GLES32.GL_TRIANGLES,
            indexes2Size,
            GLES32.GL_UNSIGNED_INT,
            index2Buffer
        )
    }

}