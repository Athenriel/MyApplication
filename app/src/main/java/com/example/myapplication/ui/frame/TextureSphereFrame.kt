package com.example.myapplication.ui.frame

import android.content.res.Resources
import android.opengl.GLES32
import com.example.myapplication.R
import com.example.myapplication.interfaces.OpenGLRenderer
import com.example.myapplication.utils.GraphicUtils
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.IntBuffer

/**
 * Created by Athenriel on 10/28/2022
 */
class TextureSphereFrame(
    private val renderer: OpenGLRenderer,
    resources: Resources
) {

    private var indexBuffer: IntBuffer? = null
    private var vertexBuffer: FloatBuffer? = null
    private var textureBuffer: FloatBuffer? = null

    private var mProgram = 0
    private var mPositionHandle = 0
    private var mMVPMatrixHandle = 0
    private var mTextureCoordinatesHandle = 0
    private var mTextureImageHandle = 0
    private var mTextureSamplerHandle = 0

    companion object {
        private const val VERTEX_SHADER_CODE = "attribute vec3 aVertexPosition;" +
                "uniform mat4 uMVPMatrix;" +
                "attribute vec2 aTextureCoordinate;" +
                "varying vec2 vTextureCoordinate;" +
                "void main() {" +
                "gl_Position = uMVPMatrix * vec4(aVertexPosition, 1.0);" +
                "vTextureCoordinate = aTextureCoordinate;" +
                "}"
        private const val FRAGMENT_SHADER_CODE = "precision mediump float;" +
                "varying vec2 vTextureCoordinate;" +
                "uniform sampler2D uTextureSampler;" + //texture
                "void main() {" +
                "vec4 fragmentColor = texture2D(uTextureSampler, vec2(vTextureCoordinate.s, vTextureCoordinate.t));" +
                "if (fragmentColor.a < 0.1) discard;" + //discard the fragment if the alpha channel is 0
                "gl_FragColor = fragmentColor;" +
                "}"
        private const val COORDINATES_PER_VERTEX =
            3 // number of coordinates per vertex in this array
        private const val VERTEX_STRIDE = COORDINATES_PER_VERTEX * 4 // 4 bytes per vertex
        private const val TEXTURE_PER_VERTEX = 2 //number of texture coordinates per vertex
        private const val TEXTURE_STRIDE = TEXTURE_PER_VERTEX * 4 //bytes per coordinate
    }

    private var indexesSphereSize = 0

    init {
        val radius = 1f
        val latitudes = 50
        val longitudes = 50
        val dist = 0f

        val verticesList = GraphicUtils.getVerticesForSphere(radius, latitudes, longitudes, dist)
        val indexesArray = GraphicUtils.getTrianglesIndexesForSphere(latitudes, longitudes)

        val verticesArray = FloatArray(verticesList.size * 3)
        var verticesIndex = 0
        for (threeDModel in verticesList) {
            verticesArray[verticesIndex++] = threeDModel.x
            verticesArray[verticesIndex++] = threeDModel.y + 1f
            verticesArray[verticesIndex++] = threeDModel.z
        }

        val textureArray = arrayListOf<Float>()
        for (i in 0..latitudes) {
            val textureIncrement = 1f / (longitudes + 1)
            for (j in 0..longitudes) {
                textureArray.add(0f + textureIncrement)
                textureArray.add(0f + textureIncrement)
            }
        }

        //Sphere
        indexesSphereSize = indexesArray.size

        indexBuffer = IntBuffer.allocate(indexesArray.size)
        indexBuffer?.put(indexesArray.toIntArray())
        indexBuffer?.position(0)

        // initialize vertex byte buffer for shape coordinates
        val bb: ByteBuffer =
            ByteBuffer.allocateDirect(verticesArray.size * 4) // (# of coordinate values * 4 bytes per float)
        bb.order(ByteOrder.nativeOrder())
        vertexBuffer = bb.asFloatBuffer()
        vertexBuffer?.put(verticesArray)
        vertexBuffer?.position(0)

        //initialize texture byte buffer
        val tb: ByteBuffer =
            ByteBuffer.allocateDirect(textureArray.size * 4) // (# of coordinate values * 4 bytes per float)
        tb.order(ByteOrder.nativeOrder())
        textureBuffer = tb.asFloatBuffer()
        textureBuffer?.put(textureArray.toFloatArray())
        textureBuffer?.position(0)

        // prepare shaders and OpenGL program
        val vertexShader = renderer.loadShader(
            GLES32.GL_VERTEX_SHADER, VERTEX_SHADER_CODE
        )
        val fragmentShader = renderer.loadShader(
            GLES32.GL_FRAGMENT_SHADER, FRAGMENT_SHADER_CODE
        )
        mProgram = GLES32.glCreateProgram() // create empty OpenGL Program
        GLES32.glAttachShader(mProgram, vertexShader) // add the vertex shader to program
        GLES32.glAttachShader(mProgram, fragmentShader) // add the fragment shader to program
        GLES32.glLinkProgram(mProgram) // link the  OpenGL program to create an executable
        GLES32.glUseProgram(mProgram) // Add program to OpenGL environment

        // get handle to vertex shader's aVertexPosition member
        mPositionHandle = GLES32.glGetAttribLocation(mProgram, "aVertexPosition")
        // Enable a handle to the triangle vertices
        GLES32.glEnableVertexAttribArray(mPositionHandle)
        // get handle to shape's transformation matrix
        mMVPMatrixHandle = GLES32.glGetUniformLocation(mProgram, "uMVPMatrix")
        renderer.checkGlError("glGetUniformLocation")
        renderer.checkGlError("glVertexAttribPointer")

        mTextureImageHandle =
            GraphicUtils.loadOpenGLTextureFromResources(resources, R.drawable.red_texture)

        GLES32.glTexParameteri(
            GLES32.GL_TEXTURE_2D,
            GLES32.GL_TEXTURE_MIN_FILTER,
            GLES32.GL_LINEAR
        )
        GLES32.glTexParameteri(
            GLES32.GL_TEXTURE_2D,
            GLES32.GL_TEXTURE_MAG_FILTER,
            GLES32.GL_NEAREST
        )

        mTextureCoordinatesHandle = GLES32.glGetAttribLocation(mProgram, "aTextureCoordinate")
        mTextureSamplerHandle = GLES32.glGetUniformLocation(mProgram, "uTextureSampler")
        renderer.checkGlError("glGetUniformLocation")

        GLES32.glEnableVertexAttribArray(mTextureCoordinatesHandle)
    }


    fun draw(mvpMatrix: FloatArray) {
        GLES32.glUseProgram(mProgram) // Add program to OpenGL environment

        // Apply the projection and view transformation
        GLES32.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0)
        renderer.checkGlError("glUniformMatrix4fv")

        GLES32.glActiveTexture(GLES32.GL_TEXTURE0) //set the active texture to unit 0
        GLES32.glBindTexture(GLES32.GL_TEXTURE_2D, mTextureImageHandle)
        GLES32.glUniform1i(mTextureSamplerHandle, 0)

        GLES32.glVertexAttribPointer(
            mTextureCoordinatesHandle,
            TEXTURE_PER_VERTEX,
            GLES32.GL_FLOAT,
            false,
            TEXTURE_STRIDE,
            textureBuffer
        )

        //set the attribute of the vertex to point to the vertex buffer
        GLES32.glVertexAttribPointer(
            mPositionHandle,
            COORDINATES_PER_VERTEX,
            GLES32.GL_FLOAT,
            false,
            VERTEX_STRIDE,
            vertexBuffer
        )
        // Draw the triangle
        GLES32.glDrawElements(
            GLES32.GL_TRIANGLES, indexesSphereSize, GLES32.GL_UNSIGNED_INT, indexBuffer
        )
    }

}