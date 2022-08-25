package com.example.myapplication.utils

import android.graphics.Path
import android.graphics.Point
import com.example.myapplication.model.ThreeDCoordinatesModel
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin

/**
 * Created by Athenriel on 8/18/2022
 */

fun Path.updatePath(pointList: List<Point>) {
    reset()
    if (pointList.isNotEmpty()) {
        moveTo(pointList[0].x.toFloat(), pointList[0].y.toFloat())
        if (pointList.size > 1) {
            for (i in 1 until pointList.size) {
                lineTo(pointList[i].x.toFloat(), pointList[i].y.toFloat())
            }
            close()
        }
    }
}

fun Path.createLineGraph(
    dataList: List<Int>,
    viewWidth: Int,
    viewHeight: Int,
    xBuffer: Int,
    yBuffer: Int
) {
    reset()
    if (dataList.size > 1) {
        var minValue = Int.MAX_VALUE
        var maxValue = Int.MIN_VALUE
        val pointList = mutableListOf<Point>()
        for (data in dataList.withIndex()) {
            pointList.add(Point(data.index, data.value))
            minValue = minOf(minValue, data.value)
            maxValue = maxOf(maxValue, data.value)
        }
        val translatedPoints = GraphicUtils.translatePoints(pointList, 0f, -minValue.toFloat())
        val valueDifference = maxValue - minValue
        val xScale: Float = (viewWidth.toFloat() - xBuffer) / (pointList.size - 1)
        val yScale: Float = (viewHeight.toFloat() - yBuffer) / valueDifference
        val scaledPoints =
            GraphicUtils.scalePoints(translatedPoints, xScale, yScale)
        val anchorPoint = GraphicUtils.findAnchorPoint(scaledPoints, xBuffer)
        val anchoredPoints = GraphicUtils.translatePoints(
            scaledPoints,
            -anchorPoint.x.toFloat(),
            -anchorPoint.y.toFloat()
        )
        moveTo(anchoredPoints[0].x.toFloat(), anchoredPoints[0].y.toFloat())
        for (i in 1 until anchoredPoints.size) {
            lineTo(anchoredPoints[i].x.toFloat(), anchoredPoints[i].y.toFloat())
        }
    }
}

object GraphicUtils {

    private fun affineTransformation(
        vertices: List<Point>,
        matrix: Array<FloatArray>
    ): List<Point> {
        val result = mutableListOf<Point>()
        if (matrix.size >= 2 && matrix[0].size == 3) {
            for (point in vertices) {
                val t = matrix[0][0] * point.x + matrix[0][1] * point.y + matrix[0][2]
                val u = matrix[1][0] * point.x + matrix[1][1] * point.y + matrix[1][2]
                result.add(Point(t.toInt(), u.toInt()))
            }
        }
        return result
    }

    private fun angleToRadians(angle: Float): Float {
        return (angle * Math.PI / 180).toFloat()
    }

    fun centerOfPoints(input: List<Point>): Point {
        var sumX = 0
        var sumY = 0
        for (point in input) {
            sumX += point.x
            sumY += point.y
        }
        return Point((sumX / input.size), sumY / input.size)
    }

    fun findAnchorPoint(input: List<Point>, xBuffer: Int): Point {
        var xMinValue = Int.MAX_VALUE
        var yMinValue = Int.MAX_VALUE
        for (point in input) {
            xMinValue = minOf(xMinValue, point.x)
            yMinValue = minOf(yMinValue, point.y)
        }
        val xAnchor = if (xMinValue < 0) {
            xMinValue - xBuffer
        } else {
            xMinValue + xBuffer
        }
        val yAnchor = yMinValue
        return Point(xAnchor, yAnchor)
    }

    fun translatePoints(input: List<Point>, newX: Float, newY: Float): List<Point> {
        val matrix: Array<FloatArray> =
            arrayOf(
                floatArrayOf(1f, 0f, newX),
                floatArrayOf(0f, 1f, newY),
                floatArrayOf(0f, 0f, 1f)
            )
        return affineTransformation(input, matrix)
    }

