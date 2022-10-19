package com.example.myapplication.ui.frame

import android.content.res.Resources
import android.opengl.GLES32
import com.example.myapplication.R
import com.example.myapplication.ui.renderer.PyramidAndCubeBlendRenderer
import com.example.myapplication.utils.GraphicUtils
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.IntBuffer

/**
 * Created by Athenriel on 10/14/2022
 */
class PyramidToBlendFrame(
    private val renderer: PyramidAndCubeBlendRenderer,
    resources: Resources
) {

    private var indexBuffer: IntBuffer? = null
    private var vertexBuffer: FloatBuffer? = null
    private var colorBuffer: FloatBuffer? = null
    private var normalBuffer: FloatBuffer? = null
    private var textureBuffer: FloatBuffer? = null

    private var mProgram = 0
    private var mPositionHandle = 0
    private var mMVPMatrixHandle = 0
    private var mColorHandle = 0
    private var mNormalHandle = 0

    private var mLightLocationHandle = 0
    private var mDiffuseColorHandle = 0
    private var mAmbientColorHandle = 0
    private var mSpecularColorHandle = 0
    private var mMaterialShininessHandle = 0
    private var mAttenuateHandle = 0

    private var mTextureCoordinatesHandle = 0
    private var mTextureImageHandle = 0
    private var mTextureSamplerHandle = 0
    private var mUseTextureHandle = 0

    private val lightLocation = floatArrayOf(0f, 0f, 0f)
    private val diffuseColor = floatArrayOf(0f, 0f, 0f, 0f)
    private val ambientColor = floatArrayOf(0f, 0f, 0f, 0f)
    private val specularColor = floatArrayOf(0f, 0f, 0f, 0f)
    private val attenuation = floatArrayOf(0f, 0f, 0f)
    private val materialShininess = 10f

    companion object {
        private const val VERTEX_SHADER_CODE =
            "attribute vec3 aVertexPosition;" +
                    "attribute vec4 aVertexColor;" +
                    "uniform mat4 uMVPMatrix;" +
                    "varying vec4 vColor;" +
                    "attribute vec3 aVertexNormal;" +
                    "uniform vec3 uLightSourceLocation;" + //location of the light source (for diffuse and specular light)
                    "uniform vec4 uDiffuseColor;" +
                    "varying vec4 vDiffuseColor;" +
                    "varying float vDiffuseLightWeighting;" +
                    "uniform vec4 uAmbientColor;" +
                    "varying vec4 vAmbientColor;" +
                    "uniform vec4 uSpecularColor;" +
                    "varying vec4 vSpecularColor;" +
                    "varying float vSpecularLightWeighting;" +
                    "uniform float uMaterialShininess;" +
                    "uniform vec3 uAttenuation;" +
                    "attribute vec2 aTextureCoordinate;" +
                    "varying vec2 vTextureCoordinate;" +
                    "void main() {" +
                    "gl_Position = uMVPMatrix * vec4(aVertexPosition, 1.0);" +
                    "vColor=aVertexColor;" +
                    "vec4 mvPosition = uMVPMatrix * vec4(aVertexPosition, 1.0);" +
                    "vec3 lightDirection = normalize(uLightSourceLocation - mvPosition.xyz);" +
                    "vec3 transformedNormal = normalize((uMVPMatrix * vec4(aVertexNormal, 0.0)).xyz);" +
                    "vAmbientColor = uAmbientColor;" +
                    "vDiffuseColor = uDiffuseColor;" +
                    "vSpecularColor = uSpecularColor;" +
                    "vec3 eyeDirection = normalize(-mvPosition.xyz);" +
                    "vec3 reflectionDirection = reflect(-lightDirection, transformedNormal);" +
                    "vec3 vertexToLightSource = mvPosition.xyz - uLightSourceLocation;" +
                    "float diff_light_dist = length(vertexToLightSource);" +
                    "float attenuation = 1.0 / (uAttenuation.x + uAttenuation.y * diff_light_dist + uAttenuation.z * diff_light_dist * diff_light_dist);" +
                    "vDiffuseLightWeighting = attenuation * max(dot(transformedNormal, lightDirection), 0.0);" +
                    "vSpecularLightWeighting = attenuation * pow(max(dot(reflectionDirection, eyeDirection), 0.0), uMaterialShininess);" +
                    "vTextureCoordinate = aTextureCoordinate;" +
                    "}"
        private const val FRAGMENT_SHADER_CODE =
            "precision mediump float;" +
                    "varying vec4 vColor;" +
                    "varying vec4 vDiffuseColor;" +
                    "varying float vDiffuseLightWeighting;" +
                    "varying vec4 vAmbientColor;" +
                    "varying vec4 vSpecularColor;" +
                    "varying float vSpecularLightWeighting;" +
                    "varying vec2 vTextureCoordinate;" +
                    "uniform bool uUseTexture;" +
                    "uniform sampler2D uTextureSampler;" + //texture
                    "void main() {" +
                    "vec4 diffuseColor = vDiffuseLightWeighting * vDiffuseColor;" +
                    "vec4 specularColor = vSpecularLightWeighting * vSpecularColor;" +
                    "if (uUseTexture) {" +
                    "vec4 fragmentColor = texture2D(uTextureSampler, vec2(vTextureCoordinate.s, vTextureCoordinate.t));" +
                    "gl_FragColor = fragmentColor + vAmbientColor + specularColor + diffuseColor;" +
                    "} else {" +
                    "gl_FragColor = vColor * vAmbientColor + specularColor + diffuseColor;" +
                    "}" +
                    "}"
        private const val COORDINATES_PER_VERTEX =
            3 // number of coordinates per vertex in this array
        private const val COLOR_PER_VERTEX = 4 // number of color values per vertex in this array
        private const val VERTEX_STRIDE = COORDINATES_PER_VERTEX * 4 // 4 bytes per vertex
        private const val COLOR_STRIDE = COLOR_PER_VERTEX * 4 // 4 bytes per vertex
        private const val TEXTURE_PER_VERTEX = 2 //number of texture coordinates per vertex
        private const val TEXTURE_STRIDE = TEXTURE_PER_VERTEX * 4 //bytes per coordinate
    }

    private val pyramidVertex = floatArrayOf(
        //front face
        0f, 2f, 0f, //0
        -1f, -1f, 1f, //1
        1f, -1f, 1f, //2
        //back face
        -1f, -1f, -1f, //3
        1f, -1f, -1f, //4
        //center of bottom
        0f, -1f, 0f //5
    )

    private val pyramidTextureCoordinates = floatArrayOf(
        //front face
        0.5f, 0.5f, //0
        0f, 1f, //1
        1f, 1f, //2
        //back face
        0f, 0f, //3
        1f, 0f, //4
        //center of bottom
        0.5f, 0.5f //5
    )

    private val pyramidIndices = intArrayOf(
        //front
        0, 1, 2,
        //right side
        0, 2, 4,
        //back
        0, 3, 4,
        //left side
        0, 3, 1,
        //bottom
        1, 2, 5,
        2, 4, 5,
        4, 3, 5,
        3, 1, 5
    )

    private val pyramidColor = floatArrayOf(
        //front face
        1f, 0f, 0f, 1f, //0
        1f, 0f, 0f, 1f, //1
        1f, 0f, 0f, 1f, //2
        //back face
        1f, 0f, 0f, 1f, //3
        1f, 0f, 0f, 1f, //4
        //center of bottom
        1f, 0f, 0f, 1f //5
    )

    init {
        //Pyramid
        indexBuffer = IntBuffer.allocate(pyramidIndices.size)
        indexBuffer?.put(pyramidIndices)
        indexBuffer?.position(0)

        // initialize vertex byte buffer for shape coordinates
        val bb: ByteBuffer =
            ByteBuffer.allocateDirect(pyramidVertex.size * 4) // (# of coordinate values * 4 bytes per float)
        bb.order(ByteOrder.nativeOrder())
        vertexBuffer = bb.asFloatBuffer()
        vertexBuffer?.put(pyramidVertex)
        vertexBuffer?.position(0)

        normalBuffer = bb.asFloatBuffer()
        normalBuffer?.put(pyramidVertex)
        normalBuffer?.position(0)

        //initialize color byte buffer
        val cb: ByteBuffer =
            ByteBuffer.allocateDirect(pyramidColor.size * 4) // (# of coordinate values * 4 bytes per float)
        cb.order(ByteOrder.nativeOrder())
        colorBuffer = cb.asFloatBuffer()
        colorBuffer?.put(pyramidColor)
        colorBuffer?.position(0)

        //initialize texture byte buffer
        val tb: ByteBuffer =
            ByteBuffer.allocateDirect(pyramidTextureCoordinates.size * 4) // (# of coordinate values * 4 bytes per float)
        tb.order(ByteOrder.nativeOrder())
        textureBuffer = tb.asFloatBuffer()
        textureBuffer?.put(pyramidTextureCoordinates)
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

        mNormalHandle = GLES32.glGetAttribLocation(mProgram, "aVertexNormal")
        GLES32.glEnableVertexAttribArray(mNormalHandle)
        renderer.checkGlError("glVertexAttribPointer")

        mTextureImageHandle =
            GraphicUtils.loadOpenGLTextureFromResources(resources, R.drawable.crane)

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

        mLightLocationHandle = GLES32.glGetUniformLocation(mProgram, "uLightSourceLocation")
        mDiffuseColorHandle = GLES32.glGetUniformLocation(mProgram, "uDiffuseColor")
        mAmbientColorHandle = GLES32.glGetUniformLocation(mProgram, "uAmbientColor")
        mSpecularColorHandle = GLES32.glGetUniformLocation(mProgram, "uSpecularColor")
        mMaterialShininessHandle = GLES32.glGetUniformLocation(mProgram, "uMaterialShininess")
        mAttenuateHandle = GLES32.glGetUniformLocation(mProgram, "uAttenuation")
        mTextureCoordinatesHandle = GLES32.glGetAttribLocation(mProgram, "aTextureCoordinate")
        mTextureSamplerHandle = GLES32.glGetUniformLocation(mProgram, "uTextureSampler")
        mUseTextureHandle = GLES32.glGetUniformLocation(mProgram, "uUseTexture")
        renderer.checkGlError("glGetUniformLocation")

        GLES32.glEnableVertexAttribArray(mTextureCoordinatesHandle)

        lightLocation[0] = 4f
        lightLocation[1] = 4f
        lightLocation[2] = 0f

        diffuseColor[0] = 1f
        diffuseColor[1] = 1f
        diffuseColor[2] = 1f
        diffuseColor[3] = 1f

        ambientColor[0] = 0.8f
        ambientColor[1] = 0.8f
        ambientColor[2] = 0.8f
        ambientColor[3] = 1f

        specularColor[0] = 1f
        specularColor[1] = 1f
        specularColor[2] = 1f
        specularColor[3] = 1f

        attenuation[0] = 1f
        attenuation[1] = 0.35f
        attenuation[2] = 0.44f
    }

    fun draw(mvpMatrix: FloatArray) {
        GLES32.glUseProgram(mProgram) // Add program to OpenGL environment

        // Apply the projection and view transformation
        GLES32.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0)
        renderer.checkGlError("glUniformMatrix4fv")

        GLES32.glActiveTexture(GLES32.GL_TEXTURE0) //set the active texture to unit 0
        GLES32.glBindTexture(GLES32.GL_TEXTURE_2D, mTextureImageHandle)
        GLES32.glUniform1i(mTextureSamplerHandle, 0)

        GLES32.glUniform1i(mUseTextureHandle, 0) // disable texture

        GLES32.glUniform3fv(mLightLocationHandle, 1, lightLocation, 0)
        GLES32.glUniform4fv(mDiffuseColorHandle, 1, diffuseColor, 0)
        GLES32.glUniform4fv(mAmbientColorHandle, 1, ambientColor, 0)
        GLES32.glUniform4fv(mSpecularColorHandle, 1, specularColor, 0)
        GLES32.glUniform1f(mMaterialShininessHandle, materialShininess)
        GLES32.glUniform3fv(mAttenuateHandle, 1, attenuation, 0)

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
        GLES32.glVertexAttribPointer(
            mNormalHandle,
            COORDINATES_PER_VERTEX,
            GLES32.GL_FLOAT,
            false,
            VERTEX_STRIDE,
            normalBuffer
        )
        GLES32.glVertexAttribPointer(
            mColorHandle, COLOR_PER_VERTEX, GLES32.GL_FLOAT, false, COLOR_STRIDE, colorBuffer
        )
        // Draw the triangle
        GLES32.glDrawElements(
            GLES32.GL_TRIANGLES, pyramidIndices.size, GLES32.GL_UNSIGNED_INT, indexBuffer
        )
    }

}