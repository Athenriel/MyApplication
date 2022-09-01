package com.example.myapplication.model

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import com.example.myapplication.utils.GraphicUtils
import com.example.myapplication.utils.GraphicUtils.drawLinePairs

/**
 * Created by Athenriel on 9/1/2022
 */
class ThreeDCubeModel {

    private val dataList: MutableList<ThreeDCoordinatesModel> = mutableListOf()

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE //stroke only no fill
        color = Color.BLACK //color black
        strokeWidth = 2f //set the line stroke width to 2
    }

    fun setCoordinateList(coordinates: List<ThreeDCoordinatesModel>) {
        dataList.clear()
        if (coordinates.size == 8) {
            dataList.addAll(coordinates)
        }
    }

    fun translate(newX: Double, newY: Double, newZ: Double) {
        setCoordinateList(
            GraphicUtils.translateThreeDCoordinates(
                dataList,
                newX,
                newY,
                newZ
            )
        )
    }

    fun scale(scaleX: Double, scaleY: Double, scaleZ: Double) {
        setCoordinateList(
            GraphicUtils.scaleThreeDCoordinates(
                dataList,
                scaleX,
                scaleY,
                scaleZ
            )
        )
    }

    fun shear(shearX: Double, shearY: Double) {
        setCoordinateList(
            GraphicUtils.shearThreeDCoordinates(
                dataList,
                shearX,
                shearY
            )
        )
    }

    fun quaternionRotate(
        angle: Float,
        xAxis: Double,
        yAxis: Double,
        zAxis: Double,
        rotationType: GraphicUtils.RotationType = GraphicUtils.RotationType.CENTER,
        reference: ThreeDCoordinatesModel? = null
    ) {
        setCoordinateList(
            GraphicUtils.quaternionRotateThreeDCoordinates(
                dataList,
                angle,
                xAxis,
                yAxis,
                zAxis,
                rotationType,
                reference
            )
        )
    }

    fun getCoordinateList(): List<ThreeDCoordinatesModel> {
        return dataList
    }

    fun setPaintColor(red: Int, green: Int, blue: Int) {
        if (red >= 0 && green >= 0 && blue >= 0 &&
            red < 256 && green < 256 && blue < 256
        ) {
            paint.setARGB(255, red, green, blue)
        }
    }

    fun setPaintStyle(style: Paint.Style) {
        paint.style = style
    }

    fun drawCubeOnCanvas(canvas: Canvas) {
        drawLinePairs(canvas, dataList, 0, 1, paint)
        drawLinePairs(canvas, dataList, 1, 3, paint)
        drawLinePairs(canvas, dataList, 3, 2, paint)
        drawLinePairs(canvas, dataList, 2, 0, paint)
        drawLinePairs(canvas, dataList, 4, 5, paint)
        drawLinePairs(canvas, dataList, 5, 7, paint)
        drawLinePairs(canvas, dataList, 7, 6, paint)
        drawLinePairs(canvas, dataList, 6, 4, paint)
        drawLinePairs(canvas, dataList, 0, 4, paint)
        drawLinePairs(canvas, dataList, 1, 5, paint)
        drawLinePairs(canvas, dataList, 2, 6, paint)
        drawLinePairs(canvas, dataList, 3, 7, paint)
    }

    fun drawFilledCubeOnCanvas(canvas: Canvas, path: Path) {
        path.moveTo(dataList[0].x.toFloat(), dataList[0].y.toFloat())
        path.lineTo(dataList[1].x.toFloat(), dataList[1].y.toFloat())
        path.lineTo(dataList[3].x.toFloat(), dataList[3].y.toFloat())
        path.lineTo(dataList[2].x.toFloat(), dataList[2].y.toFloat())
        path.lineTo(dataList[0].x.toFloat(), dataList[0].y.toFloat())
        path.close()
        canvas.drawPath(path, paint)
        path.reset()

        path.moveTo(dataList[4].x.toFloat(), dataList[4].y.toFloat())
        path.lineTo(dataList[5].x.toFloat(), dataList[5].y.toFloat())
        path.lineTo(dataList[7].x.toFloat(), dataList[7].y.toFloat())
        path.lineTo(dataList[6].x.toFloat(), dataList[6].y.toFloat())
        path.lineTo(dataList[4].x.toFloat(), dataList[4].y.toFloat())
        path.close()
        canvas.drawPath(path, paint)
        path.reset()

        path.moveTo(dataList[0].x.toFloat(), dataList[0].y.toFloat())
        path.lineTo(dataList[4].x.toFloat(), dataList[4].y.toFloat())
        path.lineTo(dataList[6].x.toFloat(), dataList[6].y.toFloat())
        path.lineTo(dataList[2].x.toFloat(), dataList[2].y.toFloat())
        path.lineTo(dataList[0].x.toFloat(), dataList[0].y.toFloat())
        path.close()
        canvas.drawPath(path, paint)
        path.reset()

        path.moveTo(dataList[0].x.toFloat(), dataList[0].y.toFloat())
        path.lineTo(dataList[4].x.toFloat(), dataList[4].y.toFloat())
        path.lineTo(dataList[5].x.toFloat(), dataList[5].y.toFloat())
        path.lineTo(dataList[1].x.toFloat(), dataList[1].y.toFloat())
        path.lineTo(dataList[0].x.toFloat(), dataList[0].y.toFloat())
        path.close()
        canvas.drawPath(path, paint)
        path.reset()

        path.moveTo(dataList[1].x.toFloat(), dataList[1].y.toFloat())
        path.lineTo(dataList[5].x.toFloat(), dataList[5].y.toFloat())
        path.lineTo(dataList[7].x.toFloat(), dataList[7].y.toFloat())
        path.lineTo(dataList[3].x.toFloat(), dataList[3].y.toFloat())
        path.lineTo(dataList[1].x.toFloat(), dataList[1].y.toFloat())
        path.close()
        canvas.drawPath(path, paint)
        path.reset()

        path.moveTo(dataList[3].x.toFloat(), dataList[3].y.toFloat())
        path.lineTo(dataList[7].x.toFloat(), dataList[7].y.toFloat())
        path.lineTo(dataList[6].x.toFloat(), dataList[6].y.toFloat())
        path.lineTo(dataList[2].x.toFloat(), dataList[2].y.toFloat())
        path.lineTo(dataList[3].x.toFloat(), dataList[3].y.toFloat())
        path.close()
        canvas.drawPath(path, paint)
        path.reset()

    }

}