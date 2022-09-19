package com.example.myapplication.ui.graphics

import android.os.Bundle
import android.view.View
import com.example.myapplication.databinding.FragmentOpenglSphereBinding
import com.example.myapplication.ui.BaseFragment
import com.example.myapplication.ui.renderer.OpenGLSphereRenderer

/**
 * Created by Athenriel on 9/23/2022
 */
class OpenGLSphereFragment :
    BaseFragment<FragmentOpenglSphereBinding>(FragmentOpenglSphereBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.openglSphereOpenglView.setRenderer(OpenGLSphereRenderer())
    }

}