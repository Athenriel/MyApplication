package com.example.myapplication.ui.frame

import android.opengl.GLES32
import android.opengl.Matrix
import com.example.myapplication.interfaces.OpenGLRenderer
import com.example.myapplication.model.FullRotationModel
import com.example.myapplication.model.ModelMatrixRotationModel
import com.example.myapplication.utils.GraphicUtils
import timber.log.Timber
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.IntBuffer


/**
 * Created by Athenriel on 10/21/2022
 */
class StereoFrame(
    planeWidth: Float,
    planeHeight: Float,
    isLeft: Boolean,
    renderer: OpenGLRenderer
) {

    private var indexBuffer: IntBuffer? = null
    private var vertexBuffer: FloatBuffer? = null
    private var textureBuffer: FloatBuffer? = null

    private var mProgram = 0
    private var mPositionHandle = 0
    private var mMVPMatrixHandle = 0
    private var mTextureHandle = 0
    private var mTextureCoordinatesHandle = 0

    private var width = 0f
    private var height = 0f

    private val frameBuffer = IntArray(1)
    private val frameBufferTextureId = IntArray(2)
    private val renderBuffer = IntArray(1)

    //for drawing the object in the framebuffer
    private val frameModelMatrix = FloatArray(16)
    private val frameViewMatrix = FloatArray(16)
    private val projectionMatrix = FloatArray(16)

    //for drawing the framebuffer as a surface on the screen
    private val mMVPMatrix = FloatArray(16)
    private val mModelMatrix = FloatArray(16)
    private val mViewMatrix = FloatArray(16)
    private val mDisplayProjectionMatrix = FloatArray(16)

    private var depthZ = -5f
    private var aspectRatio = 1f
    private var nearZ = 1f
    private var farZ = 80f
    private var screenZ = -10f
    private var intraOcularDistance = 0.8f
    private var modelTranslation = 0f
    private var zoom = 0f

    companion object {
        private const val VERTEX_SHADER_CODE =
            "attribute vec3 aVertexPosition;" +
                    "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 aVertexColor;" +
                    "attribute vec2 aTextureCoordinate; " + //texture coordinate
                    "varying vec2 vTextureCoordinate;" +
                    "void main() {" +
                    "gl_Position = uMVPMatrix * vec4(aVertexPosition, 1.0);" +
                    "vTextureCoordinate=aTextureCoordinate;" +
                    "}"
        private const val FRAGMENT_SHADER_CODE =
            "precision lowp float;" +
                    "varying vec2 vTextureCoordinate;" +
                    "uniform sampler2D uTextureSampler;" + //texture
                    "void main() {" +
                    "vec4 fragmentColor = texture2D(uTextureSampler, vec2(vTextureCoordinate.s, vTextureCoordinate.t));" + //load the color texture
                    "gl_FragColor = vec4(fragmentColor.rgb, fragmentColor.a);" + //the fragment color
                    "}"
        private const val COORDINATES_PER_VERTEX =
            3 // number of coordinates per vertex in this array
        private const val VERTEX_STRIDE = COORDINATES_PER_VERTEX * 4 // 4 bytes per vertex
        private const val TEXTURE_PER_VERTEX = 2 //number of texture coordinates per vertex
        private const val TEXTURE_STRIDE = TEXTURE_PER_VERTEX * 4 //bytes per coordinate
    }

    private val plane2DVertex = floatArrayOf(
        //front face
        -1f, -1f, 1f, //0
        1f, -1f, 1f, //1
        1f, 1f, 1f, //2
        -1f, 1f, 1f //3
    )

    private val plane2DTextureCoordinates = floatArrayOf(
        //front face
        0f, 0f, //0
        1f, 0f, //1
        1f, 1f, //2
        0f, 1f //3
    )

    private val plane2DIndices = intArrayOf(
        0, 1, 2,
        0, 2, 3
    )

    private fun getFrustumShift(): Float {
        return -(intraOcularDistance / 2) * nearZ / screenZ
    }

    init {
        indexBuffer = IntBuffer.allocate(plane2DIndices.size)
        indexBuffer?.put(plane2DIndices)
        indexBuffer?.position(0)

        // initialize vertex byte buffer for shape coordinates
        val bb: ByteBuffer =
            ByteBuffer.allocateDirect(plane2DVertex.size * 4) // (# of coordinate values * 4 bytes per float)
        bb.order(ByteOrder.nativeOrder())
        vertexBuffer = bb.asFloatBuffer()
        vertexBuffer?.put(plane2DVertex)
        vertexBuffer?.position(0)

        //initialize texture byte buffer
        val tb: ByteBuffer =
            ByteBuffer.allocateDirect(plane2DTextureCoordinates.size * 4) // (# of coordinate values * 4 bytes per float)
        tb.order(ByteOrder.nativeOrder())
        textureBuffer = tb.asFloatBuffer()
        textureBuffer?.put(plane2DTextureCoordinates)
        textureBuffer?.position(0)

        // prepare shaders and OpenGL program
        val vertexShader: Int = renderer.loadShader(GLES32.GL_VERTEX_SHADER, VERTEX_SHADER_CODE)
        val fragmentShader: Int =
            renderer.loadShader(GLES32.GL_FRAGMENT_SHADER, FRAGMENT_SHADER_CODE)
        mProgram = GLES32.glCreateProgram() // create empty OpenGL Program
        GLES32.glAttachShader(mProgram, vertexShader) // add the vertex shader to program
        GLES32.glAttachShader(mProgram, fragmentShader) // add the fragment shader to program
        GLES32.glLinkProgram(mProgram) // link the  OpenGL program to create an executable
        GLES32.glUseProgram(mProgram) // Add program to OpenGL environment

        // get handle to vertex shader's vPosition member
        mPositionHandle = GLES32.glGetAttribLocation(mProgram, "aVertexPosition")
        // Enable a handle to the triangle vertices
        GLES32.glEnableVertexAttribArray(mPositionHandle)
        // Prepare the triangle coordinate data
        GLES32.glVertexAttribPointer(
            mPositionHandle,
            COORDINATES_PER_VERTEX,
            GLES32.GL_FLOAT,
            false,
            VERTEX_STRIDE,
            vertexBuffer
        )
        // get handle to shape's transformation matrix
        mTextureCoordinatesHandle =
            GLES32.glGetAttribLocation(mProgram, "aTextureCoordinate") //texture coordinates
        GLES32.glEnableVertexAttribArray(mTextureCoordinatesHandle)
        GLES32.glVertexAttribPointer(
            mTextureCoordinatesHandle,
            TEXTURE_PER_VERTEX,
            GLES32.GL_FLOAT,
            false,
            TEXTURE_STRIDE,
            textureBuffer
        )
        mTextureHandle = GLES32.glGetUniformLocation(mProgram, "uTextureSampler") //texture
        mMVPMatrixHandle = GLES32.glGetUniformLocation(mProgram, "uMVPMatrix")

        width = planeWidth / 2f
        height = planeHeight
        aspectRatio = width / height
        if (planeWidth > planeHeight) {
            val planeRatio = planeWidth / planeHeight
            Matrix.orthoM(mDisplayProjectionMatrix, 0, -planeRatio, planeRatio, -1f, 1f, -10f, 200f)
        } else {
            val planeRatio = planeHeight / planeWidth
            Matrix.orthoM(mDisplayProjectionMatrix, 0, -1f, 1f, -planeRatio, planeRatio, -10f, 200f)
        }

        Matrix.setLookAtM(
            mViewMatrix, 0, 0f, 0f, 0.1f,
            0f, 0f, 0f,  //looks at the origin
            0f, 1f, 0f
        ) //head is down (set to (0,1,0) to look from the top)
        Matrix.setIdentityM(mModelMatrix, 0) //set the model matrix to an identity matrix
        Matrix.scaleM(mModelMatrix, 0, width / height, 1f, 1f)

        if (isLeft) {
            Matrix.translateM(mModelMatrix, 0, -1f, 0f, 0f)
        } else {
            Matrix.translateM(mModelMatrix, 0, 1f, 0f, 0f)
        }
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0)
        Matrix.multiplyMM(mMVPMatrix, 0, mDisplayProjectionMatrix, 0, mMVPMatrix, 0)

        val frustumShift = getFrustumShift()
        if (isLeft) {
            Matrix.frustumM(
                projectionMatrix,
                0,
                frustumShift - aspectRatio,
                frustumShift + aspectRatio,
                -1f,
                1f,
                nearZ,
                farZ
            )
            Matrix.setLookAtM(
                frameViewMatrix, 0,
                -intraOcularDistance / 2f, 0f, 0.1f,
                0f, 0f, screenZ, //looks at the screen
                0f, 1f, 0f
            ) //head is down (set to (0,1,0) to look from the top)
        } else {
            Matrix.frustumM(
                projectionMatrix,
                0,
                -aspectRatio - frustumShift,
                aspectRatio - frustumShift,
                -1f,
                1f,
                nearZ,
                farZ
            )

            Matrix.setLookAtM(
                frameViewMatrix, 0,
                intraOcularDistance / 2f, 0f, 0.1f,
                0f, 0f, screenZ, //looks at the screen
                0f, 1f, 0f
            ) //head is down (set to (0,1,0) to look from the top)
        }

        modelTranslation = -intraOcularDistance / 2
        Matrix.setIdentityM(frameModelMatrix, 0) //set the model matrix to an identity matrix
        Matrix.translateM(frameModelMatrix, 0, modelTranslation, 0f, depthZ + zoom)
        createFrameBuffers()
    }

    private fun createFrameBuffers() {
        GLES32.glGenTextures(1, frameBufferTextureId, 0) //generate 2 texture objects
        GLES32.glGenFramebuffers(1, frameBuffer, 0) //generate a framebuffer object
        //bind the framebuffer for drawing
        GLES32.glBindFramebuffer(GLES32.GL_DRAW_FRAMEBUFFER, frameBuffer[0])
        GraphicUtils.initializeOpenGLTexture(
            GLES32.GL_TEXTURE1,
            frameBufferTextureId[0],
            width,
            height,
            GLES32.GL_RGBA,
            GLES32.GL_UNSIGNED_BYTE
        )
        GLES32.glFramebufferTexture2D(
            GLES32.GL_FRAMEBUFFER,
            GLES32.GL_COLOR_ATTACHMENT0,
            GLES32.GL_TEXTURE_2D,
            frameBufferTextureId[0],
            0
        )
        GLES32.glGenRenderbuffers(1, renderBuffer, 0)
        GLES32.glBindRenderbuffer(GLES32.GL_RENDERBUFFER, renderBuffer[0])
        GLES32.glRenderbufferStorage(
            GLES32.GL_RENDERBUFFER,
            GLES32.GL_DEPTH_COMPONENT24,
            width.toInt(),
            height.toInt()
        )
        GLES32.glFramebufferRenderbuffer(
            GLES32.GL_FRAMEBUFFER,
            GLES32.GL_DEPTH_ATTACHMENT,
            GLES32.GL_RENDERBUFFER,
            renderBuffer[0]
        )
        val status = GLES32.glCheckFramebufferStatus(GLES32.GL_FRAMEBUFFER)
        if (status != GLES32.GL_FRAMEBUFFER_COMPLETE) {
            Timber.e("Error in createFrameBuffers")
        }
        GLES32.glBindTexture(GLES32.GL_TEXTURE_2D, 0) //unbind the texture
        GLES32.glBindFramebuffer(GLES32.GL_FRAMEBUFFER, 0) //unbind the framebuffer
    }

    fun getStereoViewWidth(): Float {
        return width
    }

    fun getStereoViewHeight(): Float {
        return height
    }

    fun getStereoFrameBuffer(): IntArray {
        return frameBuffer
    }

    fun getStereoViewMatrix(): FloatArray {
        return frameViewMatrix
    }

    fun getStereoProjectionMatrix(): FloatArray {
        return projectionMatrix
    }

    fun setZoom(newZoom: Float) {
        zoom = newZoom
    }

    fun getModelMatrix(fullRotationModel: FullRotationModel?): FloatArray {
        //get the model matrix to draw the object onto the frame buffer
        val planeModelMatrix = FloatArray(16)
        Matrix.setIdentityM(planeModelMatrix, 0) //set the model matrix to an identity matrix
        fullRotationModel?.let { fullRotationModelSafe ->
            if (fullRotationModelSafe.rotationY.isValid()) {
                val rotationMatrix = FloatArray(16)
                Matrix.setRotateM(
                    rotationMatrix,
                    0,
                    fullRotationModelSafe.rotationY.getAngle(),
                    fullRotationModelSafe.rotationY.getXValue(),
                    fullRotationModelSafe.rotationY.getYValue(),
                    fullRotationModelSafe.rotationY.getZValue()
                )
                Matrix.multiplyMM(
                    planeModelMatrix,
                    0,
                    frameModelMatrix,
                    0,
                    rotationMatrix,
                    0
                )
            }
            ModelMatrixRotationModel(fullRotationModelSafe.rotationX).rotateMatrix(planeModelMatrix)
            ModelMatrixRotationModel(fullRotationModelSafe.rotationZ).rotateMatrix(planeModelMatrix)
        }
        return planeModelMatrix
    }

    fun draw() {
        Matrix.setIdentityM(frameModelMatrix, 0) //set the model matrix to an identity matrix
        Matrix.translateM(frameModelMatrix, 0, modelTranslation, 0f, depthZ + zoom)

        GLES32.glUseProgram(mProgram) // Add program to OpenGL environment
        // Apply the projection and view transformation
        GLES32.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0)
        GLES32.glActiveTexture(GLES32.GL_TEXTURE1) //set the active texture to unit 0
        GLES32.glBindTexture(
            GLES32.GL_TEXTURE_2D,
            frameBufferTextureId[0]
        ) //bind the texture to this unit
        GLES32.glUniform1i(mTextureHandle, 1) //tell the uniform sampler to use this texture i
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
            mPositionHandle, COORDINATES_PER_VERTEX,
            GLES32.GL_FLOAT, false, VERTEX_STRIDE, vertexBuffer
        )
        // Draw the 2D plane
        GLES32.glDrawElements(
            GLES32.GL_TRIANGLES,
            plane2DIndices.size,
            GLES32.GL_UNSIGNED_INT,
            indexBuffer
        )
    }

}