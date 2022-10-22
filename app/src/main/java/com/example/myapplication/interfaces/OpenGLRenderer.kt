package com.example.myapplication.interfaces

/**
 * Created by Athenriel on 10/21/2022
 */
interface OpenGLRenderer {
    fun checkGlError(operation: String)
    fun loadShader(type: Int, shaderCode: String): Int
}