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
import com.example.myapplication.databinding.FragmentOpenglBlendBinding
import com.example.myapplication.model.FullRotationModel
import com.example.myapplication.model.TouchRotationModel
import com.example.myapplication.ui.BaseFragment
import com.example.myapplication.ui.renderer.PyramidAndCubeBlendRenderer
import com.example.myapplication.utils.GraphicUtils
import timber.log.Timber
import java.util.*

/**
 * Created by Athenriel on 10/14/2022
 */
class OpenGLBlendFragment :
    BaseFragment<FragmentOpenglBlendBinding>(FragmentOpenglBlendBinding::inflate) {

    private var previousX = 0f
    private var previousY = 0f
    private var viewDestroyed = false
    private var scaleFactor = 1f
    private var scaleGestureDetector: ScaleGestureDetector? = null
    private var sensor: Sensor? = null
    private lateinit var renderer: PyramidAndCubeBlendRenderer
    private lateinit var sensorManager: SensorManager
    private var rotationEventListener: SensorEventListener? = null
    private val fullRotationModel = FullRotationModel()

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDestroyed = false
        renderer = PyramidAndCubeBlendRenderer(resources)
        binding.openglBlendOpenglView.setRenderer(renderer)

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

        rotationEventListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                event?.let { eventSafe ->
                    Timber.d(
                        "Sensor rotationEventListener changed with event x %s y %s z %s",
                        eventSafe.values[0],
                        eventSafe.values[1],
                        eventSafe.values[2]
                    )
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

        binding.openglBlendOpenglView.setOnTouchListener { _, event ->
            if (event.pointerCount > 1) {
                scaleGestureDetector?.onTouchEvent(event)
            } else {
                if (event.action == MotionEvent.ACTION_MOVE) {
                    var deltaX = event.x - previousX
                    var deltaY = event.y - previousY
                    if (event.y > binding.openglBlendOpenglView.height / 2) {
                        deltaX *= -1
                    }
                    if (event.x < binding.openglBlendOpenglView.width / 2) {
                        deltaY *= -1
                    }
                    val previousTouchX = renderer.getTouchRotationModel()?.angleX ?: 0f
                    val previousTouchY = renderer.getTouchRotationModel()?.angleY ?: 0f
                    renderer.setTouchRotationModel(
                        TouchRotationModel(
                            previousTouchX + deltaY * binding.openglBlendOpenglView.getWidthScaleFactor(),
                            previousTouchY + deltaX * binding.openglBlendOpenglView.getWidthScaleFactor()
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

    private fun updateView() {
        try {
            if (!viewDestroyed) {
                binding.openglBlendOpenglView.refresh()
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
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(
            rotationEventListener
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewDestroyed = true
    }

}