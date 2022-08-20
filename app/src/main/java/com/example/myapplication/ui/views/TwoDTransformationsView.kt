package com.example.myapplication.ui.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.example.myapplication.utils.GraphicUtils
import com.example.myapplication.utils.updatePath

/**
 * Created by Athenriel on 8/18/2022
 */
class TwoDTransformationsView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val linearBlueRedMirrorGradient =
        LinearGradient(500f, 400f, 600f, 300f, Color.BLUE, Color.RED, Shader.TileMode.MIRROR)

    private val blueRedMirrorGradientFillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL //fill only no stroke
        shader = linearBlueRedMirrorGradient
    }

    private val blackStrokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE //stroke only no fill
        color = Color.BLACK //color black
        strokeWidth = 5f //set the line stroke width to 5
    }

    private val redStrokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE //stroke only no fill
        color = Color.RED //color red
        strokeWidth = 5f //set the line stroke width to 5
    }

    private val greenStrokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE //stroke only no fill
        color = Color.GREEN //color green
        strokeWidth = 5f //set the line stroke width to 5
    }

    private val cyanStrokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE //stroke only no fill
        color = Color.CYAN //color cyan
        strokeWidth = 5f //set the line stroke width to 5
    }

    private val magentaStrokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE //stroke only no fill
        color = Color.MAGENTA //color magenta
        strokeWidth = 5f //set the line stroke width to 5
    }

    private val grayStrokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE //stroke only no fill
        color = Color.GRAY //color gray
        strokeWidth = 5f //set the line stroke width to 5
    }

    private val points = mutableListOf<Point>().apply {
        add(Point(50, 300))
        add(Point(150, 400))
        add(Point(180, 340))
        add(Point(240, 420))
        add(Point(300, 200))
    }

    private val referenceTrianglePoints = mutableListOf<Point>().apply {
        add(Point(100, 100))
        add(Point(200, 100))
        add(Point(200, 200))
    }

    private val secondPoints = mutableListOf<Point>().apply {
        add(Point(500, 300))
        add(Point(500, 400))
        add(Point(600, 400))
        add(Point(600, 300))
    }

    private val path = Path()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //Add your drawing code here
        path.updatePath(referenceTrianglePoints)
        canvas.drawPath(path, blueRedMirrorGradientFillPaint)
        val shearXTrianglePoints = GraphicUtils.shearPoints(referenceTrianglePoints, 2f, 0f)
        path.updatePath(shearXTrianglePoints)
        canvas.drawPath(path, cyanStrokePaint)
        val shearYTrianglePoints = GraphicUtils.shearPoints(referenceTrianglePoints, 0f, 2f)
        path.updatePath(shearYTrianglePoints)
        canvas.drawPath(path, magentaStrokePaint)
        path.updatePath(points)
        canvas.drawPath(path, blueRedMirrorGradientFillPaint)
        val translatePoints = GraphicUtils.translatePoints(points, 250f, -150f)
        path.updatePath(translatePoints)
        canvas.drawPath(path, blackStrokePaint)
        val rotatePoints = GraphicUtils.rotatePoints(points, -30f)
        path.updatePath(rotatePoints)
        canvas.drawPath(path, redStrokePaint)
        val scalePoints = GraphicUtils.scalePoints(points, 0.5f, 0.5f)
        path.updatePath(scalePoints)
        canvas.drawPath(path, greenStrokePaint)
        val shearPoints = GraphicUtils.shearPoints(points, 0f, 1f)
        path.updatePath(shearPoints)
        canvas.drawPath(path, cyanStrokePaint)
        val reflectXPoints = GraphicUtils.reflectPointsInX(points)
        path.updatePath(reflectXPoints)
        canvas.drawPath(path, magentaStrokePaint)
        val reflectYPoints = GraphicUtils.reflectPointsInY(points)
        path.updatePath(reflectYPoints)
        canvas.drawPath(path, grayStrokePaint)
        /*
        path.updatePath(points)
        canvas.drawPath(path, blueRedMirrorGradientFillPaint)
        var transformedPoints = GraphicUtils.shearPoints(points, 2f, 0f)
        path.updatePath(transformedPoints)
        canvas.drawPath(path, cyanStrokePaint)
        transformedPoints = GraphicUtils.scalePoints(transformedPoints, 0.5f, 3f)
        path.updatePath(transformedPoints)
        canvas.drawPath(path, greenStrokePaint)
        transformedPoints = GraphicUtils.rotatePoints(transformedPoints, 45f)
        path.updatePath(transformedPoints)
        canvas.drawPath(path, redStrokePaint)
        transformedPoints = GraphicUtils.translatePoints(transformedPoints, 0f, 550f)
        path.updatePath(transformedPoints)
        canvas.drawPath(path, blackStrokePaint)
        canvas.drawPath(path, blueRedMirrorGradientFillPaint)
        */
        /*
        path.updatePath(secondPoints)
        canvas.drawPath(path, blackStrokePaint)
        val thirdPoints = GraphicUtils.rotatePoints(secondPoints, 45f)
        path.updatePath(thirdPoints)
        canvas.drawPath(path, blueRedMirrorGradientFillPaint)
        */
    }

}