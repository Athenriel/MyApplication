package com.example.myapplication.ui.graphics

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.myapplication.databinding.FragmentGraphicsBinding
import com.example.myapplication.ui.BaseFragment

/**
 * Created by Athenriel on 8/16/2022
 */
class GraphicsFragment : BaseFragment<FragmentGraphicsBinding>(FragmentGraphicsBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.graphicsFirstBtn.setOnClickListener {
            val directions = GraphicsFragmentDirections.actionGraphicsFragmentToPolygonsFragment()
            findNavController().navigate(directions)
        }
        binding.graphicsSecondBtn.setOnClickListener {
            val directions = GraphicsFragmentDirections.actionGraphicsFragmentToGraphsFragment()
            findNavController().navigate(directions)
        }
        binding.graphicsThirdBtn.setOnClickListener {
            val directions = GraphicsFragmentDirections.actionGraphicsFragmentToThreeDCubeFragment()
            findNavController().navigate(directions)
        }
    }

}