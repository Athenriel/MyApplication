package com.example.myapplication.ui.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class MyView(context: Context, attrs: AttributeSet): View(context, attrs) {

    private var redPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE //stroke only no fill
        color = -0x10000 //color red
        strokeWidth = 5f //set the line stroke width to 5
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //Add your drawing code here
        canvas.drawRect(10f, 30f, 200f, 200f, redPaint)
    }

}