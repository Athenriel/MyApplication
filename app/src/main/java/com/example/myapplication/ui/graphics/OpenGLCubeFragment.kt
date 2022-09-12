package com.example.myapplication.ui.graphics

import android.os.Bundle
import android.view.View
import com.example.myapplication.databinding.FragmentOpenglCubeBinding
import com.example.myapplication.ui.BaseFragment
import com.example.myapplication.ui.renderer.OpenGLCubeRenderer

/**
 * Created by Athenriel on 9/13/2022
 */
class OpenGLCubeFragment :
    BaseFragment<FragmentOpenglCubeBinding>(FragmentOpenglCubeBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.openglCubeOpenglView.setRenderer(OpenGLCubeRenderer())
    }

}