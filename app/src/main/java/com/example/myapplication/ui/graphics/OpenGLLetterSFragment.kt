package com.example.myapplication.ui.graphics

import android.os.Bundle
import android.view.View
import com.example.myapplication.databinding.FragmentOpenglLetterSBinding
import com.example.myapplication.ui.BaseFragment
import com.example.myapplication.ui.renderer.OpenGLLetterSRenderer

/**
 * Created by Athenriel on 9/23/2022
 */
class OpenGLLetterSFragment :
    BaseFragment<FragmentOpenglLetterSBinding>(FragmentOpenglLetterSBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.openglLetterSOpenglView.setRenderer(OpenGLLetterSRenderer())
    }

}