    fun rotatePoints(input: List<Point>, angle: Float, useCenter: Boolean = true): List<Point> {
        val radians = angleToRadians(angle)
        val matrix: Array<FloatArray> = arrayOf(
            floatArrayOf(cos(radians), -sin(radians), 0f), floatArrayOf(
                sin(radians), cos(radians), 0f
            ), floatArrayOf(0f, 0f, 1f)
        )
        return if (useCenter) {
            val center = centerOfPoints(input)
            val movedInput = translatePoints(input, -center.x.toFloat(), -center.y.toFloat())
            val tempPoints = affineTransformation(movedInput, matrix)
            translatePoints(tempPoints, center.x.toFloat(), center.y.toFloat())
        } else {
            affineTransformation(input, matrix)
        }
    }

    fun scalePoints(
        input: List<Point>,
        scaleX: Float,
        scaleY: Float,
        useCenter: Boolean = true
    ): List<Point> {
        val matrix: Array<FloatArray> =
            arrayOf(
                floatArrayOf(scaleX, 0f, 0f),
                floatArrayOf(0f, scaleY, 0f),
                floatArrayOf(0f, 0f, 1f)
            )
        return if (useCenter) {
            val center = centerOfPoints(input)
            val movedInput = translatePoints(input, -center.x.toFloat(), -center.y.toFloat())
            val tempPoints = affineTransformation(movedInput, matrix)
            translatePoints(tempPoints, center.x.toFloat(), center.y.toFloat())
        } else {
            affineTransformation(input, matrix)
        }
    }

    fun shearPoints(
        input: List<Point>,
        shearX: Float,
        shearY: Float,
        useCenter: Boolean = true
    ): List<Point> {
        val matrix: Array<FloatArray> =
            arrayOf(
                floatArrayOf(1f, shearX, 0f),
                floatArrayOf(shearY, 1f, 0f),
                floatArrayOf(0f, 0f, 1f)
            )
        return if (useCenter) {
            val center = centerOfPoints(input)
            val movedInput = translatePoints(input, -center.x.toFloat(), -center.y.toFloat())
            val tempPoints = affineTransformation(movedInput, matrix)
            translatePoints(tempPoints, center.x.toFloat(), center.y.toFloat())
        } else {
            affineTransformation(input, matrix)
        }
    }

    fun reflectPointsInX(input: List<Point>, useCenter: Boolean = true): List<Point> {
        val matrix: Array<FloatArray> =
            arrayOf(
                floatArrayOf(1f, 0f, 0f),
                floatArrayOf(0f, -1f, 0f),
                floatArrayOf(0f, 0f, 1f)
            )
        return if (useCenter) {
            val center = centerOfPoints(input)
            val movedInput = translatePoints(input, -center.x.toFloat(), -center.y.toFloat())
            val tempPoints = affineTransformation(movedInput, matrix)
            translatePoints(tempPoints, center.x.toFloat(), center.y.toFloat())
        } else {
            affineTransformation(input, matrix)
        }
    }

    fun reflectPointsInY(input: List<Point>, useCenter: Boolean = true): List<Point> {
        val matrix: Array<FloatArray> =
            arrayOf(
                floatArrayOf(-1f, 0f, 0f),
                floatArrayOf(0f, 1f, 0f),
                floatArrayOf(0f, 0f, 1f)
            )
        return if (useCenter) {
            val center = centerOfPoints(input)
            val movedInput = translatePoints(input, -center.x.toFloat(), -center.y.toFloat())
            val tempPoints = affineTransformation(movedInput, matrix)
            translatePoints(tempPoints, center.x.toFloat(), center.y.toFloat())
        } else {
            affineTransformation(input, matrix)
        }
    }

    private fun getIdentityMatrix(): MutableList<Double> {
        return mutableListOf(
            1.0, 0.0, 0.0, 0.0,
            0.0, 1.0, 0.0, 0.0,
            0.0, 0.0, 1.0, 0.0,
            0.0, 0.0, 0.0, 1.0
        )
    }

