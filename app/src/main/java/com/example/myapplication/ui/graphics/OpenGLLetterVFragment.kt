package com.example.myapplication.ui.graphics

import android.os.Bundle
import android.view.View
import com.example.myapplication.databinding.FragmentOpenglLetterVBinding
import com.example.myapplication.ui.BaseFragment
import com.example.myapplication.ui.renderer.OpenGLLetterVRenderer

/**
 * Created by Athenriel on 9/23/2022
 */
class OpenGLLetterVFragment :
    BaseFragment<FragmentOpenglLetterVBinding>(FragmentOpenglLetterVBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.openglLetterVOpenglView.setRenderer(OpenGLLetterVRenderer())
    }

}