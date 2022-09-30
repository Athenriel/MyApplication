package com.example.myapplication.model

import kotlin.math.abs

/**
 * Created by Athenriel on 9/29/2022
 */
data class RotationModel(
    private var angle: Float,
    private var x: Boolean,
    private var y: Boolean,
    private var z: Boolean
) {

    companion object {
        private const val VALUE_TRUE = 1f
        private const val VALUE_FALSE = 0f
        private const val ANGLE_MIN = 0f
        private const val ANGLE_MAX = 360f
    }

    fun getAngle(): Float {
        return angle
    }

    fun getX(): Boolean {
        return x
    }

    fun getY(): Boolean {
        return y
    }

    fun getZ(): Boolean {
        return z
    }

    fun getXValue(): Float {
        return if (x) {
            VALUE_TRUE
        } else {
            VALUE_FALSE
        }
    }

    fun getYValue(): Float {
        return if (y) {
            VALUE_TRUE
        } else {
            VALUE_FALSE
        }
    }

    fun getZValue(): Float {
        return if (z) {
            VALUE_TRUE
        } else {
            VALUE_FALSE
        }
    }

    fun setAngle(newAngle: Float) {
        angle = newAngle
    }

    fun setX(newX: Boolean) {
        x = newX
    }

    fun setY(newY: Boolean) {
        y = newY
    }

    fun setZ(newZ: Boolean) {
        z = newZ
    }

    fun incrementAngle(step: Float) {
        angle += step
    }

    fun checkNextRotation(): Boolean {
        return abs(angle) > ANGLE_MAX
    }

    fun prepareNextRotation() {
        angle = ANGLE_MIN
        when {
            !x && !y && !z -> {
                x = true
            }
            x && !y && !z -> {
                x = false
                y = true
            }
            !x && y && !z -> {
                y = false
                z = true
            }
            !x && !y && z -> {
                z = false
                x = true
                y = true
            }
            x && y && !z -> {
                y = false
                z = true
            }
            x && !y && z -> {
                x = false
                y = true
            }
            !x && y && z -> {
                x = true
            }
            x && y && z -> {
                x = true
                y = false
                z = false
            }
            else -> {
                x = true
                y = false
                z = false
            }
        }
    }

    fun isValid(): Boolean {
        return x || y || z
    }

}