    private fun transformThreeDCoordinate(
        threeDCoordinatesModel: ThreeDCoordinatesModel,
        matrix: List<Double>
    ): ThreeDCoordinatesModel {
        val result = ThreeDCoordinatesModel()
        if (matrix.size >= 16) {
            result.x = matrix[0] * threeDCoordinatesModel.x +
                    matrix[1] * threeDCoordinatesModel.y +
                    matrix[2] * threeDCoordinatesModel.z +
                    matrix[3]
            result.y = matrix[4] * threeDCoordinatesModel.x +
                    matrix[5] * threeDCoordinatesModel.y +
                    matrix[6] * threeDCoordinatesModel.z +
                    matrix[7]
            result.z = matrix[8] * threeDCoordinatesModel.x +
                    matrix[9] * threeDCoordinatesModel.y +
                    matrix[10] * threeDCoordinatesModel.z +
                    matrix[11]
            result.w = matrix[12] * threeDCoordinatesModel.x +
                    matrix[13] * threeDCoordinatesModel.y +
                    matrix[14] * threeDCoordinatesModel.z +
                    matrix[15]
        }
        return result
    }

    private fun transformThreeDCoordinates(
        threeDCoordinatesList: List<ThreeDCoordinatesModel>,
        matrix: List<Double>
    ): List<ThreeDCoordinatesModel> {
        val resultList = mutableListOf<ThreeDCoordinatesModel>()
        if (matrix.size >= 16) {
            for (coordinate in threeDCoordinatesList) {
                val newCoordinate = transformThreeDCoordinate(coordinate, matrix)
                newCoordinate.normalize()
                resultList.add(newCoordinate)
            }
        }
        return resultList
    }

    fun centerOfThreeDCoordinates(input: List<ThreeDCoordinatesModel>): ThreeDCoordinatesModel {
        var sumX = 0.0
        var sumY = 0.0
        var sumZ = 0.0
        for (coordinate in input) {
            sumX += coordinate.x
            sumY += coordinate.y
            sumZ += coordinate.z
        }
        return ThreeDCoordinatesModel((sumX / input.size), sumY / input.size, sumZ / input.size)
    }

    fun translateThreeDCoordinates(
        threeDCoordinatesList: List<ThreeDCoordinatesModel>,
        tx: Double,
        ty: Double,
        tz: Double
    ): List<ThreeDCoordinatesModel> {
        val matrix = getIdentityMatrix()
        matrix[3] = tx
        matrix[7] = ty
        matrix[11] = tz
        return transformThreeDCoordinates(threeDCoordinatesList, matrix)
    }

    fun scaleThreeDCoordinates(
        threeDCoordinatesList: List<ThreeDCoordinatesModel>,
        sx: Double,
        sy: Double,
        sz: Double,
        useCenter: Boolean = true
    ): List<ThreeDCoordinatesModel> {
        val matrix = getIdentityMatrix()
        matrix[0] = sx
        matrix[5] = sy
        matrix[10] = sz
        return if (useCenter) {
            val center = centerOfThreeDCoordinates(threeDCoordinatesList)
            val movedInput =
                translateThreeDCoordinates(threeDCoordinatesList, -center.x, -center.y, -center.z)
            val tempCoordinates = transformThreeDCoordinates(movedInput, matrix)
            return translateThreeDCoordinates(tempCoordinates, center.x, center.y, center.z)
        } else {
            transformThreeDCoordinates(threeDCoordinatesList, matrix)
        }
    }

    fun shearThreeDCoordinates(
        threeDCoordinatesList: List<ThreeDCoordinatesModel>,
        shearX: Double,
        shearY: Double,
        useCenter: Boolean = true
    ): List<ThreeDCoordinatesModel> {
        val matrix = getIdentityMatrix()
        matrix[2] = shearX
        matrix[6] = shearY
        return if (useCenter) {
            val center = centerOfThreeDCoordinates(threeDCoordinatesList)
            val movedInput =
                translateThreeDCoordinates(threeDCoordinatesList, -center.x, -center.y, -center.z)
            val tempCoordinates = transformThreeDCoordinates(movedInput, matrix)
            return translateThreeDCoordinates(tempCoordinates, center.x, center.y, center.z)
        } else {
            transformThreeDCoordinates(threeDCoordinatesList, matrix)
        }
    }

