package com.example.myapplication.ui.graphics

import android.os.Bundle
import android.view.View
import com.example.myapplication.databinding.FragmentOpenglPyramidBinding
import com.example.myapplication.ui.BaseFragment
import com.example.myapplication.ui.renderer.OpenGLPyramidRenderer

/**
 * Created by Athenriel on 9/12/2022
 */
class OpenGLPyramidFragment :
    BaseFragment<FragmentOpenglPyramidBinding>(FragmentOpenglPyramidBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.openglPyramidOpenglView.setRenderer(OpenGLPyramidRenderer())
    }

}