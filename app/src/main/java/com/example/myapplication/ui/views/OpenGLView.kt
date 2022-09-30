package com.example.myapplication.ui.views

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet

/**
 * Created by Athenriel on 9/5/2022
 */
class OpenGLView(context: Context, attrs: AttributeSet) : GLSurfaceView(context, attrs) {

    init {
        setEGLContextClientVersion(2)
    }

    override fun setRenderer(renderer: Renderer?) {
        super.setRenderer(renderer)
        renderMode = RENDERMODE_WHEN_DIRTY
    }

    fun refresh() {
        requestRender()
    }

}