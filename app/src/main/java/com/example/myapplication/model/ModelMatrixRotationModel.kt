package com.example.myapplication.model

import android.opengl.Matrix
import kotlin.random.Random

/**
 * Created by Athenriel on 9/29/2022
 */
data class ModelMatrixRotationModel(
    val matrixOffset: Int,
    val rotationModel: RotationModel,
    val resultOffset: Int,
    val leftOffset: Int,
    val rightOffset: Int
) {

    constructor(rotationModel: RotationModel) : this(
        NO_OFFSET,
        rotationModel,
        NO_OFFSET,
        NO_OFFSET,
        NO_OFFSET
    )

    companion object {
        private const val NO_OFFSET = 0
        private const val MATRIX_SIZE = 16
        private const val ANGLE_MIN = 0
        private const val ANGLE_MAX = 360

        fun randomRotateMatrix(modelMatrix: FloatArray) {
            val angle = Random.nextInt(ANGLE_MIN, ANGLE_MAX).toFloat()
            val x = Random.nextBoolean()
            val y = Random.nextBoolean()
            val z = Random.nextBoolean()
            val rotationModel = RotationModel(angle, x, y, z)
            if (rotationModel.isValid()) {
                rotate(
                    modelMatrix,
                    NO_OFFSET,
                    rotationModel,
                    NO_OFFSET,
                    NO_OFFSET,
                    NO_OFFSET
                )
            }
        }

        private fun rotate(
            modelMatrix: FloatArray,
            matrixOffset: Int,
            rotationModel: RotationModel,
            resultOffset: Int,
            leftOffset: Int,
            rightOffset: Int
        ) {
            val rotationMatrix = FloatArray(MATRIX_SIZE)
            Matrix.setRotateM(
                rotationMatrix,
                matrixOffset,
                rotationModel.getAngle(),
                rotationModel.getXValue(),
                rotationModel.getYValue(),
                rotationModel.getZValue()
            )
            Matrix.multiplyMM(
                modelMatrix,
                resultOffset,
                modelMatrix,
                leftOffset,
                rotationMatrix,
                rightOffset
            )
        }
    }

    fun rotateMatrix(modelMatrix: FloatArray) {
        if (rotationModel.isValid()) {
            rotate(
                modelMatrix,
                matrixOffset,
                rotationModel,
                resultOffset,
                leftOffset,
                rightOffset
            )
        }
    }

}