package com.example.myapplication.ui.graphics

import android.os.Bundle
import android.view.View
import com.example.myapplication.databinding.FragmentOpenglPentagonPrismBinding
import com.example.myapplication.ui.BaseFragment
import com.example.myapplication.ui.renderer.OpenGLPentagonPrismRenderer

/**
 * Created by Athenriel on 9/13/2022
 */
class OpenGLPentagonPrismFragment :
    BaseFragment<FragmentOpenglPentagonPrismBinding>(FragmentOpenglPentagonPrismBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.openglPentagonPrismOpenglView.setRenderer(OpenGLPentagonPrismRenderer())
    }

}