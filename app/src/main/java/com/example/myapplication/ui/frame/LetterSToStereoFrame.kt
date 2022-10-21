package com.example.myapplication.ui.frame

import android.opengl.GLES32
import com.example.myapplication.interfaces.OpenGLRenderer
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.IntBuffer
import kotlin.math.pow

/**
 * Created by Athenriel on 9/23/2022
 * Does not work and I do not know why
 */
class LetterSToStereoFrame(private val renderer: OpenGLRenderer) {

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

    private var letterSVertices = FloatArray(1)
    private var letterSIndices = IntArray(1)
    private var letterSColors = FloatArray(1)

    private fun createCurve(controlPointsRight: FloatArray, controlPointsLeft: FloatArray) {
        val vertices = FloatArray(65535)
        val color = FloatArray(65535)
        val pIndex = IntArray(65535)
        var vertexindex = 0
        var colorindex = 0
        var indx = 0
        var controlptindex = 0
        val numSegments = controlPointsRight.size / 2 / 3
        var t: Double
        var x: Double
        var y: Double
        var xl: Double
        var yl: Double
        val z = 0.3
        var centrex = 0.0
        var centrey = 0.0
        var i = 0
        while (i < controlPointsRight.size) {
            centrex += controlPointsRight[i].toDouble()
            centrey += controlPointsRight[i + 1].toDouble()
            i += 2
        }
        centrex /= (controlPointsRight.size / 2.0).toFloat().toDouble()
        centrey /= (controlPointsRight.size / 2.0).toFloat().toDouble()
        for (segment in 0 until numSegments) {
            t = 0.0
            while (t < 1.0) {
                x =
                    (1.0 - t).pow(3.0) * controlPointsRight[controlptindex] + controlPointsRight[controlptindex + 2] * 3 * t * (1 - t).pow(
                        2.0
                    ) + controlPointsRight[controlptindex + 4] * 3 * t * t * (1 - t) + controlPointsRight[controlptindex + 6] * t.pow(
                        3.0
                    )
                y =
                    (1.0 - t).pow(3.0) * controlPointsRight[controlptindex + 1] + controlPointsRight[controlptindex + 3] * 3 * t * (1 - t).pow(
                        2.0
                    ) + controlPointsRight[controlptindex + 5] * 3 * t * t * (1 - t) + controlPointsRight[controlptindex + 7] * t.pow(
                        3.0
                    )
                xl =
                    (1.0 - t).pow(3.0) * controlPointsLeft[controlptindex] + controlPointsLeft[controlptindex + 2] * 3 * t * (1 - t).pow(
                        2.0
                    ) + controlPointsLeft[controlptindex + 4] * 3 * t * t * (1 - t) + controlPointsLeft[controlptindex + 6] * t.pow(
                        3.0
                    )
                yl =
                    (1.0 - t).pow(3.0) * controlPointsLeft[controlptindex + 1] + controlPointsLeft[controlptindex + 3] * 3 * t * (1 - t).pow(
                        2.0
                    ) + controlPointsLeft[controlptindex + 5] * 3 * t * t * (1 - t) + controlPointsLeft[controlptindex + 7] * t.pow(
                        3.0
                    )
                vertices[vertexindex++] = (x - centrex).toFloat()
                vertices[vertexindex++] = (y - centrey).toFloat()
                vertices[vertexindex++] = z.toFloat()
                vertices[vertexindex++] = (xl - centrex).toFloat()
                vertices[vertexindex++] = (yl - centrey).toFloat()
                vertices[vertexindex++] = z.toFloat()
                vertices[vertexindex++] = (x - centrex).toFloat()
                vertices[vertexindex++] = (y - centrey).toFloat()
                vertices[vertexindex++] = (-z).toFloat()
                vertices[vertexindex++] = (xl - centrex).toFloat()
                vertices[vertexindex++] = (yl - centrey).toFloat()
                vertices[vertexindex++] = (-z).toFloat()
                color[colorindex++] = 1f
                color[colorindex++] = 1f
                color[colorindex++] = 0f
                color[colorindex++] = 1f
                color[colorindex++] = 1f
                color[colorindex++] = 1f
                color[colorindex++] = 0f
                color[colorindex++] = 1f
                color[colorindex++] = 1f
                color[colorindex++] = 0f
                color[colorindex++] = 0f
                color[colorindex++] = 1f
                color[colorindex++] = 1f
                color[colorindex++] = 0f
                color[colorindex++] = 0f
                color[colorindex++] = 1f
                t += 0.1
            }
            controlptindex += 6
        }
        var v0 = 0
        var v1 = 1
        var v2 = 4
        var v3 = 5
        var v4 = 2
        var v5 = 3
        var v6 = 6
        var v7 = 7
        while (v7 < vertexindex / 3) {
            //the front
            pIndex[indx++] = v0
            pIndex[indx++] = v1
            pIndex[indx++] = v2
            pIndex[indx++] = v2
            pIndex[indx++] = v1
            pIndex[indx++] = v3
            //back
            pIndex[indx++] = v4
            pIndex[indx++] = v5
            pIndex[indx++] = v6
            pIndex[indx++] = v6
            pIndex[indx++] = v5
            pIndex[indx++] = v7
            //bottom
            pIndex[indx++] = v4
            pIndex[indx++] = v0
            pIndex[indx++] = v2
            pIndex[indx++] = v2
            pIndex[indx++] = v6
            pIndex[indx++] = v4
            //top
            pIndex[indx++] = v5
            pIndex[indx++] = v1
            pIndex[indx++] = v3
            pIndex[indx++] = v3
            pIndex[indx++] = v7
            pIndex[indx++] = v5
            v0 += 4
            v1 += 4
            v2 += 4
            v3 += 4
            v4 += 4
            v5 += 4
            v6 += 4
            v7 += 4
        }
        //cover bottom end
        pIndex[indx++] = 1
        pIndex[indx++] = 0
        pIndex[indx++] = 2
        pIndex[indx++] = 2
        pIndex[indx++] = 3
        pIndex[indx++] = 1
        //cover the top end
        /*
        pIndex[indx++] = v0
        pIndex[indx++] = v1
        pIndex[indx++] = v4
        pIndex[indx++] = v4
        pIndex[indx++] = v5
        pIndex[indx++] = v1
        */
        pIndex[indx++] = v1
        pIndex[indx++] = v0
        pIndex[indx++] = v4
        pIndex[indx++] = v4
        pIndex[indx++] = v5
        pIndex[indx++] = v1
        letterSVertices = vertices.copyOf(vertexindex)
        letterSIndices = pIndex.copyOf(indx)
        letterSColors = color.copyOf(colorindex)
    }

