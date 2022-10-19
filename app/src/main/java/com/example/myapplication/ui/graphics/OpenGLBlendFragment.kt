package com.example.myapplication.ui.graphics

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import com.example.myapplication.databinding.FragmentOpenglBlendBinding
import com.example.myapplication.model.RotationModel
import com.example.myapplication.model.TouchRotationModel
import com.example.myapplication.ui.BaseFragment
import com.example.myapplication.ui.renderer.PyramidAndCubeBlendRenderer
import timber.log.Timber
import java.util.*

/**
 * Created by Athenriel on 10/14/2022
 */
class OpenGLBlendFragment :
    BaseFragment<FragmentOpenglBlendBinding>(FragmentOpenglBlendBinding::inflate) {

    private var previousX = 0f
    private var previousY = 0f
    private val timer = Timer()
    private var paused = false
    private var viewDestroyed = false
    private var angleStep = 1f
    private var scaleFactor = 1f
    private var scaleGestureDetector: ScaleGestureDetector? = null
    private lateinit var renderer: PyramidAndCubeBlendRenderer
    private val rotationModel = RotationModel(0f, x = true, y = false, z = false)

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDestroyed = false
        renderer = PyramidAndCubeBlendRenderer(resources)
        binding.openglBlendOpenglView.setRenderer(renderer)
        val timerTask = object : TimerTask() {
            override fun run() {
                if (!paused) {
                    if (rotationModel.checkNextRotation()) {
                        rotationModel.prepareNextRotation()
                    }
                    rotationModel.incrementAngle(angleStep)
                    renderer.setRotationModel(rotationModel)
                    updateView()
                }
            }
        }
        timer.scheduleAtFixedRate(timerTask, 100, 30)

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
        }

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
        paused = false
    }

    override fun onPause() {
        super.onPause()
        paused = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        timer.cancel()
        viewDestroyed = true
    }

}