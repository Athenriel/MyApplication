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
        val scaledFirstThreeDCoordinatesList =
            GraphicUtils.scaleThreeDCoordinates(
                translatedThreeDCoordinatesList,
                40.0,
                40.0,
                40.0,
                false
            )
        val projectedThreeDCoordinatesList = GraphicUtils.perspectiveProjectThreeDCoordinates(translatedThreeDCoordinatesList, 1.0, -1.0, -1.0, 1.0, 1.0, 1.1)
        var scaledSecondThreeDCoordinatesList =
            GraphicUtils.scaleThreeDCoordinates(
                projectedThreeDCoordinatesList,
                40.0,
                40.0,
                40.0,
                false
            )
        scaledSecondThreeDCoordinatesList = GraphicUtils.translateThreeDCoordinates(scaledSecondThreeDCoordinatesList, 200.0, 200.0, 0.0)
        val firstTimerTask = object : TimerTask() {
            var angle = 0f
            override fun run() {
                var rotatedThreeDCoordinatesList = GraphicUtils.quaternionRotateThreeDCoordinates(
                    scaledFirstThreeDCoordinatesList,
                    angle,
                    0.0,
                    1.0,
                    1.0,
                    GraphicUtils.RotationType.NONE
                )
                rotatedThreeDCoordinatesList = GraphicUtils.translateThreeDCoordinates(
                    rotatedThreeDCoordinatesList,
                    200.0,
                    200.0,
                    0.0
                )
                if (!viewDestroyed) {
                    binding.threeDFirstThreeDCubeView.setThreeDCoordinates(rotatedThreeDCoordinatesList)
                }
                angle += 10
                if (angle >= 360) {
                    angle = 0f
                }
            }
        }
        val secondTimerTask = object : TimerTask() {
            var positionX = 0f
            var dir = true
            var movedThreeDCoordinatesList = scaledSecondThreeDCoordinatesList
            override fun run() {
                if (!viewDestroyed) {
                    if (positionX + 80 >= binding.threeDSecondThreeDCubeView.width && dir) {
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
                    binding.threeDSecondThreeDCubeView.setThreeDCoordinates(movedThreeDCoordinatesList)
                }
            }
        }
        timer.scheduleAtFixedRate(firstTimerTask, 100, 100)
        timer.scheduleAtFixedRate(secondTimerTask, 100, 2)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        timer.cancel()
        viewDestroyed = true
    }

}