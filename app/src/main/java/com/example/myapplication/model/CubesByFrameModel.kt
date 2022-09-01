package com.example.myapplication.model

/**
 * Created by Athenriel on 9/1/2022
 */
data class CubesByFrameModel(
    val frame: Int,
    val transformationList: List<CubeTransformationListByFrameModel>
) {

    fun getCubeListForFrame(
        cubeList: List<ThreeDCubeModel>,
        reference: ThreeDCoordinatesModel? = null
    ) {
        if (transformationList.size == cubeList.size) {
            for (index in transformationList.indices) {
                transformationList[index].getCubeTransformation(cubeList[index], reference)
            }
        }
    }

}