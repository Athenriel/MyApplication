package com.example.myapplication.utils

import android.content.res.Resources
import android.graphics.*
import android.opengl.GLES32
import android.opengl.GLUtils
import com.example.myapplication.model.ThreeDCoordinatesModel
import com.example.myapplication.model.ThreeDFloatsModel
import timber.log.Timber
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.tan

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

    fun centerOfPointFs(input: List<PointF>): PointF {
        var sumX = 0f
        var sumY = 0f
        for (point in input) {
            sumX += point.x
            sumY += point.y
        }
        return PointF((sumX / input.size), sumY / input.size)
    }

    fun getBezierCurveVertices(
        z: Float,
        bezierPointFList: List<PointF>
    ): ArrayList<ThreeDFloatsModel> {
        val tValues = listOf(
            0f,
            0.1f,
            0.2f,
            0.3f,
            0.4f,
            0.5f,
            0.6f,
            0.7f,
            0.8f,
            0.9f,
            1f
        )
        val bezierVertices = arrayListOf<ThreeDFloatsModel>()
        if (bezierPointFList.size >= 4) {
            for (t in tValues) {
                val x = (1 - t).pow(3) * bezierPointFList[0].x +
                        bezierPointFList[1].x * 3 * t * (1 - t).pow(2) +
                        bezierPointFList[2].x * 3 * t.pow(2) * (1 - t) +
                        bezierPointFList[3].x * t.pow(3)
                val y = (1 - t).pow(3) * bezierPointFList[0].y +
                        bezierPointFList[1].y * 3 * t * (1 - t).pow(2) +
                        bezierPointFList[2].y * 3 * t.pow(2) * (1 - t) +
                        bezierPointFList[3].y * t.pow(3)
                bezierVertices.add(ThreeDFloatsModel(x, y, z))
            }
        }
        return bezierVertices
    }

    fun translateFloatArrayVertices(
        array: FloatArray,
        translateX: Float,
        translateY: Float,
        translateZ: Float
    ) {
        for (i in array.indices step 3) {
            array[i] += translateX
            array[i + 1] += translateY
            array[i + 2] += translateZ
        }
    }

    fun getVerticesForSphere(
        radius: Float,
        latitudes: Int,
        longitudes: Int,
        dist: Float
    ): ArrayList<ThreeDFloatsModel> {
        val vertices = arrayListOf<ThreeDFloatsModel>()
        for (i in 0..latitudes) {
            val theta = i * Math.PI / latitudes
            val sinTheta = sin(theta)
            val cosTheta = cos(theta)
            for (j in 0..longitudes) {
                val phi = j * 2 * Math.PI / longitudes
                val sinPhi = sin(phi)
                val cosPhi = cos(phi)
                val x = cosPhi * sinTheta
                val y = cosTheta
                val z = sinPhi * sinTheta
                vertices.add(
                    ThreeDFloatsModel(
                        radius * x.toFloat(),
                        radius * y.toFloat() + dist,
                        radius * z.toFloat()
                    )
                )
            }
        }
        return vertices
    }

    fun getTrianglesIndexesForSphere(
        latitudes: Int,
        longitudes: Int
    ): ArrayList<Int> {
        val indexesList = arrayListOf<Int>()
        for (i in 0 until latitudes) {
            for (j in 0 until longitudes) {
                val p0 = i * (longitudes + 1) + j
                val p1 = p0 + longitudes + 1

                indexesList.add(p0)
                indexesList.add(p1)
                indexesList.add(p0 + 1)

                indexesList.add(p1)
                indexesList.add(p1 + 1)
                indexesList.add(p0 + 1)
            }
        }
        return indexesList
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

    private fun topOrBottomOfThreeDCoordinates(
        input: List<ThreeDCoordinatesModel>,
        top: Boolean
    ): ThreeDCoordinatesModel {
        var sumX = 0.0
        var sumY = 0.0
        var sumZ = 0.0
        var tempList = mutableListOf<ThreeDCoordinatesModel>()
        tempList.addAll(input)
        if (top) {
            tempList.sortBy { it.y }
        } else {
            tempList.sortByDescending { it.y }
        }
        tempList = tempList.subList(0, 3)
        for (coordinate in tempList) {
            sumX += coordinate.x
            sumY += coordinate.y
            sumZ += coordinate.z
        }
        return ThreeDCoordinatesModel(
            (sumX / tempList.size),
            sumY / tempList.size,
            sumZ / tempList.size
        )
    }

    private fun startOrEndOfThreeDCoordinates(
        input: List<ThreeDCoordinatesModel>,
        start: Boolean
    ): ThreeDCoordinatesModel {
        var sumX = 0.0
        var sumY = 0.0
        var sumZ = 0.0
        var tempList = mutableListOf<ThreeDCoordinatesModel>()
        tempList.addAll(input)
        if (start) {
            tempList.sortBy { it.x }
        } else {
            tempList.sortByDescending { it.x }
        }
        tempList = tempList.subList(0, 3)
        for (coordinate in tempList) {
            sumX += coordinate.x
            sumY += coordinate.y
            sumZ += coordinate.z
        }
        return ThreeDCoordinatesModel(
            (sumX / tempList.size),
            sumY / tempList.size,
            sumZ / tempList.size
        )
    }

    private fun frontOrBackOfThreeDCoordinates(
        input: List<ThreeDCoordinatesModel>,
        front: Boolean
    ): ThreeDCoordinatesModel {
        var sumX = 0.0
        var sumY = 0.0
        var sumZ = 0.0
        var tempList = mutableListOf<ThreeDCoordinatesModel>()
        tempList.addAll(input)
        if (front) {
            tempList.sortBy { it.z }
        } else {
            tempList.sortByDescending { it.z }
        }
        tempList = tempList.subList(0, 3)
        for (coordinate in tempList) {
            sumX += coordinate.x
            sumY += coordinate.y
            sumZ += coordinate.z
        }
        return ThreeDCoordinatesModel(
            (sumX / tempList.size),
            sumY / tempList.size,
            sumZ / tempList.size
        )
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
        zAxis: Double,
        rotationType: RotationType = RotationType.CENTER,
        reference: ThreeDCoordinatesModel? = null
    ): List<ThreeDCoordinatesModel> {
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
        return when (rotationType) {
            RotationType.CENTER -> {
                val center = centerOfThreeDCoordinates(threeDCoordinatesList)
                val movedInput =
                    translateThreeDCoordinates(
                        threeDCoordinatesList,
                        -center.x,
                        -center.y,
                        -center.z
                    )
                val tempCoordinates = transformThreeDCoordinates(movedInput, matrix)
                translateThreeDCoordinates(tempCoordinates, center.x, center.y, center.z)
            }
            RotationType.START -> {
                val start = startOrEndOfThreeDCoordinates(threeDCoordinatesList, true)
                val movedInput =
                    translateThreeDCoordinates(threeDCoordinatesList, -start.x, -start.y, -start.z)
                val tempCoordinates = transformThreeDCoordinates(movedInput, matrix)
                translateThreeDCoordinates(tempCoordinates, start.x, start.y, start.z)
            }
            RotationType.END -> {
                val end = startOrEndOfThreeDCoordinates(threeDCoordinatesList, false)
                val movedInput =
                    translateThreeDCoordinates(threeDCoordinatesList, -end.x, -end.y, -end.z)
                val tempCoordinates = transformThreeDCoordinates(movedInput, matrix)
                translateThreeDCoordinates(tempCoordinates, end.x, end.y, end.z)
            }
            RotationType.TOP -> {
                val top = topOrBottomOfThreeDCoordinates(threeDCoordinatesList, true)
                val movedInput =
                    translateThreeDCoordinates(threeDCoordinatesList, -top.x, -top.y, -top.z)
                val tempCoordinates = transformThreeDCoordinates(movedInput, matrix)
                translateThreeDCoordinates(tempCoordinates, top.x, top.y, top.z)
            }
            RotationType.BOTTOM -> {
                val bottom = topOrBottomOfThreeDCoordinates(threeDCoordinatesList, false)
                val movedInput =
                    translateThreeDCoordinates(
                        threeDCoordinatesList,
                        -bottom.x,
                        -bottom.y,
                        -bottom.z
                    )
                val tempCoordinates = transformThreeDCoordinates(movedInput, matrix)
                translateThreeDCoordinates(tempCoordinates, bottom.x, bottom.y, bottom.z)
            }
            RotationType.FRONT -> {
                val front = frontOrBackOfThreeDCoordinates(threeDCoordinatesList, true)
                val movedInput =
                    translateThreeDCoordinates(threeDCoordinatesList, -front.x, -front.y, -front.z)
                val tempCoordinates = transformThreeDCoordinates(movedInput, matrix)
                translateThreeDCoordinates(tempCoordinates, front.x, front.y, front.z)
            }
            RotationType.BACK -> {
                val back = frontOrBackOfThreeDCoordinates(threeDCoordinatesList, false)
                val movedInput =
                    translateThreeDCoordinates(
                        threeDCoordinatesList,
                        -back.x,
                        -back.y,
                        -back.z
                    )
                val tempCoordinates = transformThreeDCoordinates(movedInput, matrix)
                translateThreeDCoordinates(tempCoordinates, back.x, back.y, back.z)
            }
            RotationType.REFERENCE -> {
                val referencePoint = reference ?: ThreeDCoordinatesModel(0.0, 0.0, 0.0, 1.0)
                val movedInput =
                    translateThreeDCoordinates(
                        threeDCoordinatesList,
                        -referencePoint.x,
                        -referencePoint.y,
                        -referencePoint.z
                    )
                val tempCoordinates = transformThreeDCoordinates(movedInput, matrix)
                translateThreeDCoordinates(
                    tempCoordinates,
                    referencePoint.x,
                    referencePoint.y,
                    referencePoint.z
                )
            }
            RotationType.NONE -> {
                transformThreeDCoordinates(threeDCoordinatesList, matrix)
            }
        }
    }

    fun orthogonalProjectThreeDCoordinates(
        threeDCoordinatesList: List<ThreeDCoordinatesModel>
    ): List<ThreeDCoordinatesModel> {
        val resultList = mutableListOf<ThreeDCoordinatesModel>()
        val matrix = getIdentityMatrix()
        matrix[10] = 0.0
        for (coordinate in threeDCoordinatesList) {
            val newCoordinate = transformThreeDCoordinate(coordinate, matrix)
            newCoordinate.normalize()
            resultList.add(newCoordinate)
        }
        return resultList
    }

    fun perspectiveProjectThreeDCoordinates(
        threeDCoordinatesList: List<ThreeDCoordinatesModel>,
        right: Double,
        left: Double,
        top: Double,
        bottom: Double,
        near: Double,
        far: Double
    ): List<ThreeDCoordinatesModel> {
        val resultList = mutableListOf<ThreeDCoordinatesModel>()
        val matrix = getIdentityMatrix()
        matrix[0] = (2 * near) / (right - left)
        matrix[2] = (right + left) / (right - left)
        matrix[5] = (2 * near) / (top - bottom)
        matrix[6] = (top + bottom) / (top - bottom)
        matrix[10] = -(far + near) / (far - near)
        matrix[11] = -(2 * far * near) / (far - near)
        matrix[14] = -1.0
        matrix[15] = 0.0
        for (coordinate in threeDCoordinatesList) {
            val newCoordinate = transformThreeDCoordinate(coordinate, matrix)
            newCoordinate.normalize()
            resultList.add(newCoordinate)
        }
        return resultList
    }

    fun symmetricViewFrustumProjectThreeDCoordinates(
        threeDCoordinatesList: List<ThreeDCoordinatesModel>,
        fieldOfView: Float,
        right: Double,
        left: Double,
        top: Double,
        bottom: Double,
        near: Double,
        far: Double
    ): List<ThreeDCoordinatesModel> {
        val resultList = mutableListOf<ThreeDCoordinatesModel>()
        val matrix = getIdentityMatrix()
        val aspectRatio = (right - left) / (top - bottom)
        matrix[0] = 1.0 / (aspectRatio * tan(fieldOfView / 2))
        matrix[5] = 1.0 / tan(fieldOfView / 2)
        matrix[10] = -(far + near) / (far - near)
        matrix[11] = -(2 * far * near) / (far - near)
        matrix[14] = -1.0
        matrix[15] = 0.0
        for (coordinate in threeDCoordinatesList) {
            val newCoordinate = transformThreeDCoordinate(coordinate, matrix)
            newCoordinate.normalize()
            resultList.add(newCoordinate)
        }
        return resultList
    }

    fun drawLinePairs(
        canvas: Canvas,
        coordinateList: List<ThreeDCoordinatesModel>,
        start: Int,
        end: Int,
        paint: Paint
    ) {
        if (coordinateList.size > end) {
            canvas.drawLine(
                coordinateList[start].x.toFloat(),
                coordinateList[start].y.toFloat(),
                coordinateList[end].x.toFloat(),
                coordinateList[end].y.toFloat(),
                paint
            )
        }
    }

    fun loadOpenGLTextureFromResources(resources: Resources, resourceId: Int): Int {
        val textureHandle = IntArray(1)
        GLES32.glGenTextures(1, textureHandle, 0)
        if (textureHandle[0] != 0) {
            val options = BitmapFactory.Options()
            options.inScaled = false
            val bitmap = BitmapFactory.decodeResource(resources, resourceId, options)
            GLES32.glBindTexture(GLES32.GL_TEXTURE_2D, textureHandle[0])
            GLUtils.texImage2D(GLES32.GL_TEXTURE_2D, 0, bitmap, 0)
            bitmap.recycle()
        } else {
            Timber.e("GLES32 Error generating texture handle")
        }

        return textureHandle[0]
    }

    enum class RotationType {
        CENTER, START, END, TOP, BOTTOM, FRONT, BACK, REFERENCE, NONE
    }

}