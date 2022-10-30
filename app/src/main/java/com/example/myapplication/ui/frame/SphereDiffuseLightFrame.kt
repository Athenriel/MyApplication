package com.example.myapplication.ui.frame

import android.opengl.GLES32
import com.example.myapplication.interfaces.OpenGLRenderer
import com.example.myapplication.utils.GraphicUtils
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.IntBuffer

/**
 * Created by Athenriel on 10/28/2022
 */
class SphereDiffuseLightFrame(private val renderer: OpenGLRenderer) {

    private var indexSphere1Buffer: IntBuffer? = null
    private var vertexSphere1Buffer: FloatBuffer? = null
    private var colorSphere1Buffer: FloatBuffer? = null
    private var normalSphere1Buffer: FloatBuffer? = null

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

    private val lightLocation = floatArrayOf(0f, 0f, 0f)
    private val diffuseColor = floatArrayOf(0f, 0f, 0f, 0f)
    private val ambientColor = floatArrayOf(0f, 0f, 0f)
    private val specularColor = floatArrayOf(0f, 0f, 0f, 0f)
    private val attenuation = floatArrayOf(0f, 0f, 0f)
    private val materialShininess = 10f

    companion object {
        private const val VERTEX_SHADER_CODE = "attribute vec3 aVertexPosition;" +
                "attribute vec4 aVertexColor;" +
                "uniform mat4 uMVPMatrix;" +
                "varying vec4 vColor;" +
                "attribute vec3 aVertexNormal;" +
                "uniform vec3 uLightSourceLocation;" + //location of the light source (for diffuse and specular light)
                "uniform vec4 uDiffuseColor;" +
                "varying vec4 vDiffuseColor;" +
                "varying float vDiffuseLightWeighting;" +
                "uniform vec3 uAmbientColor;" +
                "varying vec3 vAmbientColor;" +
                "uniform vec4 uSpecularColor;" +
                "varying vec4 vSpecularColor;" +
                "varying float vSpecularLightWeighting;" +
                "uniform float uMaterialShininess;" +
                "uniform vec3 uAttenuation;" +
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
                "}"
        private const val FRAGMENT_SHADER_CODE = "precision mediump float;" +
                "varying vec4 vColor;" +
                "varying vec4 vDiffuseColor;" +
                "varying float vDiffuseLightWeighting;" +
                "varying vec3 vAmbientColor;" +
                "varying vec4 vSpecularColor;" +
                "varying float vSpecularLightWeighting;" +
                "void main() {" +
                "vec4 diffuseColor = vDiffuseLightWeighting * vDiffuseColor;" +
                "vec4 specularColor = vSpecularLightWeighting * vSpecularColor;" +
                "gl_FragColor = vec4(vColor.xyz * vAmbientColor, 1) + specularColor + diffuseColor;" +
                "}"
        private const val COORDINATES_PER_VERTEX =
            3 // number of coordinates per vertex in this array
        private const val COLOR_PER_VERTEX =
            4 // number of color values per vertex in this array
        private const val VERTEX_STRIDE = COORDINATES_PER_VERTEX * 4 // 4 bytes per vertex
        private const val COLOR_STRIDE = COLOR_PER_VERTEX * 4 // 4 bytes per vertex
    }

    private var indexesSphereSize = 0

