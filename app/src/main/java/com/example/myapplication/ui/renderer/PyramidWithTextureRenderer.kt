package com.example.myapplication.ui.renderer

import android.content.res.Resources
import android.opengl.GLES32
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import com.example.myapplication.interfaces.OpenGLRenderer
import com.example.myapplication.interfaces.RenderTouchRotationListener
import com.example.myapplication.model.ModelMatrixRotationModel
import com.example.myapplication.model.RotationModel
import com.example.myapplication.model.TouchRotationModel
import com.example.myapplication.ui.frame.PyramidWithTextureFrame
import timber.log.Timber
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * Created by Athenriel on 10/12/2022
 */
class PyramidWithTextureRenderer(private val resources: Resources) : GLSurfaceView.Renderer,
    RenderTouchRotationListener, OpenGLRenderer {

    private val mMVPMatrix = FloatArray(16) //model view projection matrix
    private val mProjectionMatrix = FloatArray(16) //projection matrix
    private val mViewMatrix = FloatArray(16) //view matrix
    private val mMVMatrix = FloatArray(16) //model view matrix
    private val mModelMatrix = FloatArray(16) //model  matrix
    private var mPyramid: PyramidWithTextureFrame? = null
    private var rotationModel: RotationModel? = null
    private var touchRotationModel: TouchRotationModel? = null

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        // Set the background frame color to black
        GLES32.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        mPyramid = PyramidWithTextureFrame(this, true, resources)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        // Adjust the view based on view window changes, such as screen rotation
        GLES32.glViewport(0, 0, width, height)
        val ratio = width.toFloat() / height
        val left = -ratio
        Matrix.frustumM(mProjectionMatrix, 0, left, ratio, -1.0f, 1.0f, 1.0f, 80.0f)
    }

    override fun onDrawFrame(gl: GL10?) {
        // Draw background color
        GLES32.glClear(GLES32.GL_COLOR_BUFFER_BIT or GLES32.GL_DEPTH_BUFFER_BIT)
        GLES32.glClearDepthf(1.0f) //set up the depth buffer
        GLES32.glEnable(GLES32.GL_DEPTH_TEST) //enable depth test (so, it will not look through the surfaces)
        GLES32.glDepthFunc(GLES32.GL_LEQUAL) //indicate what type of depth test

        Matrix.setIdentityM(
            mMVPMatrix,
            0
        ) //set the model view projection matrix to an identity matrix
        Matrix.setIdentityM(mMVMatrix, 0) //set the model view  matrix to an identity matrix
        Matrix.setIdentityM(mModelMatrix, 0) //set the model matrix to an identity matrix

        // Set the camera position (View matrix)
        Matrix.setLookAtM(
            mViewMatrix, 0,
            0.0f, 0f, 1.0f,  //camera is at (0,0,1)
            0f, 0f, 0f,  //looks at the origin
            0f, 1f, 0.0f
        ) //head is down (set to (0,1,0) to look from the top)

        Matrix.translateM(mModelMatrix, 0, 0.0f, 0.0f, -3f) //move backward for 3 units

        touchRotationModel?.let { touchRotationModelSafe ->
            ModelMatrixRotationModel(
                RotationModel(
                    touchRotationModelSafe.angleY,
                    x = false,
                    y = true,
                    z = false
                )
            ).rotateMatrix(mModelMatrix)
            ModelMatrixRotationModel(
                RotationModel(
                    touchRotationModelSafe.angleX,
                    x = true,
                    y = false,
                    z = false
                )
            ).rotateMatrix(mModelMatrix)
        } ?: run {
            rotationModel?.let { rotationModelSafe ->
                val modelMatrixRotationModel = ModelMatrixRotationModel(rotationModelSafe)
                modelMatrixRotationModel.rotateMatrix(mModelMatrix)
            }
        }

        // Calculate the projection and view transformation
        //calculate the model view matrix
        Matrix.multiplyMM(mMVMatrix, 0, mViewMatrix, 0, mModelMatrix, 0)
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVMatrix, 0)

        mPyramid?.draw(mMVPMatrix)
    }

    fun setRotationModel(newRotationModel: RotationModel) {
        rotationModel = newRotationModel
    }

    override fun checkGlError(operation: String) {
        val error = GLES32.glGetError()
        if (error != GLES32.GL_NO_ERROR) {
            Timber.e("PyramidWithTextureRenderer error %s operation %s", error, operation)
        }
    }

    override fun loadShader(type: Int, shaderCode: String): Int {
        // create a vertex shader  (GLES32.GL_VERTEX_SHADER) or a fragment shader (GLES32.GL_FRAGMENT_SHADER)
        val shader = GLES32.glCreateShader(type)
        GLES32.glShaderSource(
            shader,
            shaderCode
        ) // add the source code to the shader and compile it
        GLES32.glCompileShader(shader)
        return shader
    }

    override fun setTouchRotationModel(newTouchRotationModel: TouchRotationModel?) {
        touchRotationModel = newTouchRotationModel
    }

    override fun getTouchRotationModel(): TouchRotationModel? {
        return touchRotationModel
    }

}