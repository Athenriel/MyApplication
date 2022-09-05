package com.example.myapplication.model

/**
 * Created by Athenriel on 9/1/2022
 */
class ThreeDCubeListModel {

    private var dataList: MutableList<ThreeDCubeModel> = mutableListOf()

    fun setDataList(data: List<ThreeDCubeModel>) {
        dataList.clear()
        dataList.addAll(data)
    }

    fun getData(): List<ThreeDCubeModel> {
        return dataList
    }

}