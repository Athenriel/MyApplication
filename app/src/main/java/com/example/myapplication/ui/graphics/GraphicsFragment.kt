package com.example.myapplication.ui.graphics

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentGraphicsBinding
import com.example.myapplication.model.GraphicExampleModel
import com.example.myapplication.ui.BaseFragment
import com.example.myapplication.ui.graphics.adapter.GraphicExamplesAdapter

/**
 * Created by Athenriel on 8/16/2022
 */
class GraphicsFragment : BaseFragment<FragmentGraphicsBinding>(FragmentGraphicsBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val graphicExampleTitleList = listOf(
            getString(R.string.polygons_title),                     //0
            getString(R.string.graphs_title),                       //1
            getString(R.string.three_d_cube_title),                 //2
            getString(R.string.dance_title),                        //3
            getString(R.string.opengl_triangle_title),              //4
            getString(R.string.opengl_ellipse_title),               //5
            getString(R.string.opengl_pyramid_title),               //6
            getString(R.string.opengl_cube_title),                  //7
            getString(R.string.opengl_pentagon_prism_title),        //8
            getString(R.string.opengl_letter_a_title),              //9
            getString(R.string.opengl_letter_s_title),              //10
            getString(R.string.opengl_letter_v_title),              //11
            getString(R.string.opengl_sphere_title),                //12
            getString(R.string.opengl_half_cone_title),             //13
            getString(R.string.opengl_imperial_title),              //14
            getString(R.string.opengl_logo_title),                  //15
            getString(R.string.opengl_logo_diffuse_light_title),    //16
            getString(R.string.opengl_pyramid_with_texture_title)   //17
        )

        val graphicExampleModelList = mutableListOf<GraphicExampleModel>()

        for (title in graphicExampleTitleList.withIndex()) {
            graphicExampleModelList.add(GraphicExampleModel(title.index, title.value))
        }

        val graphicExamplesAdapter = GraphicExamplesAdapter(
            graphicExampleModelList,
            object : GraphicExamplesAdapter.GraphicExampleListener {
                override fun onGraphicExampleClick(id: Int) {
                    navigateToGraphicExample(id)
                }
            }
        )

        binding.graphicsExamplesRecyclerView.adapter = graphicExamplesAdapter
    }

    private fun navigateToGraphicExample(id: Int) {
        val directions = when (id) {
            0 -> {
                GraphicsFragmentDirections.actionGraphicsFragmentToPolygonsFragment()
            }
            1 -> {
                GraphicsFragmentDirections.actionGraphicsFragmentToGraphsFragment()
            }
            2 -> {
                GraphicsFragmentDirections.actionGraphicsFragmentToThreeDCubeFragment()
            }
            3 -> {
                GraphicsFragmentDirections.actionGraphicsFragmentToDanceFragment()
            }
            4 -> {
                GraphicsFragmentDirections.actionGraphicsFragmentToOpenGLTriangleFragment()
            }
            5 -> {
                GraphicsFragmentDirections.actionGraphicsFragmentToOpenGLEllipseFragment()
            }
            6 -> {
                GraphicsFragmentDirections.actionGraphicsFragmentToOpenGLPyramidFragment()
            }
            7 -> {
                GraphicsFragmentDirections.actionGraphicsFragmentToOpenGLCubeFragment()
            }
            8 -> {
                GraphicsFragmentDirections.actionGraphicsFragmentToOpenGLPentagonPrismFragment()
            }
            9 -> {
                GraphicsFragmentDirections.actionGraphicsFragmentToOpenGLLetterAFragment()
            }
            10 -> {
                GraphicsFragmentDirections.actionGraphicsFragmentToOpenGLLetterSFragment()
            }
            11 -> {
                GraphicsFragmentDirections.actionGraphicsFragmentToOpenGLLetterVFragment()
            }
            12 -> {
                GraphicsFragmentDirections.actionGraphicsFragmentToOpenGLSphereFragment()
            }
            13 -> {
                GraphicsFragmentDirections.actionGraphicsFragmentToOpenGLHalfConeFragment()
            }
            14 -> {
                GraphicsFragmentDirections.actionGraphicsFragmentToOpenGLImperialFragment()
            }
            15 -> {
                GraphicsFragmentDirections.actionGraphicsFragmentToOpenGLLogoFragment()
            }
            16 -> {
                GraphicsFragmentDirections.actionGraphicsFragmentToOpenGLLogoDiffuseLightFragment()
            }
            17 -> {
                GraphicsFragmentDirections.actionGraphicsFragmentToOpenGLPyramidWithTextureFragment()
            }
            else -> {
                GraphicsFragmentDirections.actionGraphicsFragmentToPolygonsFragment()
            }
        }
        findNavController().navigate(directions)
    }

}