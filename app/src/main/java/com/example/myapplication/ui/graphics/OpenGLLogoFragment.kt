package com.example.myapplication.ui.graphics

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import com.example.myapplication.databinding.FragmentOpenglLogoBinding
import com.example.myapplication.model.RotationModel
import com.example.myapplication.model.TouchRotationModel
import com.example.myapplication.ui.BaseFragment
import com.example.myapplication.ui.renderer.OpenGLLogoRenderer
import timber.log.Timber
import java.util.*

/**
 * Created by Athenriel on 9/30/2022
 */
class OpenGLLogoFragment :
    BaseFragment<FragmentOpenglLogoBinding>(FragmentOpenglLogoBinding::inflate) {

    private var previousX = 0f
    private var previousY = 0f
    private val timer = Timer()
    private var paused = false
    private var viewDestroyed = false
    private var angleStep = 1f
    private lateinit var renderer: OpenGLLogoRenderer
    private val rotationModel = RotationModel(0f, x = true, y = false, z = false)

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDestroyed = false
        renderer = OpenGLLogoRenderer()
        binding.openglLogoOpenglView.setRenderer(renderer)
        val timerTask = object : TimerTask() {
            override fun run() {
                if (!paused) {
                    if (rotationModel.checkNextRotation()) {
                        rotationModel.prepareNextRotation()
                    }
                    rotationModel.incrementAngle(angleStep)
                    renderer.setRotationModel(rotationModel)
                    try {
                        if (!viewDestroyed) {
                            binding.openglLogoOpenglView.refresh()
                        }
                    } catch (e: Exception) {
                        Timber.e(e)
                    }
                }
            }
        }
        timer.scheduleAtFixedRate(timerTask, 100, 30)

        binding.openglLogoOpenglView.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_MOVE) {
                var deltaX = event.x - previousX
                var deltaY = event.y - previousY
                if (event.y > binding.openglLogoOpenglView.height / 2) {
                    deltaX *= -1
                }
                if (event.x < binding.openglLogoOpenglView.width / 2) {
                    deltaY *= -1
                }
                val previousTouchX = renderer.getTouchRotationModel()?.angleX ?: 0f
                val previousTouchY = renderer.getTouchRotationModel()?.angleY ?: 0f
                renderer.setTouchRotationModel(
                    TouchRotationModel(
                        previousTouchX + deltaY * binding.openglLogoOpenglView.getWidthScaleFactor(),
                        previousTouchY + deltaX * binding.openglLogoOpenglView.getWidthScaleFactor()
                    )
                )
                binding.openglLogoOpenglView.refresh()
            } else if (event.action == MotionEvent.ACTION_UP) {
                renderer.setTouchRotationModel(null)
            }
            previousX = event.x
            previousY = event.y
            true
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