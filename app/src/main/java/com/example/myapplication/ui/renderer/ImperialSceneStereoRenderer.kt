package com.example.myapplication.ui.renderer

import android.content.res.Resources
import android.opengl.GLES32
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import com.example.myapplication.interfaces.OpenGLRenderer
import com.example.myapplication.interfaces.RenderTouchRotationListener
import com.example.myapplication.model.FullRotationModel
import com.example.myapplication.model.RotationModel
import com.example.myapplication.model.TouchRotationModel
import com.example.myapplication.model.TranslationModel
import com.example.myapplication.ui.frame.*
import timber.log.Timber
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * Created by Athenriel on 10/26/2022
 */
class ImperialSceneStereoRenderer(private val resources: Resources) : GLSurfaceView.Renderer,
    OpenGLRenderer, RenderTouchRotationListener {

    private val mMVPMatrix = FloatArray(16) //model view projection matrix
    private val mProjectionMatrix = FloatArray(16) //projection matrix
    private val mViewMatrix = FloatArray(16) //view matrix
    private val mMVMatrix = FloatArray(16) //model view matrix
    private val mModelMatrix = FloatArray(16) //model  matrix
    private var viewWidth = 1
    private var viewHeight = 1
    private var mSphere: TextureSphereFrame? = null
    private var mCharacter: FlatCharacterFrame? = null
    private var mGorilla: FlatGoldenGorillaFrame? = null
    private var mBackground: FlatSchoolBackgroundFrame? = null
    private var leftStereoView: StereoFrame? = null
    private var rightStereoView: StereoFrame? = null
    private var zoom = 0f
    private var rotationModel = RotationModel(0f, x = true, y = false, z = false)
    private var fullRotationModel: FullRotationModel? = null
    private var touchRotationModel: TouchRotationModel? = null
    private var translationModel: TranslationModel? = null

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        // Set the background frame color to black
        GLES32.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        mSphere = TextureSphereFrame(this, resources)
        mCharacter = FlatCharacterFrame(this, resources)
        mGorilla = FlatGoldenGorillaFrame(this, resources)
        mBackground = FlatSchoolBackgroundFrame(this, resources)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        // Adjust the view based on view window changes, such as screen rotation
        GLES32.glViewport(0, 0, width, height)
        if (width > height) {
            val planeRatio = (width / height).toFloat()
            Matrix.orthoM(
                mProjectionMatrix,
                0,
                -planeRatio,
                planeRatio,
                -1f,
                1f,
                StereoFrame.Z_NEAR,
                StereoFrame.Z_FAR
            )
        } else {
            val planeRatio = (height / width).toFloat()
            Matrix.orthoM(
                mProjectionMatrix,
                0,
                -1f,
                1f,
                -planeRatio,
                planeRatio,
                StereoFrame.Z_NEAR,
                StereoFrame.Z_FAR
            )
        }
        viewWidth = width
        viewHeight = height
        leftStereoView = StereoFrame(width.toFloat(), height.toFloat(), true, this)
        rightStereoView = StereoFrame(width.toFloat(), height.toFloat(), false, this)
        fullRotationModel = FullRotationModel()
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

        Matrix.translateM(
            mModelMatrix,
            0,
            0.0f,
            0.0f,
            StereoFrame.Z_DEPTH + zoom
        ) //move backward for 5 units

        touchRotationModel?.let { touchRotationModelSafe ->
            fullRotationModel = FullRotationModel(
                RotationModel(
                    touchRotationModelSafe.angleX,
                    x = true,
                    y = false,
                    z = false
                ), RotationModel(
                    touchRotationModelSafe.angleY,
                    x = false,
                    y = true,
                    z = false
                ), RotationModel(
                    0f,
                    x = false,
                    y = false,
                    z = false
                )
            )
        }

        // Calculate the projection and view transformation
        //calculate the model view matrix
        Matrix.multiplyMM(mMVMatrix, 0, mViewMatrix, 0, mModelMatrix, 0)
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVMatrix, 0)

        //draw the frame buffer
        GLES32.glViewport(0, 0, viewWidth, viewHeight)
        Matrix.setIdentityM(mModelMatrix, 0) //set the model matrix to an identity matrix
        leftStereoView?.setZoom(zoom)
        rightStereoView?.setZoom(zoom)
        leftStereoView?.let { leftStereoViewSafe ->
            drawFrameBuffer(leftStereoViewSafe, fullRotationModel, rotationModel)
        }
        rightStereoView?.let { rightStereoViewSafe ->
            drawFrameBuffer(rightStereoViewSafe, fullRotationModel, rotationModel)
        }

        //draw the framebuffer
        GLES32.glViewport(0, 0, viewWidth, viewHeight)
        GLES32.glClear(GLES32.GL_COLOR_BUFFER_BIT or GLES32.GL_DEPTH_BUFFER_BIT)
        leftStereoView?.draw()
        rightStereoView?.draw()
    }

    private fun drawFrameBuffer(
        stereoFrame: StereoFrame,
        fullRotationModel: FullRotationModel?,
        rotationModel: RotationModel
    ) {
        GLES32.glBindFramebuffer(GLES32.GL_FRAMEBUFFER, stereoFrame.getStereoFrameBuffer()[0])
        GLES32.glViewport(
            0,
            0,
            stereoFrame.getStereoViewWidth().toInt(),
            stereoFrame.getStereoViewHeight().toInt()
        )
        GLES32.glClear(GLES32.GL_COLOR_BUFFER_BIT or GLES32.GL_DEPTH_BUFFER_BIT)

        val sphereMVMMatrix = mMVMatrix.clone()
        val sphereMVPMatrix = mMVPMatrix.clone()
        val sphereMatrix = stereoFrame.getModelMatrix(fullRotationModel)
        val sphereRotationMatrix =
            stereoFrame.getModelMatrix(FullRotationModel.getFromRotationModel(rotationModel))
        Matrix.multiplyMM(sphereMVMMatrix, 0, sphereRotationMatrix, 0, sphereMatrix, 0)
        Matrix.multiplyMM(sphereMVMMatrix, 0, stereoFrame.getStereoViewMatrix(), 0, sphereMatrix, 0)
        Matrix.multiplyMM(
            sphereMVPMatrix,
            0,
            stereoFrame.getStereoProjectionMatrix(),
            0,
            sphereMVMMatrix,
            0
        )
        mSphere?.draw(sphereMVPMatrix)

        val characterMVMMatrix = mMVMatrix.clone()
        val characterMVPMatrix = mMVPMatrix.clone()
        val characterMatrix = stereoFrame.getModelMatrix(fullRotationModel)
        val characterRotationMatrix =
            stereoFrame.getModelMatrix(FullRotationModel.getFromRotationModel(rotationModel))
        Matrix.multiplyMM(characterMVMMatrix, 0, characterRotationMatrix, 0, characterMatrix, 0)
        Matrix.multiplyMM(
            characterMVMMatrix,
            0,
            stereoFrame.getStereoViewMatrix(),
            0,
            characterMatrix,
            0
        )
        Matrix.multiplyMM(
            characterMVPMatrix,
            0,
            stereoFrame.getStereoProjectionMatrix(),
            0,
            characterMVMMatrix,
            0
        )
        mCharacter?.draw(characterMVPMatrix, translationModel)

        val pMatrix = stereoFrame.getModelMatrix(fullRotationModel)
        Matrix.multiplyMM(mMVMatrix, 0, stereoFrame.getStereoViewMatrix(), 0, pMatrix, 0)
        Matrix.multiplyMM(mMVPMatrix, 0, stereoFrame.getStereoProjectionMatrix(), 0, mMVMatrix, 0)
        mGorilla?.draw(mMVPMatrix)
        mBackground?.draw(mMVPMatrix)

        GLES32.glBindFramebuffer(GLES32.GL_FRAMEBUFFER, 0) //render onto the screen
    }

    fun setTranslationModel(newTranslationModel: TranslationModel) {
        translationModel = newTranslationModel
    }

    fun setRotationModel(newRotationModel: RotationModel) {
        rotationModel = newRotationModel
    }

    fun setFullRotationModel(newRotationModel: FullRotationModel) {
        fullRotationModel = newRotationModel
    }

    fun setZoom(newZoom: Float) {
        zoom = newZoom
    }

    override fun checkGlError(operation: String) {
        val error = GLES32.glGetError()
        if (error != GLES32.GL_NO_ERROR) {
            Timber.e("LetterSAndLetterAStereoRenderer error %s operation %s", error, operation)
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