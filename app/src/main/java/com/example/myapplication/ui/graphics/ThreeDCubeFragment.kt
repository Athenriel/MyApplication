package com.example.myapplication.ui.graphics

import android.os.Bundle
import android.view.View
import com.example.myapplication.databinding.FragmentThreeDBinding
import com.example.myapplication.model.ThreeDCoordinatesModel
import com.example.myapplication.ui.BaseFragment
import com.example.myapplication.utils.GraphicUtils
import java.util.*

/**
 * Created by Athenriel on 8/23/2022
 */
class ThreeDCubeFragment : BaseFragment<FragmentThreeDBinding>(FragmentThreeDBinding::inflate) {

    private val timer = Timer()
    private var viewDestroyed = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDestroyed = false
        val coordinateList = listOf(
            ThreeDCoordinatesModel(-1.0, -1.0, -1.0),
            ThreeDCoordinatesModel(-1.0, -1.0, 1.0),
            ThreeDCoordinatesModel(-1.0, 1.0, -1.0),
            ThreeDCoordinatesModel(-1.0, 1.0, 1.0),
            ThreeDCoordinatesModel(1.0, -1.0, -1.0),
            ThreeDCoordinatesModel(1.0, -1.0, 1.0),
            ThreeDCoordinatesModel(1.0, 1.0, -1.0),
            ThreeDCoordinatesModel(1.0, 1.0, 1.0)
        )
        val translatedThreeDCoordinatesList =
            GraphicUtils.translateThreeDCoordinates(coordinateList, 2.0, 2.0, 2.0)
        val scaledThreeDCoordinatesList =
            GraphicUtils.scaleThreeDCoordinates(
                translatedThreeDCoordinatesList,
                40.0,
                40.0,
                40.0,
                false
            )
        //var rotatedThreeDCoordinatesList = GraphicUtils.rotateInYThreeDCoordinates(scaledThreeDCoordinatesList, 45f, false)
        //rotatedThreeDCoordinatesList = GraphicUtils.rotateInXThreeDCoordinates(rotatedThreeDCoordinatesList, 45f, false)
        //rotatedThreeDCoordinatesList = GraphicUtils.rotateInZThreeDCoordinates(rotatedThreeDCoordinatesList, 80f, false)
        //rotatedThreeDCoordinatesList = GraphicUtils.rotateInYThreeDCoordinates(rotatedThreeDCoordinatesList, 30f, false)
        //val shearedThreeDCoordinatesList = GraphicUtils.shearThreeDCoordinates(scaledThreeDCoordinatesList, 2.0, 1.0, false)
        binding.threeDThreeDCubeView.setThreeDCoordinates(scaledThreeDCoordinatesList)
        val timerTask = object : TimerTask() {
            var angle = 0f
            override fun run() {
                /*
                var rotatedThreeDCoordinatesList = GraphicUtils.rotateInZThreeDCoordinates(
                    scaledThreeDCoordinatesList,
                    60f,
                    false
                )
                rotatedThreeDCoordinatesList = GraphicUtils.rotateInYThreeDCoordinates(
                    rotatedThreeDCoordinatesList,
                    90f,
                    false
                )
                rotatedThreeDCoordinatesList = GraphicUtils.rotateInXThreeDCoordinates(
                    rotatedThreeDCoordinatesList,
                    angle,
                    false
                )
                */
                var rotatedThreeDCoordinatesList = GraphicUtils.quaternionRotateThreeDCoordinates(
                    scaledThreeDCoordinatesList,
                    angle,
                    0.0,
                    1.0,
                    1.0
                )
                rotatedThreeDCoordinatesList = GraphicUtils.translateThreeDCoordinates(
                    rotatedThreeDCoordinatesList,
                    200.0,
                    200.0,
                    0.0
                )
                if (!viewDestroyed) {
                    binding.threeDThreeDCubeView.setThreeDCoordinates(rotatedThreeDCoordinatesList)
                }
                angle += 10
                if (angle >= 360) {
                    angle = 0f
                }
            }
            /*
            var positionX = 0f
            var dir = true
            var movedThreeDCoordinatesList = scaledThreeDCoordinatesList
            override fun run() {
                if (!viewDestroyed) {
                    if (positionX + 80 >= binding.threeDThreeDCubeView.width && dir) {
                        dir = false
                    } else if (!dir && positionX <= 0) {
                        dir = true
                    }
                }
                movedThreeDCoordinatesList = if (dir) {
                    positionX += 1
                    GraphicUtils.translateThreeDCoordinates(movedThreeDCoordinatesList, 1.0, 0.0, 0.0)
                } else {
                    positionX -= 1
                    GraphicUtils.translateThreeDCoordinates(movedThreeDCoordinatesList, -1.0, 0.0, 0.0)
                }
                if (!viewDestroyed) {
                    binding.threeDThreeDCubeView.setThreeDCoordinates(movedThreeDCoordinatesList)
                }
            }
            */
        }
        timer.scheduleAtFixedRate(timerTask, 100, 100)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        timer.cancel()
        viewDestroyed = true
    }

}