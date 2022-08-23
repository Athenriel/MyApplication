package com.example.myapplication.model

/**
 * Created by Athenriel on 8/23/2022
 * Homogeneous coordinates in 3D space
 */
data class ThreeDCoordinatesModel(
    var x: Double = 0.0,
    var y: Double = 0.0,
    var z: Double = 0.0,
    var w: Double = 1.0
) {
    /**
     * To keep homogeneous coordinates, divide by w and set w = 1
     */
    fun normalize() {
        if (w != 0.0) {
            x /= w
            y /= w
            z /= w
            w = 1.0
        }
    }
}
