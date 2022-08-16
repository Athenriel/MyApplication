package com.example.myapplication.ui.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

/**
 * Created by Athenriel on 8/19/2022
 */
class PiePlotView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val blueFillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL //fill only no stroke
        color = Color.BLUE //color blue
        strokeWidth = 5f //set the line stroke width to 5
    }

    private val redFillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL //fill only no stroke
        color = Color.RED //color red
        strokeWidth = 5f //set the line stroke width to 5
    }

    private val greenFillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL //fill only no stroke
        color = Color.GREEN //color green
        strokeWidth = 5f //set the line stroke width to 5
    }

    private val cyanFillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL //fill only no stroke
        color = Color.CYAN //color cyan
        strokeWidth = 5f //set the line stroke width to 5
    }

    private val grayFillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL //fill only no stroke
        color = Color.GRAY //color gray
        strokeWidth = 5f //set the line stroke width to 5
    }

    private val magentaFillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL //fill only no stroke
        color = Color.MAGENTA //color magenta
        strokeWidth = 5f //set the line stroke width to 5
    }

    private val yellowFillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL //fill only no stroke
        color = Color.YELLOW //color yellow
        strokeWidth = 5f //set the line stroke width to 5
    }

    private val blackFillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL //fill only no stroke
        color = Color.BLACK //color black
        strokeWidth = 5f //set the line stroke width to 5
    }

    private val colorList = listOf(
        blueFillPaint,
        redFillPaint,
        greenFillPaint,
        cyanFillPaint,
        grayFillPaint,
        magentaFillPaint,
        yellowFillPaint,
        blackFillPaint
    )

    private val colorLimit = 8

    private var currentColor = 0

    private val dataList = listOf(
        1,
        1,
        2,
        3,
        5,
        8,
        13,
        21,
        34,
        55,
        89
    )

    private var rectF = RectF(0F, 0F, 0F, 0F)

    private var initialized = false

    private fun getNextPaint(): Paint {
        val paint = if (currentColor < colorLimit) {
            colorList[currentColor]
        } else {
            currentColor = 0
            colorList[currentColor]
        }
        currentColor++
        return paint
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //Add your drawing code here
        if (!initialized) {
            rectF = RectF(0F, 0F, width.toFloat(), width.toFloat())
            initialized = true
        }
        var dataSum = 0
        for (data in dataList) {
            dataSum += data
        }
        val scaledValues = mutableListOf<Float>()
        for (data in dataList) {
            scaledValues.add(data.toFloat() / dataSum * 360)
        }
        var start = 0F
        for (i in dataList.indices) {
            canvas.drawArc(rectF, start, scaledValues[i], true, getNextPaint())
            start += scaledValues[i]
        }
    }

}