package com.example.myapplication.ui.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import com.example.myapplication.utils.createLineGraph

/**
 * Created by Athenriel on 8/18/2022
 */
class LinePlotView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val blackStrokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE //stroke only no fill
        color = Color.BLACK //color black
        strokeWidth = 5f //set the line stroke width to 5
    }

    private val blueStrokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE //stroke only no fill
        color = Color.BLUE //color blue
        strokeWidth = 5f //set the line stroke width to 5
    }

    private val redStrokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE //stroke only no fill
        color = Color.RED //color red
        strokeWidth = 5f //set the line stroke width to 5
    }

    private var dataList = mutableListOf<Int>()

    private val xBuffer = 10
    private val yBuffer = 10

    private val path = Path()

    fun setDataList(list: List<Int>) {
        dataList.clear()
        dataList.addAll(list)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //Add your drawing code here
        canvas.drawLine(
            0F,
            height.toFloat() - yBuffer,
            width.toFloat(),
            height.toFloat() - yBuffer,
            blueStrokePaint
        )
        canvas.drawLine(xBuffer.toFloat(), height.toFloat(), xBuffer.toFloat(), 0F, redStrokePaint)
        path.createLineGraph(dataList, width, height, xBuffer, yBuffer)
        canvas.drawPath(path, blackStrokePaint)
    }

}