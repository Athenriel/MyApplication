package com.example.myapplication.ui.frame

import android.graphics.PointF
import android.opengl.GLES32
import com.example.myapplication.ui.renderer.OpenGLLetterSRenderer
import com.example.myapplication.utils.GraphicUtils
import timber.log.Timber
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.IntBuffer

/**
 * Created by Athenriel on 9/23/2022
 * Does not work and I do not know why
 */
class LetterSFrame(private val renderer: OpenGLLetterSRenderer) {

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

    private val firstLetterSBezierPointList = listOf(
        PointF(2f, 0f),
        PointF(3.2f, 0f),
        PointF(4f, 0.8f),
        PointF(2.8f, 1.3f),
        PointF(2f, 1.5f),
        PointF(2f, 2f),
        PointF(3.2f, 2f)
    )

    private val secondLetterSBezierPointList = listOf(
        PointF(2f, 0.2f),
        PointF(2.2f, 0.2f),
        PointF(3.6f, 0.4f),
        PointF(2.8f, 1f),
        PointF(1.5f, 1.5f),
        PointF(1.6f, 2.2f),
        PointF(3.2f, 2.2f)
    )

    private var indexesSize = 0

    init {
        val frontVerticesList = arrayListOf<Float>()
        val frontIndexesList = arrayListOf<Int>()
        val vertices1 = GraphicUtils.getBezierCurveVertices(0.3f, firstLetterSBezierPointList.subList(0, 4))
        vertices1.removeLast()
        Timber.d("dcheck vertices1 %s", vertices1)
        val vertices2 = GraphicUtils.getBezierCurveVertices(0.3f, firstLetterSBezierPointList.subList(3, firstLetterSBezierPointList.size))
        Timber.d("dcheck vertices2 %s", vertices2)
        val vertices3 = GraphicUtils.getBezierCurveVertices(0.3f, secondLetterSBezierPointList.subList(0, 4))
        vertices3.removeLast()
        Timber.d("dcheck vertices3 %s", vertices3)
        val vertices4 = GraphicUtils.getBezierCurveVertices(0.3f, secondLetterSBezierPointList.subList(3, secondLetterSBezierPointList.size))
        Timber.d("dcheck vertices4 %s", vertices4)
        if (vertices1.size == vertices3.size) {
            for (i in vertices1.indices) {
                frontVerticesList.add(vertices1[i].x)
                frontVerticesList.add(vertices1[i].y)
                frontVerticesList.add(vertices1[i].z)
                frontVerticesList.add(vertices3[i].x)
                frontVerticesList.add(vertices3[i].y)
                frontVerticesList.add(vertices3[i].z)
            }
        }
        if (vertices2.size == vertices4.size) {
            for (i in vertices1.indices) {
                frontVerticesList.add(vertices2[i].x)
                frontVerticesList.add(vertices2[i].y)
                frontVerticesList.add(vertices2[i].z)
                frontVerticesList.add(vertices4[i].x)
                frontVerticesList.add(vertices4[i].y)
                frontVerticesList.add(vertices4[i].z)
            }
        }
        Timber.d("dcheck size %s frontVerticesList %s", frontVerticesList.size, frontVerticesList)
        for (i in 0 until frontVerticesList.size step 2) {
            if (i+3 < frontVerticesList.size) {
                frontIndexesList.add(i)
                frontIndexesList.add(i+2)
                frontIndexesList.add(i+1)
                frontIndexesList.add(i+1)
                frontIndexesList.add(i+2)
                frontIndexesList.add(i+3)
            }
        }
        Timber.d("dcheck size %s frontIndexesList %s", frontIndexesList.size, frontIndexesList)

        /*
        val backVerticesList = arrayListOf<Float>()
        val backIndexesList = arrayListOf<Int>()
        if (vertices1.size == vertices3.size) {
            for (i in vertices1.indices) {
                backVerticesList.add(vertices1[i].x)
                backVerticesList.add(vertices1[i].y)
                backVerticesList.add(-vertices1[i].z)
                backVerticesList.add(vertices3[i].x)
                backVerticesList.add(vertices3[i].y)
                backVerticesList.add(-vertices3[i].z)
            }
        }
        if (vertices2.size == vertices4.size) {
            for (i in vertices1.indices) {
                backVerticesList.add(vertices1[i].x)
                backVerticesList.add(vertices1[i].y)
                backVerticesList.add(-vertices1[i].z)
                backVerticesList.add(vertices3[i].x)
                backVerticesList.add(vertices3[i].y)
                backVerticesList.add(-vertices3[i].z)
            }
        }
        Timber.d("dcheck size %s backVerticesList %s", backVerticesList.size, backVerticesList)
        for (i in frontIndexesList) {
            backIndexesList.add(i + frontVerticesList.size)
        }
        Timber.d("dcheck size %s backIndexesList %s", backIndexesList.size, backIndexesList)
        */

        val colorList = arrayListOf<Float>()
        for (i in 0 until frontVerticesList.size) {
            colorList.add(0f)
            colorList.add(0f)
            colorList.add(1f)
            colorList.add(1f)
        }
        /*
        for (i in 0 until backVerticesList.size) {
            colorList.add(0f)
            colorList.add(1f)
            colorList.add(0f)
            colorList.add(1f)
        }
        */
        Timber.d("dcheck size %s colorList %s", colorList.size, colorList)

        val fullVerticesList = arrayListOf<Float>()
        fullVerticesList.addAll(frontVerticesList)
        //fullVerticesList.addAll(backVerticesList)
        val fullIndexesList = arrayListOf<Int>()
        fullIndexesList.addAll(frontIndexesList)
        //fullIndexesList.addAll(backIndexesList)

        val fullIndexArray = fullIndexesList.toIntArray()
        val colorArray = colorList.toFloatArray()
        val fullVerticesArray = FloatArray(fullVerticesList.size)
        var verticesIndex = 0
        for (vertex in fullVerticesList) {
            fullVerticesArray[verticesIndex++] = vertex
        }

        indexesSize = fullIndexArray.size

        indexBuffer = IntBuffer.allocate(fullIndexArray.size)
        indexBuffer?.put(fullIndexArray)
        indexBuffer?.position(0)

        // initialize vertex byte buffer for shape coordinates
        val bb: ByteBuffer =
            ByteBuffer.allocateDirect(fullVerticesArray.size * 4) // (# of coordinate values * 4 bytes per float)
        bb.order(ByteOrder.nativeOrder())
        vertexBuffer = bb.asFloatBuffer()
        vertexBuffer?.put(fullVerticesArray)
        vertexBuffer?.position(0)
        vertexCount = fullVerticesArray.size / COORDINATES_PER_VERTEX

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