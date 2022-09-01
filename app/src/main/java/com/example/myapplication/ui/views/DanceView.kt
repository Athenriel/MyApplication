package com.example.myapplication.ui.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.example.myapplication.model.ThreeDCubeListModel
import timber.log.Timber

/**
 * Created by Athenriel on 9/1/2022
 */
class DanceView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private var initialized = false

    private lateinit var linearYellowBrownMirrorGradient: LinearGradient

    private lateinit var yellowBrownGradientPaint: Paint

    private var threeDCubesModel = ThreeDCubeListModel()
    private var lettersThreeDModel = ThreeDCubeListModel()

    fun setThreeDCubesModel(threeDCubes: ThreeDCubeListModel) {
        threeDCubesModel = threeDCubes
    }

    fun setLettersThreeDCubesModel(threeDCubes: ThreeDCubeListModel) {
        lettersThreeDModel = threeDCubes
    }

    private val path = Path()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //Add your drawing code here
        if (!initialized) {
            linearYellowBrownMirrorGradient = LinearGradient(
                width.toFloat() / 2,
                0f,
                width.toFloat(),
                height.toFloat() * 6 / 5,
                Color.argb(255, 255, 191, 116),
                Color.argb(255, 69, 48, 26),
                Shader.TileMode.MIRROR
            )
            yellowBrownGradientPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                style = Paint.Style.FILL //fill only no stroke
                shader = linearYellowBrownMirrorGradient
            }
            initialized = true
        }
        canvas.drawPaint(yellowBrownGradientPaint)
        try {
            path.reset()
            for (cube in threeDCubesModel.getData()) {
                cube.drawCubeOnCanvas(canvas)
            }
            path.reset()
            for (cube in lettersThreeDModel.getData()) {
                cube.drawCubeOnCanvas(canvas)
            }
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

}