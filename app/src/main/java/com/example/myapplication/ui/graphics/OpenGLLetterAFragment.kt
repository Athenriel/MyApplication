package com.example.myapplication.ui.graphics

import android.os.Bundle
import android.view.View
import com.example.myapplication.databinding.FragmentOpenglLetterABinding
import com.example.myapplication.ui.BaseFragment
import com.example.myapplication.ui.renderer.OpenGLLetterARenderer

/**
 * Created by Athenriel on 9/13/2022
 */
class OpenGLLetterAFragment :
    BaseFragment<FragmentOpenglLetterABinding>(FragmentOpenglLetterABinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.openglLetterAOpenglView.setRenderer(OpenGLLetterARenderer())
    }

}