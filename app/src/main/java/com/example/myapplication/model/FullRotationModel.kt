package com.example.myapplication.model

/**
 * Created by Athenriel on 10/19/2022
 */
data class FullRotationModel(
    val rotationX: RotationModel = RotationModel(0f, x = true, y = false, z = false),
    val rotationY: RotationModel = RotationModel(0f, x = false, y = true, z = false),
    val rotationZ: RotationModel = RotationModel(0f, x = false, y = false, z = true)
) {
    companion object {
        fun getFromRotationModel(rotationModel: RotationModel): FullRotationModel {
            val fullRotationModel = FullRotationModel()

            if (rotationModel.isValid()) {
                when {
                    rotationModel.getX() && !rotationModel.getY() && !rotationModel.getZ() -> {
                        fullRotationModel.rotationX.setAngle(rotationModel.getAngle())
                    }
                    !rotationModel.getX() && rotationModel.getY() && !rotationModel.getZ() -> {
                        fullRotationModel.rotationY.setAngle(rotationModel.getAngle())
                    }
                    !rotationModel.getX() && !rotationModel.getY() && rotationModel.getZ() -> {
                        fullRotationModel.rotationZ.setAngle(rotationModel.getAngle())
                    }
                }
            }

            return fullRotationModel
        }
    }
}