    init {
        val controlPointsRight = FloatArray(14)
        val controlPointsLeft = FloatArray(14)
        var ci = 0

        controlPointsLeft[ci] = 2f
        controlPointsRight[ci++] = 2f

        controlPointsLeft[ci] = 0.2f
        controlPointsRight[ci++] = 0f

        controlPointsLeft[ci] = 2.2f
        controlPointsRight[ci++] = 3.2f

        controlPointsLeft[ci] = 0.2f
        controlPointsRight[ci++] = 0f

        controlPointsLeft[ci] = 3.6f
        controlPointsRight[ci++] = 4f

        controlPointsLeft[ci] = 0.4f
        controlPointsRight[ci++] = 0.8f

        controlPointsLeft[ci] = 2.8f
        controlPointsRight[ci++] = 2.8f

        controlPointsLeft[ci] = 1f
        controlPointsRight[ci++] = 1.3f

        controlPointsLeft[ci] = 1.4f
        controlPointsRight[ci++] = 2f

        controlPointsLeft[ci] = 1.5f
        controlPointsRight[ci++] = 1.5f

        controlPointsLeft[ci] = 1.6f
        controlPointsRight[ci++] = 2f

        controlPointsLeft[ci] = 2.2f
        controlPointsRight[ci++] = 2f

        controlPointsLeft[ci] = 3.2f
        controlPointsRight[ci++] = 3.2f

        controlPointsLeft[ci] = 2.2f
        controlPointsRight[ci] = 2f

        createCurve(controlPointsRight, controlPointsLeft)

        indexBuffer = IntBuffer.allocate(letterSIndices.size)
        indexBuffer?.put(letterSIndices)
        indexBuffer?.position(0)

        // initialize vertex byte buffer for shape coordinates
        val bb: ByteBuffer =
            ByteBuffer.allocateDirect(letterSVertices.size * 4) // (# of coordinate values * 4 bytes per float)
        bb.order(ByteOrder.nativeOrder())
        vertexBuffer = bb.asFloatBuffer()
        vertexBuffer?.put(letterSVertices)
        vertexBuffer?.position(0)
        vertexCount = letterSVertices.size / COORDINATES_PER_VERTEX

        //initialize color byte buffer
        val cb: ByteBuffer =
            ByteBuffer.allocateDirect(letterSColors.size * 4) // (# of coordinate values * 4 bytes per float)
        cb.order(ByteOrder.nativeOrder())
        colorBuffer = cb.asFloatBuffer()
        colorBuffer?.put(letterSColors)
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
            letterSIndices.size,
            GLES32.GL_UNSIGNED_INT,
            indexBuffer
        )
    }

}