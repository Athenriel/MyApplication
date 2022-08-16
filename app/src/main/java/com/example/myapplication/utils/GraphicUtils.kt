package com.example.myapplication.utils

import android.graphics.Path
import android.graphics.Point
import kotlin.math.cos
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

    fun rotatePoints(input: List<Point>, angle: Float): List<Point> {
        val center = centerOfPoints(input)
        val movedInput = translatePoints(input, -center.x.toFloat(), -center.y.toFloat())
        val radians = angleToRadians(angle)
        val matrix: Array<FloatArray> = arrayOf(
            floatArrayOf(cos(radians), -sin(radians), 0f), floatArrayOf(
                sin(radians), cos(radians), 0f
            ), floatArrayOf(0f, 0f, 1f)
        )
        val tempPoints = affineTransformation(movedInput, matrix)
        return translatePoints(tempPoints, center.x.toFloat(), center.y.toFloat())
    }

    fun scalePoints(input: List<Point>, scaleX: Float, scaleY: Float): List<Point> {
        val center = centerOfPoints(input)
        val movedInput = translatePoints(input, -center.x.toFloat(), -center.y.toFloat())
        val matrix: Array<FloatArray> =
            arrayOf(
                floatArrayOf(scaleX, 0f, 0f),
                floatArrayOf(0f, scaleY, 0f),
                floatArrayOf(0f, 0f, 1f)
            )
        val tempPoints = affineTransformation(movedInput, matrix)
        return translatePoints(tempPoints, center.x.toFloat(), center.y.toFloat())
    }

    fun shearPoints(input: List<Point>, shearX: Float, shearY: Float): List<Point> {
        val center = centerOfPoints(input)
        val movedInput = translatePoints(input, -center.x.toFloat(), -center.y.toFloat())
        val matrix: Array<FloatArray> =
            arrayOf(
                floatArrayOf(1f, shearX, 0f),
                floatArrayOf(shearY, 1f, 0f),
                floatArrayOf(0f, 0f, 1f)
            )
        val tempPoints = affineTransformation(movedInput, matrix)
        return translatePoints(tempPoints, center.x.toFloat(), center.y.toFloat())
    }

    fun reflectPointsInX(input: List<Point>): List<Point> {
        val center = centerOfPoints(input)
        val movedInput = translatePoints(input, -center.x.toFloat(), -center.y.toFloat())
        val matrix: Array<FloatArray> =
            arrayOf(
                floatArrayOf(1f, 0f, 0f),
                floatArrayOf(0f, -1f, 0f),
                floatArrayOf(0f, 0f, 1f)
            )
        val tempPoints = affineTransformation(movedInput, matrix)
        return translatePoints(tempPoints, center.x.toFloat(), center.y.toFloat())
    }

    fun reflectPointsInY(input: List<Point>): List<Point> {
        val center = centerOfPoints(input)
        val movedInput = translatePoints(input, -center.x.toFloat(), -center.y.toFloat())
        val matrix: Array<FloatArray> =
            arrayOf(
                floatArrayOf(-1f, 0f, 0f),
                floatArrayOf(0f, 1f, 0f),
                floatArrayOf(0f, 0f, 1f)
            )
        val tempPoints = affineTransformation(movedInput, matrix)
        return translatePoints(tempPoints, center.x.toFloat(), center.y.toFloat())
    }

}