    init {
        val radius = 2f
        val latitudes = 50
        val longitudes = 50
        val dist = 0f

        val verticesList = GraphicUtils.getVerticesForSphere(radius, latitudes, longitudes, dist)
        val indexesArray = GraphicUtils.getTrianglesIndexesForSphere(latitudes, longitudes)

        val verticesArray = FloatArray(verticesList.size * 3)
        var verticesIndex = 0
        for (threeDModel in verticesList) {
            verticesArray[verticesIndex++] = threeDModel.x
            verticesArray[verticesIndex++] = threeDModel.y
            verticesArray[verticesIndex++] = threeDModel.z
        }

        val colorArray = arrayListOf<Float>()
        for (i in 0..latitudes) {
            for (j in 0..longitudes) {
                colorArray.add(1f)
                colorArray.add(0f)
                colorArray.add(0f)
                colorArray.add(1f)
            }
        }

        //Sphere
        indexesSphereSize = indexesArray.size

        indexSphere1Buffer = IntBuffer.allocate(indexesArray.size)
        indexSphere1Buffer?.put(indexesArray.toIntArray())
        indexSphere1Buffer?.position(0)

        // initialize vertex byte buffer for shape coordinates
        val bb1: ByteBuffer =
            ByteBuffer.allocateDirect(verticesArray.size * 4) // (# of coordinate values * 4 bytes per float)
        bb1.order(ByteOrder.nativeOrder())
        vertexSphere1Buffer = bb1.asFloatBuffer()
        vertexSphere1Buffer?.put(verticesArray)
        vertexSphere1Buffer?.position(0)

        normalSphere1Buffer = bb1.asFloatBuffer()
        normalSphere1Buffer?.put(verticesArray)
        normalSphere1Buffer?.position(0)

        //initialize color byte buffer
        val cb1: ByteBuffer =
            ByteBuffer.allocateDirect(colorArray.size * 4) // (# of coordinate values * 4 bytes per float)
        cb1.order(ByteOrder.nativeOrder())
        colorSphere1Buffer = cb1.asFloatBuffer()
        colorSphere1Buffer?.put(colorArray.toFloatArray())
        colorSphere1Buffer?.position(0)

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

        mNormalHandle = GLES32.glGetAttribLocation(mProgram, "aVertexNormal")
        GLES32.glEnableVertexAttribArray(mNormalHandle)
        renderer.checkGlError("glVertexAttribPointer")

        mLightLocationHandle = GLES32.glGetUniformLocation(mProgram, "uLightSourceLocation")
        mDiffuseColorHandle = GLES32.glGetUniformLocation(mProgram, "uDiffuseColor")
        mAmbientColorHandle = GLES32.glGetUniformLocation(mProgram, "uAmbientColor")
        mSpecularColorHandle = GLES32.glGetUniformLocation(mProgram, "uSpecularColor")
        mMaterialShininessHandle = GLES32.glGetUniformLocation(mProgram, "uMaterialShininess")
        mAttenuateHandle = GLES32.glGetUniformLocation(mProgram, "uAttenuation")

        lightLocation[0] = 0f
        lightLocation[1] = 0f
        lightLocation[2] = -10f

        diffuseColor[0] = 1f
        diffuseColor[1] = 1f
        diffuseColor[2] = 1f
        diffuseColor[3] = 1f

        ambientColor[0] = 0.6f
        ambientColor[1] = 0.6f
        ambientColor[2] = 0.6f

        specularColor[0] = 1f
        specularColor[1] = 1f
        specularColor[2] = 1f
        specularColor[3] = 1f

        attenuation[0] = 1f
        attenuation[1] = 0.14f
        attenuation[2] = 0.07f
    }

    fun draw(mvpMatrix: FloatArray) {
        // Apply the projection and view transformation
        GLES32.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0)
        renderer.checkGlError("glUniformMatrix4fv")

        GLES32.glUniform3fv(mLightLocationHandle, 1, lightLocation, 0)
        GLES32.glUniform4fv(mDiffuseColorHandle, 1, diffuseColor, 0)
        GLES32.glUniform3fv(mAmbientColorHandle, 1, ambientColor, 0)
        GLES32.glUniform4fv(mSpecularColorHandle, 1, specularColor, 0)
        GLES32.glUniform1f(mMaterialShininessHandle, materialShininess)
        GLES32.glUniform3fv(mAttenuateHandle, 1, attenuation, 0)

        //Sphere
        //set the attribute of the vertex to point to the vertex buffer
        GLES32.glVertexAttribPointer(
            mPositionHandle, COORDINATES_PER_VERTEX,
            GLES32.GL_FLOAT, false, VERTEX_STRIDE, vertexSphere1Buffer
        )
        GLES32.glVertexAttribPointer(
            mNormalHandle, COORDINATES_PER_VERTEX,
            GLES32.GL_FLOAT, false, VERTEX_STRIDE, normalSphere1Buffer
        )
        GLES32.glVertexAttribPointer(
            mColorHandle, COLOR_PER_VERTEX,
            GLES32.GL_FLOAT, false, COLOR_STRIDE, colorSphere1Buffer
        )
        // Draw the triangle
        GLES32.glDrawElements(
            GLES32.GL_TRIANGLES,
            indexesSphereSize,
            GLES32.GL_UNSIGNED_INT,
            indexSphere1Buffer
        )
    }

}