package com.example.myapplication.ui.graphics

import android.os.Bundle
import android.view.View
import com.example.myapplication.databinding.FragmentOpenglTriangleBinding
import com.example.myapplication.ui.BaseFragment
import com.example.myapplication.ui.renderer.OpenGLTriangleRenderer

/**
 * Created by Athenriel on 9/5/2022
 */
class OpenGLTriangleFragment :
    BaseFragment<FragmentOpenglTriangleBinding>(FragmentOpenglTriangleBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.openglTriangleOpenglView.setRenderer(OpenGLTriangleRenderer())
    }

}