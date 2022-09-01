package com.example.myapplication.model

/**
 * Created by Athenriel on 9/2/2022
 */
class CubeTransformationListByFrameModel {

    private var dataList: MutableList<CubeTransformationByFrameModel> = mutableListOf()

    fun setDataList(data: List<CubeTransformationByFrameModel>) {
        dataList.clear()
        dataList.addAll(data)
    }

    fun getData(): List<CubeTransformationByFrameModel> {
        return dataList
    }

    fun getCubeTransformation(
        threeDCubeModel: ThreeDCubeModel,
        reference: ThreeDCoordinatesModel? = null
    ) {
        for (transformation in dataList) {
            transformation.getCubeTransformation(threeDCubeModel, reference)
        }
    }

}