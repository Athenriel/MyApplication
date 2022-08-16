package com.example.myapplication.ui.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

/**
 * Created by Athenriel on 8/16/2022
 */
class PolylinesView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val redStrokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE //stroke only no fill
        color = Color.RED //color red
        strokeWidth = 5f //set the line stroke width to 5
    }

    private val redFillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL //fill only no stroke
        color = Color.RED //color red
    }

    private val blueStrokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE //stroke only no fill
        color = Color.BLUE //color blue
        strokeWidth = 5f //set the line stroke width to 5
    }

    private val greenStrokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE //stroke only no fill
        setARGB(255, 0, 255, 0) //color green
        strokeWidth = 5f //set the line stroke width to 5
    }

    private val blackStrokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE //stroke only no fill
        color = Color.BLACK //color black
        strokeWidth = 5f //set the line stroke width to 5
    }

    private val linearBlueRedMirrorGradient =
        LinearGradient(250f, 150f, 450f, 200f, Color.BLUE, Color.RED, Shader.TileMode.MIRROR)

    private val blueRedMirrorGradientFillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL //fill only no stroke
        shader = linearBlueRedMirrorGradient
    }

    private val linearBlueRedRepeatGradient =
        LinearGradient(250f, 250f, 350f, 300f, Color.BLUE, Color.RED, Shader.TileMode.REPEAT)

    private val blueRedRepeatGradientFillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL //fill only no stroke
        shader = linearBlueRedRepeatGradient
    }

    private val linearBlueRedMirrorClampedGradient =
        LinearGradient(500f, 150f, 600f, 150f, Color.BLUE, Color.RED, Shader.TileMode.MIRROR)

    private val blueRedMirrorClampedGradientFillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL //fill only no stroke
        shader = linearBlueRedMirrorClampedGradient
    }

    private val linearBlueRedMirrorExtendedGradient =
        LinearGradient(500f, 250f, 2000f, 2000f, Color.BLUE, Color.RED, Shader.TileMode.MIRROR)

    private val blueRedMirrorExtendedGradientFillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL //fill only no stroke
        shader = linearBlueRedMirrorExtendedGradient
    }

    private val zxPath = Path().apply {
        moveTo(50f, 50f)
        lineTo(100f, 50f)
        lineTo(50f, 100f)
        lineTo(100f, 100f)
        moveTo(150f, 50f)
        lineTo(200f, 100f)
        moveTo(150f, 100f)
        lineTo(200f, 50f)
    }

    private val path = Path().apply {
        moveTo(50f, 300f)
        lineTo(160f, 280f)
        lineTo(300f, 380f)
        lineTo(380f, 370f)
        lineTo(280f, 450f)
        lineTo(100f, 390f)
        lineTo(160f, 380f)
        lineTo(50f, 300f)
    }

    private val polygon = Path().apply {
        moveTo(250f, 50f)
        lineTo(300f, 100f)
        lineTo(350f, 50f)
        lineTo(400f, 100f)
        lineTo(450f, 50f)
        close()
    }

    private val wPolygon = Path().apply {
        moveTo(500f, 50f)
        lineTo(550f, 80f)
        lineTo(580f, 60f)
        lineTo(610f, 80f)
        lineTo(650f, 55f)
        close()
    }

    private val modifiedPolygon = Path().apply {
        moveTo(650f, 50f)
        lineTo(550f, 80f)
        lineTo(610f, 80f)
        lineTo(580f, 60f)
        lineTo(500f, 55f)
        close()
    }

    private val lowerPolygon = Path().apply {
        moveTo(250f, 150f)
        lineTo(300f, 200f)
        lineTo(350f, 150f)
        lineTo(400f, 200f)
        lineTo(450f, 150f)
        close()
    }

    private val lowestPolygon = Path().apply {
        moveTo(250f, 250f)
        lineTo(300f, 300f)
        lineTo(350f, 250f)
        lineTo(400f, 300f)
        lineTo(450f, 250f)
        close()
    }

    private val movedLowerPolygon = Path().apply {
        moveTo(500f, 150f)
        lineTo(550f, 200f)
        lineTo(600f, 150f)
        lineTo(650f, 200f)
        lineTo(700f, 150f)
        close()
    }

    private val movedLowestPolygon = Path().apply {
        moveTo(500f, 250f)
        lineTo(550f, 300f)
        lineTo(600f, 250f)
        lineTo(650f, 300f)
        lineTo(700f, 250f)
        close()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //Add your drawing code here
        canvas.drawPath(zxPath, greenStrokePaint)
        canvas.drawPath(path, redStrokePaint)
        canvas.drawCircle(185f, 356f, 250f, redStrokePaint)

        canvas.drawPath(polygon, blackStrokePaint)
        canvas.drawPath(polygon, redFillPaint)

        canvas.drawPath(wPolygon, blueStrokePaint)

        canvas.drawPath(modifiedPolygon, blackStrokePaint)
        canvas.drawPath(modifiedPolygon, redFillPaint)

        canvas.drawPath(lowerPolygon, blueRedMirrorGradientFillPaint)
        canvas.drawPath(lowestPolygon, blueRedRepeatGradientFillPaint)

        canvas.drawPath(movedLowerPolygon, blueRedMirrorClampedGradientFillPaint)
        canvas.drawPath(movedLowestPolygon, blueRedMirrorExtendedGradientFillPaint)
    }

}