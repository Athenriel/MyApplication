package com.example.myapplication.ui.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.example.myapplication.model.ThreeDCoordinatesModel
import timber.log.Timber

/**
 * Created by Athenriel on 8/23/2022
 */
class ThreeDCubeView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val blackStrokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE //stroke only no fill
        color = Color.BLACK //color black
        strokeWidth = 2f //set the line stroke width to 2
    }

    private val blueStrokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE //stroke only no fill
        color = Color.BLUE //color blue
        strokeWidth = 2f //set the line stroke width to 2
    }

    private val redStrokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE //stroke only no fill
        color = Color.RED //color red
        strokeWidth = 2f //set the line stroke width to 2
    }

    private val threeDCoordinateList = mutableListOf<ThreeDCoordinatesModel>()

    fun setThreeDCoordinates(coordinateList: List<ThreeDCoordinatesModel>) {
        threeDCoordinateList.clear()
        threeDCoordinateList.addAll(coordinateList)
        invalidate()
    }

    private fun drawLinePairs(
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

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //Add your drawing code here
        try {
            drawLinePairs(canvas, threeDCoordinateList, 0, 1, redStrokePaint)
            drawLinePairs(canvas, threeDCoordinateList, 1, 3, redStrokePaint)
            drawLinePairs(canvas, threeDCoordinateList, 3, 2, redStrokePaint)
            drawLinePairs(canvas, threeDCoordinateList, 2, 0, redStrokePaint)
            drawLinePairs(canvas, threeDCoordinateList, 4, 5, redStrokePaint)
            drawLinePairs(canvas, threeDCoordinateList, 5, 7, redStrokePaint)
            drawLinePairs(canvas, threeDCoordinateList, 7, 6, redStrokePaint)
            drawLinePairs(canvas, threeDCoordinateList, 6, 4, redStrokePaint)
            drawLinePairs(canvas, threeDCoordinateList, 0, 4, redStrokePaint)
            drawLinePairs(canvas, threeDCoordinateList, 1, 5, redStrokePaint)
            drawLinePairs(canvas, threeDCoordinateList, 2, 6, redStrokePaint)
            drawLinePairs(canvas, threeDCoordinateList, 3, 7, redStrokePaint)
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

}