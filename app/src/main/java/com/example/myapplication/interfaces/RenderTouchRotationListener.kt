package com.example.myapplication.interfaces

import com.example.myapplication.model.TouchRotationModel

/**
 * Created by Athenriel on 10/7/2022
 */
interface RenderTouchRotationListener {
    fun setTouchRotationModel(newTouchRotationModel: TouchRotationModel?)
    fun getTouchRotationModel(): TouchRotationModel?
}