    fun rotateInXThreeDCoordinates(
        threeDCoordinatesList: List<ThreeDCoordinatesModel>,
        angle: Float,
        useCenter: Boolean = true
    ): List<ThreeDCoordinatesModel> {
        val radians = angleToRadians(angle)
        val matrix = getIdentityMatrix()
        matrix[5] = cos(radians).toDouble()
        matrix[6] = -sin(radians).toDouble()
        matrix[9] = sin(radians).toDouble()
        matrix[10] = cos(radians).toDouble()
        return if (useCenter) {
            val center = centerOfThreeDCoordinates(threeDCoordinatesList)
            val movedInput =
                translateThreeDCoordinates(threeDCoordinatesList, -center.x, -center.y, -center.z)
            val tempCoordinates = transformThreeDCoordinates(movedInput, matrix)
            return translateThreeDCoordinates(tempCoordinates, center.x, center.y, center.z)
        } else {
            transformThreeDCoordinates(threeDCoordinatesList, matrix)
        }
    }

    fun rotateInYThreeDCoordinates(
        threeDCoordinatesList: List<ThreeDCoordinatesModel>,
        angle: Float,
        useCenter: Boolean = true
    ): List<ThreeDCoordinatesModel> {
        val radians = angleToRadians(angle)
        val matrix = getIdentityMatrix()
        matrix[0] = cos(radians).toDouble()
        matrix[2] = sin(radians).toDouble()
        matrix[8] = -sin(radians).toDouble()
        matrix[10] = cos(radians).toDouble()
        return if (useCenter) {
            val center = centerOfThreeDCoordinates(threeDCoordinatesList)
            val movedInput =
                translateThreeDCoordinates(threeDCoordinatesList, -center.x, -center.y, -center.z)
            val tempCoordinates = transformThreeDCoordinates(movedInput, matrix)
            return translateThreeDCoordinates(tempCoordinates, center.x, center.y, center.z)
        } else {
            transformThreeDCoordinates(threeDCoordinatesList, matrix)
        }
    }

    fun rotateInZThreeDCoordinates(
        threeDCoordinatesList: List<ThreeDCoordinatesModel>,
        angle: Float,
        useCenter: Boolean = true
    ): List<ThreeDCoordinatesModel> {
        val radians = angleToRadians(angle)
        val matrix = getIdentityMatrix()
        matrix[0] = cos(radians).toDouble()
        matrix[1] = -sin(radians).toDouble()
        matrix[4] = sin(radians).toDouble()
        matrix[5] = cos(radians).toDouble()
        return if (useCenter) {
            val center = centerOfThreeDCoordinates(threeDCoordinatesList)
            val movedInput =
                translateThreeDCoordinates(threeDCoordinatesList, -center.x, -center.y, -center.z)
            val tempCoordinates = transformThreeDCoordinates(movedInput, matrix)
            return translateThreeDCoordinates(tempCoordinates, center.x, center.y, center.z)
        } else {
            transformThreeDCoordinates(threeDCoordinatesList, matrix)
        }
    }

    fun quaternionRotateThreeDCoordinates(
        threeDCoordinatesList: List<ThreeDCoordinatesModel>,
        angle: Float,
        xAxis: Double,
        yAxis: Double,
        zAxis: Double
    ): List<ThreeDCoordinatesModel> {
        val resultList = mutableListOf<ThreeDCoordinatesModel>()
        val theta = angleToRadians(angle) / 2
        val sinTheta = sin(theta)
        val cosTheta = cos(theta)
        val matrix = getIdentityMatrix()
        val w = cosTheta
        val x = xAxis * sinTheta
        val y = yAxis * sinTheta
        val z = zAxis * sinTheta
        matrix[0] = w.pow(2) + x.pow(2) - y.pow(2) - z.pow(2)
        matrix[1] = 2 * x * y - 2 * w * z
        matrix[2] = 2 * x * z + 2 * w * y
        matrix[4] = 2 * x * y + 2 * w * z
        matrix[5] = w.pow(2) + y.pow(2) - x.pow(2) - z.pow(2)
        matrix[6] = 2 * y * z - 2 * w * x
        matrix[8] = 2 * x * z - 2 * w * y
        matrix[9] = 2 * y * z + 2 * w * x
        matrix[10] = w.pow(2) + z.pow(2) - x.pow(2) - y.pow(2)
        for (coordinate in threeDCoordinatesList) {
            val newCoordinate = transformThreeDCoordinate(coordinate, matrix)
            newCoordinate.normalize()
            resultList.add(newCoordinate)
        }
        return resultList
    }

}