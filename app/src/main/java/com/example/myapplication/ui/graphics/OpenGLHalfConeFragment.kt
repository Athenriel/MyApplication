package com.example.myapplication.ui.graphics

import android.os.Bundle
import android.view.View
import com.example.myapplication.databinding.FragmentOpenglHalfConeBinding
import com.example.myapplication.ui.BaseFragment
import com.example.myapplication.ui.renderer.OpenGLHalfConeRenderer

/**
 * Created by Athenriel on 9/23/2022
 */
class OpenGLHalfConeFragment :
    BaseFragment<FragmentOpenglHalfConeBinding>(FragmentOpenglHalfConeBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.openglHalfConeOpenglView.setRenderer(OpenGLHalfConeRenderer())
    }

}