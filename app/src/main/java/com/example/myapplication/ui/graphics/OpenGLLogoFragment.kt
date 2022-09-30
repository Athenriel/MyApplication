package com.example.myapplication.ui.graphics

import android.os.Bundle
import android.view.View
import com.example.myapplication.databinding.FragmentOpenglLogoBinding
import com.example.myapplication.model.RotationModel
import com.example.myapplication.ui.BaseFragment
import com.example.myapplication.ui.renderer.OpenGLLogoRenderer
import timber.log.Timber
import java.util.*

/**
 * Created by Athenriel on 9/30/2022
 */
class OpenGLLogoFragment :
    BaseFragment<FragmentOpenglLogoBinding>(FragmentOpenglLogoBinding::inflate) {

    private val timer = Timer()
    private var paused = false
    private var viewDestroyed = false
    private var angleStep = 1f
    private lateinit var renderer: OpenGLLogoRenderer
    private val rotationModel = RotationModel(0f, x = true, y = false, z = false)

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