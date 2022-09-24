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

        binding.graphicsButtonsScrollView.apply {
            isVerticalScrollBarEnabled = true
            isScrollbarFadingEnabled = false
        }

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
        binding.graphicsFourthBtn.setOnClickListener {
            val directions = GraphicsFragmentDirections.actionGraphicsFragmentToDanceFragment()
            findNavController().navigate(directions)
        }
        binding.graphicsFifthBtn.setOnClickListener {
            val directions =
                GraphicsFragmentDirections.actionGraphicsFragmentToOpenGLTriangleFragment()
            findNavController().navigate(directions)
        }
        binding.graphicsSixthBtn.setOnClickListener {
            val directions =
                GraphicsFragmentDirections.actionGraphicsFragmentToOpenGLEllipseFragment()
            findNavController().navigate(directions)
        }
        binding.graphicsSeventhBtn.setOnClickListener {
            val directions =
                GraphicsFragmentDirections.actionGraphicsFragmentToOpenGLPyramidFragment()
            findNavController().navigate(directions)
        }
        binding.graphicsEightBtn.setOnClickListener {
            val directions =
                GraphicsFragmentDirections.actionGraphicsFragmentToOpenGLCubeFragment()
            findNavController().navigate(directions)
        }
        binding.graphicsNinthBtn.setOnClickListener {
            val directions =
                GraphicsFragmentDirections.actionGraphicsFragmentToOpenGLPentagonPrismFragment()
            findNavController().navigate(directions)
        }
        binding.graphicsTenthBtn.setOnClickListener {
            val directions =
                GraphicsFragmentDirections.actionGraphicsFragmentToOpenGLLetterAFragment()
            findNavController().navigate(directions)
        }
        binding.graphicsEleventhBtn.setOnClickListener {
            val directions =
                GraphicsFragmentDirections.actionGraphicsFragmentToOpenGLLetterSFragment()
            findNavController().navigate(directions)
        }
        binding.graphicsTwelfthBtn.setOnClickListener {
            val directions =
                GraphicsFragmentDirections.actionGraphicsFragmentToOpenGLLetterVFragment()
            findNavController().navigate(directions)
        }
        binding.graphicsThirteenthBtn.setOnClickListener {
            val directions =
                GraphicsFragmentDirections.actionGraphicsFragmentToOpenGLSphereFragment()
            findNavController().navigate(directions)
        }
        binding.graphicsFourteenthBtn.setOnClickListener {
            val directions =
                GraphicsFragmentDirections.actionGraphicsFragmentToOpenGLHalfConeFragment()
            findNavController().navigate(directions)
        }
    }

}