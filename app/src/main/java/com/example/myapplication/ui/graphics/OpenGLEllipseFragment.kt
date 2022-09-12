package com.example.myapplication.ui.graphics

import android.os.Bundle
import android.view.View
import com.example.myapplication.databinding.FragmentOpenglEllipseBinding
import com.example.myapplication.ui.BaseFragment
import com.example.myapplication.ui.renderer.OpenGLEllipseRenderer

/**
 * Created by Athenriel on 9/12/2022
 */
class OpenGLEllipseFragment :
    BaseFragment<FragmentOpenglEllipseBinding>(FragmentOpenglEllipseBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.openglEllipseOpenglView.setRenderer(OpenGLEllipseRenderer())
    }

}