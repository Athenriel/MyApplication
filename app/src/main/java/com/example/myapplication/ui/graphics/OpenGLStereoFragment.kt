package com.example.myapplication.ui.graphics

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import com.example.myapplication.databinding.FragmentOpenglStereoBinding
import com.example.myapplication.model.TouchRotationModel
import com.example.myapplication.ui.BaseFragment
import com.example.myapplication.ui.renderer.LetterSAndLetterAStereoRenderer
import timber.log.Timber
import java.util.*

/**
 * Created by Athenriel on 10/21/2022
 */
class OpenGLStereoFragment :
    BaseFragment<FragmentOpenglStereoBinding>(FragmentOpenglStereoBinding::inflate) {

    private var previousX = 0f
    private var previousY = 0f
    private var viewDestroyed = false
    private var scaleFactor = 1f
    private var scaleGestureDetector: ScaleGestureDetector? = null
    private lateinit var renderer: LetterSAndLetterAStereoRenderer

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDestroyed = false
        renderer = LetterSAndLetterAStereoRenderer()
        binding.openglStereoOpenglView.setRenderer(renderer)

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

        binding.openglStereoOpenglView.setOnTouchListener { _, event ->
            if (event.pointerCount > 1) {
                scaleGestureDetector?.onTouchEvent(event)
            } else {
                if (event.action == MotionEvent.ACTION_MOVE) {
                    var deltaX = event.x - previousX
                    var deltaY = event.y - previousY
                    if (event.y > binding.openglStereoOpenglView.height / 2) {
                        deltaX *= -1
                    }
                    if (event.x < binding.openglStereoOpenglView.width / 2) {
                        deltaY *= -1
                    }
                    val previousTouchX = renderer.getTouchRotationModel()?.angleX ?: 0f
                    val previousTouchY = renderer.getTouchRotationModel()?.angleY ?: 0f
                    renderer.setTouchRotationModel(
                        TouchRotationModel(
                            previousTouchX + deltaY * binding.openglStereoOpenglView.getWidthScaleFactor(),
                            previousTouchY + deltaX * binding.openglStereoOpenglView.getWidthScaleFactor()
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
                binding.openglStereoOpenglView.refresh()
            }
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewDestroyed = true
    }

}