package com.example.myapplication.ui.views

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet

/**
 * Created by Athenriel on 9/5/2022
 */
class OpenGLView(context: Context, attrs: AttributeSet) : GLSurfaceView(context, attrs) {

    private var scaleFactor = 0f

    init {
        setEGLContextClientVersion(2)
    }

    override fun setRenderer(renderer: Renderer?) {
        super.setRenderer(renderer)
        renderMode = RENDERMODE_WHEN_DIRTY
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        scaleFactor = 0f
        super.onSizeChanged(w, h, oldw, oldh)
    }

    fun getWidthScaleFactor(): Float {
        if (scaleFactor == 0f) {
            scaleFactor = 180f / width
        }
        return scaleFactor
    }

    fun refresh() {
        requestRender()
    }

}