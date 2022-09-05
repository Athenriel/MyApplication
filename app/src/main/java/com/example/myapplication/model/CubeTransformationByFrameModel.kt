package com.example.myapplication.model

import com.example.myapplication.utils.GraphicUtils

/**
 * Created by Athenriel on 9/1/2022
 */
class CubeTransformationByFrameModel(
    private val angle: Float,
    private val xAxis: Double,
    private val yAxis: Double,
    private val zAxis: Double,
    private val rotationType: GraphicUtils.RotationType = GraphicUtils.RotationType.CENTER,
    private val translation: Boolean = false
) {

    fun getCubeTransformation(
        threeDCubeModel: ThreeDCubeModel,
        reference: ThreeDCoordinatesModel? = null
    ) {
        if (angle != 0f) {
            threeDCubeModel.quaternionRotate(
                angle,
                xAxis,
                yAxis,
                zAxis,
                rotationType,
                reference
            )
        } else if (translation) {
            threeDCubeModel.translate(
                xAxis,
                yAxis,
                zAxis
            )
        }
    }

}
