package com.example.myapplication.ui.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class MyView(context: Context, attrs: AttributeSet): View(context, attrs) {

    private var redStrokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE //stroke only no fill
        color = -0x10000 //color red
        strokeWidth = 5f //set the line stroke width to 5
    }

    private var blueStrokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE //stroke only no fill
        color = Color.BLUE //color blue
        strokeWidth = 5f //set the line stroke width to 5
    }

    private var greenStrokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE //stroke only no fill
        color = Color.GREEN //color green
        strokeWidth = 5f //set the line stroke width to 5
    }

    private var blackStrokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE //stroke only no fill
        color = Color.BLACK //color black
        strokeWidth = 5f //set the line stroke width to 5
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //Add your drawing code here
        canvas.drawRect(10f, 30f, 200f, 200f, redStrokePaint)
        canvas.drawCircle(300f, 300f, 250f, blueStrokePaint)

        canvas.drawCircle(500f, 450f, 50f, greenStrokePaint)

        canvas.drawRect(500f, 500f, 700f, 700f, blackStrokePaint)
        canvas.drawCircle(600f, 600f, 145f, blackStrokePaint)
    }

}