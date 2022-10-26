package com.example.myapplication.ui.graphics

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.Surface
import android.view.View
import com.example.myapplication.databinding.FragmentOpenglStereoImperialSceneBinding
import com.example.myapplication.model.FullRotationModel
import com.example.myapplication.model.RotationModel
import com.example.myapplication.model.TouchRotationModel
import com.example.myapplication.model.TranslationModel
import com.example.myapplication.ui.BaseFragment
import com.example.myapplication.ui.renderer.ImperialSceneStereoRenderer
import com.example.myapplication.utils.GraphicUtils
import timber.log.Timber
import java.util.*

/**
 * Created by Athenriel on 10/26/2022
 */
class OpenGLStereoImperialSceneFragment :
    BaseFragment<FragmentOpenglStereoImperialSceneBinding>(FragmentOpenglStereoImperialSceneBinding::inflate) {

    private var previousX = 0f
    private var previousY = 0f
    private var viewDestroyed = false
    private var scaleFactor = 1f
    private val timer = Timer()
    private var paused = false
    private var angleStep = 1f
    private var scaleGestureDetector: ScaleGestureDetector? = null
    private var sensor: Sensor? = null
    private var rotationEventListener: SensorEventListener? = null
    private lateinit var sensorManager: SensorManager
    private lateinit var renderer: ImperialSceneStereoRenderer
    private val fullRotationModel = FullRotationModel()
    private val rotationModel = RotationModel(0f, x = true, y = false, z = false)
    private var translationModelIndex = 0
    private val translationModelList = mutableListOf<TranslationModel>()

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDestroyed = false
        renderer = ImperialSceneStereoRenderer(resources)
        binding.openglStereoImperialSceneOpenglView.setRenderer(renderer)

        context?.let { contextSafe ->
            scaleGestureDetector = ScaleGestureDetector(contextSafe, object :
                ScaleGestureDetector.SimpleOnScaleGestureListener() {
                override fun onScale(detector: ScaleGestureDetector): Boolean {
                    scaleFactor = detector.scaleFactor
                    renderer.setZoom(scaleFactor)
                    updateView()
                    return super.onScale(detector)
                }
            })
            val manager = contextSafe.getSystemService(Context.SENSOR_SERVICE)
            if (manager is SensorManager) {
                sensorManager = manager
            }
        }

        buildTranslationList()

        val timerTask = object : TimerTask() {
            override fun run() {
                if (!paused) {
                    if (rotationModel.checkNextRotation()) {
                        rotationModel.prepareNextRotation(limitOneAxis = true)
                    }
                    rotationModel.incrementAngle(angleStep)
                    renderer.setRotationModel(rotationModel)
                    renderer.setTranslationModel(getNextTranslationModel())
                    updateView()
                }
            }
        }
        timer.scheduleAtFixedRate(timerTask, 100, 30)

        rotationEventListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                event?.let { eventSafe ->
                    val rotationMatrix = FloatArray(9)
                    SensorManager.getRotationMatrixFromVector(rotationMatrix, eventSafe.values)

                    var deviceRelativeAxisX = 0
                    var deviceRelativeAxisY = 0

                    activity?.let { activitySafe ->
                        when (activitySafe.window.windowManager.defaultDisplay.rotation) {
                            Surface.ROTATION_0 -> {
                                deviceRelativeAxisX = SensorManager.AXIS_Y
                                deviceRelativeAxisY = SensorManager.AXIS_X
                            }
                            Surface.ROTATION_90 -> {
                                deviceRelativeAxisX = SensorManager.AXIS_X
                                deviceRelativeAxisY = SensorManager.AXIS_MINUS_Y
                            }
                            Surface.ROTATION_180 -> {
                                deviceRelativeAxisX = SensorManager.AXIS_MINUS_Y
                                deviceRelativeAxisY = SensorManager.AXIS_MINUS_X
                            }
                            Surface.ROTATION_270 -> {
                                deviceRelativeAxisX = SensorManager.AXIS_MINUS_X
                                deviceRelativeAxisY = SensorManager.AXIS_Y
                            }
                            else -> {
                                deviceRelativeAxisX = SensorManager.AXIS_Y
                                deviceRelativeAxisY = SensorManager.AXIS_X
                            }
                        }
                    }

                    val adjustedRotation = FloatArray(9)
                    SensorManager.remapCoordinateSystem(
                        rotationMatrix,
                        deviceRelativeAxisX,
                        deviceRelativeAxisY,
                        adjustedRotation
                    )

                    val orientation = FloatArray(3)
                    SensorManager.getOrientation(adjustedRotation, orientation)
                    fullRotationModel.rotationX.setAngle(GraphicUtils.radiansToAngle(orientation[0]))
                    fullRotationModel.rotationY.setAngle(GraphicUtils.radiansToAngle(orientation[1]))
                    fullRotationModel.rotationZ.setAngle(GraphicUtils.radiansToAngle(orientation[2]))
                    renderer.setFullRotationModel(fullRotationModel)
                    updateView()
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                Timber.d("Sensor rotationEventListener accuracy changed to %s", accuracy)
            }
        }

        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR)

        binding.openglStereoImperialSceneOpenglView.setOnTouchListener { _, event ->
            if (event.pointerCount > 1) {
                scaleGestureDetector?.onTouchEvent(event)
            } else {
                if (event.action == MotionEvent.ACTION_MOVE) {
                    var deltaX = event.x - previousX
                    var deltaY = event.y - previousY
                    if (event.y > binding.openglStereoImperialSceneOpenglView.height / 2) {
                        deltaX *= -1
                    }
                    if (event.x < binding.openglStereoImperialSceneOpenglView.width / 2) {
                        deltaY *= -1
                    }
                    val previousTouchX = renderer.getTouchRotationModel()?.angleX ?: 0f
                    val previousTouchY = renderer.getTouchRotationModel()?.angleY ?: 0f
                    renderer.setTouchRotationModel(
                        TouchRotationModel(
                            previousTouchX + deltaY * binding.openglStereoImperialSceneOpenglView.getWidthScaleFactor(),
                            previousTouchY + deltaX * binding.openglStereoImperialSceneOpenglView.getWidthScaleFactor()
                        )
                    )
                    updateView()
                } else if (event.action == MotionEvent.ACTION_UP) {
                    renderer.setTouchRotationModel(null)
                }
                previousX = event.x
                previousY = event.y
            }
            true
        }
    }

    private fun buildTranslationList() {
        translationModelList.clear()
        translationModelIndex = 0

        translationModelList.add(TranslationModel())
        var previousIndex = 0
        //wait
        for (i in 0 until 100) {
            translationModelList.add(
                TranslationModel(
                    translationX = translationModelList[previousIndex].translationX + 0.00f,
                    translationY = translationModelList[previousIndex].translationY + 0.00f,
                    translationZ = translationModelList[previousIndex++].translationZ + 0.00f
                )
            )
        }
        //go east
        for (i in 0 until 200) {
            translationModelList.add(
                TranslationModel(
                    translationX = translationModelList[previousIndex].translationX + 0.01f,
                    translationY = translationModelList[previousIndex].translationY + 0.00f,
                    translationZ = translationModelList[previousIndex++].translationZ + 0.00f
                )
            )
        }
        //wait
        for (i in 0 until 100) {
            translationModelList.add(
                TranslationModel(
                    translationX = translationModelList[previousIndex].translationX + 0.00f,
                    translationY = translationModelList[previousIndex].translationY + 0.00f,
                    translationZ = translationModelList[previousIndex++].translationZ + 0.00f
                )
            )
        }
        //go south west
        for (i in 0 until 200) {
            translationModelList.add(
                TranslationModel(
                    translationX = translationModelList[previousIndex].translationX - 0.01f,
                    translationY = translationModelList[previousIndex].translationY - 0.00f,
                    translationZ = translationModelList[previousIndex++].translationZ - 0.01f
                )
            )
        }
        //wait (look at sphere)
        for (i in 0 until 200) {
            translationModelList.add(
                TranslationModel(
                    translationX = translationModelList[previousIndex].translationX - 0.00f,
                    translationY = translationModelList[previousIndex].translationY - 0.00f,
                    translationZ = translationModelList[previousIndex++].translationZ - 0.00f
                )
            )
        }
        //go north west
        for (i in 0 until 100) {
            translationModelList.add(
                TranslationModel(
                    translationX = translationModelList[previousIndex].translationX - 0.01f,
                    translationY = translationModelList[previousIndex].translationY - 0.00f,
                    translationZ = translationModelList[previousIndex++].translationZ + 0.01f
                )
            )
        }
        //go north west and up
        for (i in 0 until 100) {
            translationModelList.add(
                TranslationModel(
                    translationX = translationModelList[previousIndex].translationX - 0.01f,
                    translationY = translationModelList[previousIndex].translationY + 0.01f,
                    translationZ = translationModelList[previousIndex++].translationZ + 0.01f
                )
            )
        }
        //go north west and down
        for (i in 0 until 100) {
            translationModelList.add(
                TranslationModel(
                    translationX = translationModelList[previousIndex].translationX - 0.01f,
                    translationY = translationModelList[previousIndex].translationY - 0.01f,
                    translationZ = translationModelList[previousIndex++].translationZ + 0.01f
                )
            )
        }
        //go north west
        for (i in 0 until 50) {
            translationModelList.add(
                TranslationModel(
                    translationX = translationModelList[previousIndex].translationX - 0.01f,
                    translationY = translationModelList[previousIndex].translationY + 0.00f,
                    translationZ = translationModelList[previousIndex++].translationZ + 0.01f
                )
            )
        }
        //wait
        for (i in 0 until 100) {
            translationModelList.add(
                TranslationModel(
                    translationX = translationModelList[previousIndex].translationX + 0.00f,
                    translationY = translationModelList[previousIndex].translationY + 0.00f,
                    translationZ = translationModelList[previousIndex++].translationZ + 0.00f
                )
            )
        }
        //go south
        for (i in 0 until 100) {
            translationModelList.add(
                TranslationModel(
                    translationX = translationModelList[previousIndex].translationX - 0.00f,
                    translationY = translationModelList[previousIndex].translationY + 0.00f,
                    translationZ = translationModelList[previousIndex++].translationZ - 0.01f
                )
            )
        }
        //go south and up
        for (i in 0 until 50) {
            translationModelList.add(
                TranslationModel(
                    translationX = translationModelList[previousIndex].translationX - 0.00f,
                    translationY = translationModelList[previousIndex].translationY + 0.01f,
                    translationZ = translationModelList[previousIndex++].translationZ - 0.01f
                )
            )
        }
        //go south and down
        for (i in 0 until 50) {
            translationModelList.add(
                TranslationModel(
                    translationX = translationModelList[previousIndex].translationX - 0.00f,
                    translationY = translationModelList[previousIndex].translationY - 0.01f,
                    translationZ = translationModelList[previousIndex++].translationZ - 0.01f
                )
            )
        }
        //go east
        for (i in 0 until 100) {
            translationModelList.add(
                TranslationModel(
                    translationX = translationModelList[previousIndex].translationX + 0.01f,
                    translationY = translationModelList[previousIndex].translationY + 0.00f,
                    translationZ = translationModelList[previousIndex++].translationZ - 0.00f
                )
            )
        }
        //wait
        for (i in 0 until 100) {
            translationModelList.add(
                TranslationModel(
                    translationX = translationModelList[previousIndex].translationX + 0.00f,
                    translationY = translationModelList[previousIndex].translationY + 0.00f,
                    translationZ = translationModelList[previousIndex++].translationZ - 0.00f
                )
            )
        }
        //go south west
        for (i in 0 until 400) {
            translationModelList.add(
                TranslationModel(
                    translationX = translationModelList[previousIndex].translationX - 0.01f,
                    translationY = translationModelList[previousIndex].translationY + 0.00f,
                    translationZ = translationModelList[previousIndex++].translationZ - 0.01f
                )
            )
        }
        //wait
        for (i in 0 until 100) {
            translationModelList.add(
                TranslationModel(
                    translationX = translationModelList[previousIndex].translationX + 0.00f,
                    translationY = translationModelList[previousIndex].translationY + 0.00f,
                    translationZ = translationModelList[previousIndex++].translationZ - 0.00f
                )
            )
        }
    }

    private fun getNextTranslationModel(): TranslationModel {
        if (translationModelIndex >= translationModelList.size) {
            translationModelIndex = 0
        }
        return translationModelList[translationModelIndex++]
    }

    private fun updateView() {
        try {
            if (!viewDestroyed) {
                binding.openglStereoImperialSceneOpenglView.refresh()
            }
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(
            rotationEventListener,
            sensor,
            SensorManager.SENSOR_DELAY_NORMAL
        )
        paused = false
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(
            rotationEventListener
        )
        paused = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        timer.cancel()
        viewDestroyed = true
    }

}