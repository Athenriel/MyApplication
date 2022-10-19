package com.example.myapplication.ui.frame

import android.graphics.PointF
import android.opengl.GLES32
import com.example.myapplication.ui.renderer.OpenGLLetterSRenderer
import com.example.myapplication.utils.GraphicUtils
import timber.log.Timber
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.IntBuffer

/**
 * Created by Athenriel on 9/23/2022
 * Does not work and I do not know why
 */
class LetterSFrame(private val renderer: OpenGLLetterSRenderer) {

    private var indexBuffer: IntBuffer? = null
    private var vertexBuffer: FloatBuffer? = null
    private var colorBuffer: FloatBuffer? = null
    private var mProgram = 0
    private var mPositionHandle = 0
    private var mMVPMatrixHandle = 0
    private var mColorHandle = 0
    private var vertexCount = 0 // number of vertices

    companion object {
        private const val VERTEX_SHADER_CODE = "attribute vec3 aVertexPosition;" +
                "attribute vec4 aVertexColor;" +
                "uniform mat4 uMVPMatrix;" +
                "varying vec4 vColor;" +
                "void main() {" +
                "gl_Position = uMVPMatrix * vec4(aVertexPosition, 1.0);" +
                "vColor=aVertexColor;" +
                "}"
        private const val FRAGMENT_SHADER_CODE = "precision mediump float;" +
                "varying vec4 vColor;" +
                "void main() {" +
                "gl_FragColor = vColor;" +
                "}"
        private const val COORDINATES_PER_VERTEX =
            3 // number of coordinates per vertex in this array
        private const val COLOR_PER_VERTEX =
            4 // number of color values per vertex in this array
        private const val VERTEX_STRIDE = COORDINATES_PER_VERTEX * 4 // 4 bytes per vertex
        private const val COLOR_STRIDE = COLOR_PER_VERTEX * 4 // 4 bytes per vertex
    }

    private val firstLetterSBezierPointList = listOf(
        PointF(2f, 0f),
        PointF(3.2f, 0f),
        PointF(4f, 0.8f),
        PointF(2.8f, 1.3f),
        PointF(2f, 1.5f),
        PointF(2f, 2f),
        PointF(3.2f, 2f)
    )

    private val secondLetterSBezierPointList = listOf(
        PointF(2f, 0.2f),
        PointF(2.2f, 0.2f),
        PointF(3.6f, 0.4f),
        PointF(2.8f, 1f),
        PointF(1.5f, 1.5f),
        PointF(1.6f, 2.2f),
        PointF(3.2f, 2.2f)
    )

    private val fullVerticesArray = floatArrayOf(
        //first s front
        2f, 0f, 0.3f, //0
        2.3463f, 0.0229f, 0.3f, //1
        2.6592f, 0.0872f, 0.3f, //2
        2.9287f, 0.1863f, 0.3f, //3
        3.1456f, 0.3136f, 0.3f, //4
        3.3f, 0.4625f, 0.3f, //5
        3.3823f, 0.6264f, 0.3f, //6
        3.3832f, 0.7987f, 0.3f, //7
        3.2928f, 0.9728f, 0.3f, //8
        3.1016f, 1.142f, 0.3f, //9
        2.8f, 1.3f, 0.3f, //10
        2.5843f, 1.3681f, 0.3f, //11
        2.4192f, 1.4496f, 0.3f, //12
        2.3068f, 1.5394f, 0.3f, //13
        2.2496f, 1.6328f, 0.3f, //14
        2.25f, 1.725f, 0.3f, //15
        2.3104f, 1.8111f, 0.3f, //16
        2.4332f, 1.8865f, 0.3f, //17
        2.6208f, 1.9464f, 0.3f, //18
        2.8755f, 1.9857f, 0.3f, //19
        3.2f, 2f, 0.3f, //20
        //second s front
        2.0f, 0.2f, 0.3f, //21
        2.0925f, 0.2061f, 0.3f, //22
        2.2368f, 0.2256f, 0.3f, //23
        2.4121f, 0.2594f, 0.3f, //24
        2.5983f, 0.3088f, 0.3f, //25
        2.7749f, 0.375f, 0.3f, //26
        2.9215f, 0.4592f, 0.3f, //27
        3.0177f, 0.5626f, 0.3f, //28
        3.0432f, 0.6864f, 0.3f, //29
        2.9773f, 0.8317f, 0.3f, //30
        2.8f, 1.0f, 0.3f, //31
        2.4520f, 1.1550f, 0.3f, //32
        2.1888f, 1.3168f, 0.3f, //33
        2.0107f, 1.4797f, 0.3f, //34
        1.9184f, 1.6384f, 0.3f, //35
        1.9125f, 1.7875f, 0.3f, //36
        1.9935f, 1.9216f, 0.3f, //37
        2.1623f, 2.0353f, 0.3f, //38
        2.4192f, 2.1232f, 0.3f, //39
        2.7648f, 2.1799f, 0.3f, //40
        3.2f, 2.2f, 0.3f, //41
        //first s back
        2f, 0f, 0.3f, //42
        2.3463f, 0.0229f, -0.3f, //43
        2.6592f, 0.0872f, -0.3f, //44
        2.9287f, 0.1863f, -0.3f, //45
        3.1456f, 0.3136f, -0.3f, //46
        3.3f, 0.4625f, -0.3f, //47
        3.3823f, 0.6264f, -0.3f, //48
        3.3832f, 0.7987f, -0.3f, //49
        3.2928f, 0.9728f, -0.3f, //50
        3.1016f, 1.142f, -0.3f, //51
        2.8f, 1.3f, -0.3f, //52
        2.5843f, 1.3681f, -0.3f, //53
        2.4192f, 1.4496f, -0.3f, //54
        2.3068f, 1.5394f, -0.3f, //55
        2.2496f, 1.6328f, -0.3f, //56
        2.25f, 1.725f, -0.3f, //57
        2.3104f, 1.8111f, -0.3f, //58
        2.4332f, 1.8865f, -0.3f, //59
        2.6208f, 1.9464f, -0.3f, //60
        2.8755f, 1.9857f, -0.3f, //61
        3.2f, 2f, -0.3f, //62
        //second s back
        2.0f, 0.2f, -0.3f, //63
        2.0925f, 0.2061f, -0.3f, //64
        2.2368f, 0.2256f, -0.3f, //65
        2.4121f, 0.2594f, -0.3f, //66
        2.5983f, 0.3088f, -0.3f, //67
        2.7749f, 0.375f, -0.3f, //68
        2.9215f, 0.4592f, -0.3f, //69
        3.0177f, 0.5626f, -0.3f, //70
        3.0432f, 0.6864f, -0.3f, //71
        2.9773f, 0.8317f, -0.3f, //72
        2.8f, 1.0f, -0.3f, //73
        2.4520f, 1.1550f, -0.3f, //74
        2.1888f, 1.3168f, -0.3f, //75
        2.0107f, 1.4797f, -0.3f, //76
        1.9184f, 1.6384f, -0.3f, //77
        1.9125f, 1.7875f, -0.3f, //78
        1.9935f, 1.9216f, -0.3f, //79
        2.1623f, 2.0353f, -0.3f, //80
        2.4192f, 2.1232f, -0.3f, //81
        2.7648f, 2.1799f, -0.3f, //82
        3.2f, 2.2f, -0.3f //83
    )

    private val fullIndexArray = intArrayOf(
        //front
        0, 1, 22,
        22, 21, 0,
        1, 2, 23,
        23, 22, 1,
        2, 3, 24,
        24, 23, 2,
        3, 4, 25,
        25, 24, 3,
        4, 5, 26,
        26, 25, 4,
        5, 6, 27,
        27, 26, 5,
        6, 7, 28,
        28, 27, 6,
        7, 8, 29,
        29, 28, 7,
        8, 9, 30,
        30, 29, 8,
        9, 10, 31,
        31, 30, 9,
        10, 11, 32,
        32, 31, 10,
        11, 12, 33,
        33, 32, 11,
        12, 13, 34,
        34, 33, 12,
        13, 14, 35,
        35, 34, 13,
        14, 15, 36,
        36, 35, 14,
        15, 16, 37,
        37, 36, 15,
        16, 17, 38,
        38, 37, 16,
        17, 18, 39,
        39, 38, 17,
        18, 19, 40,
        40, 39, 18,
        19, 20, 41,
        41, 40, 19,
        //back
        42, 43, 64,
        64, 63, 42,
        43, 44, 65,
        65, 64, 43,
        44, 45, 66,
        66, 65, 44,
        45, 46, 67,
        67, 66, 45,
        46, 47, 68,
        68, 67, 46,
        47, 48, 69,
        69, 68, 47,
        48, 49, 70,
        70, 69, 48,
        49, 50, 71,
        71, 70, 49,
        50, 51, 72,
        72, 71, 50,
        51, 52, 73,
        73, 72, 51,
        52, 53, 74,
        74, 73, 52,
        53, 54, 75,
        75, 74, 53,
        54, 55, 76,
        76, 75, 54,
        55, 56, 77,
        77, 76, 55,
        56, 57, 78,
        78, 77, 56,
        57, 58, 79,
        79, 78, 57,
        58, 59, 80,
        80, 79, 58,
        59, 60, 81,
        81, 80, 59,
        60, 61, 82,
        82, 81, 60,
        61, 62, 83,
        83, 82, 61,
        //edges
        0, 21, 63,
        63, 42, 0,
        20, 41, 83,
        83, 62, 20,
        //first s side
        0, 1, 43,
        43, 42, 0,
        1, 2, 44,
        44, 43, 1,
        2, 3, 45,
        45, 44, 2,
        3, 4, 46,
        46, 45, 3,
        4, 5, 47,
        47, 46, 4,
        5, 56, 48,
        48, 47, 5,
        6, 7, 49,
        49, 48, 6,
        7, 8, 50,
        50, 49, 7,
        8, 9, 51,
        51, 50, 8,
        9, 10, 52,
        52, 51, 9,
        10, 11, 53,
        53, 52, 10,
        11, 12, 54,
        54, 53, 11,
        12, 13, 55,
        55, 54, 12,
        13, 14, 56,
        56, 55, 13,
        14, 15, 57,
        57, 56, 14,
        15, 16, 58,
        58, 57, 15,
        16, 17, 59,
        59, 58, 16,
        17, 18, 60,
        60, 59, 17,
        18, 19, 61,
        61, 60, 18,
        19, 20, 62,
        62, 61, 19,
        //second s side
        21, 22, 64,
        64, 63, 21,
        22, 23, 65,
        65, 64, 22,
        23, 24, 66,
        66, 65, 23,
        24, 25, 67,
        67, 66, 24,
        25, 26, 68,
        68, 67, 25,
        26, 27, 69,
        69, 68, 26,
        27, 28, 70,
        70, 69, 27,
        28, 29, 71,
        71, 70, 28,
        29, 30, 72,
        72, 71, 29,
        30, 31, 73,
        73, 72, 30,
        31, 32, 74,
        74, 73, 31,
        32, 33, 75,
        75, 74, 32,
        33, 34, 76,
        76, 75, 33,
        34, 35, 77,
        77, 76, 34,
        35, 36, 78,
        78, 77, 35,
        36, 37, 79,
        79, 78, 36,
        37, 38, 80,
        80, 79, 37,
        38, 39, 81,
        81, 80, 38,
        39, 40, 82,
        82, 81, 39,
        40, 41, 83,
        83, 82, 40
    )

    private val colorArray = floatArrayOf(
        0f, 0f, 1f, 1f, //0
        0f, 0f, 1f, 1f, //1
        0f, 0f, 1f, 1f, //2
        0f, 0f, 1f, 1f, //3
        0f, 0f, 1f, 1f, //4
        0f, 0f, 1f, 1f, //5
        0f, 0f, 1f, 1f, //6
        0f, 0f, 1f, 1f, //7
        0f, 0f, 1f, 1f, //8
        0f, 0f, 1f, 1f, //9
        0f, 0f, 1f, 1f, //10
        0f, 0f, 1f, 1f, //11
        0f, 0f, 1f, 1f, //12
        0f, 0f, 1f, 1f, //13
        0f, 0f, 1f, 1f, //14
        0f, 0f, 1f, 1f, //15
        0f, 0f, 1f, 1f, //16
        0f, 0f, 1f, 1f, //17
        0f, 0f, 1f, 1f, //18
        0f, 0f, 1f, 1f, //19
        0f, 0f, 1f, 1f, //20
        0f, 0f, 1f, 1f, //21
        0f, 0f, 1f, 1f, //22
        0f, 0f, 1f, 1f, //23
        0f, 0f, 1f, 1f, //24
        0f, 0f, 1f, 1f, //25
        0f, 0f, 1f, 1f, //26
        0f, 0f, 1f, 1f, //27
        0f, 0f, 1f, 1f, //28
        0f, 0f, 1f, 1f, //29
        0f, 0f, 1f, 1f, //30
        0f, 0f, 1f, 1f, //31
        0f, 0f, 1f, 1f, //32
        0f, 0f, 1f, 1f, //33
        0f, 0f, 1f, 1f, //34
        0f, 0f, 1f, 1f, //35
        0f, 0f, 1f, 1f, //36
        0f, 0f, 1f, 1f, //37
        0f, 0f, 1f, 1f, //38
        0f, 0f, 1f, 1f, //39
        0f, 0f, 1f, 1f, //40
        0f, 0f, 1f, 1f, //41
        0f, 0f, 1f, 1f, //42
        0f, 0f, 1f, 1f, //43
        0f, 0f, 1f, 1f, //44
        0f, 0f, 1f, 1f, //45
        0f, 0f, 1f, 1f, //46
        0f, 0f, 1f, 1f, //47
        0f, 0f, 1f, 1f, //48
        0f, 0f, 1f, 1f, //49
        0f, 0f, 1f, 1f, //50
        0f, 0f, 1f, 1f, //51
        0f, 0f, 1f, 1f, //52
        0f, 0f, 1f, 1f, //53
        0f, 0f, 1f, 1f, //54
        0f, 0f, 1f, 1f, //55
        0f, 0f, 1f, 1f, //56
        0f, 0f, 1f, 1f, //57
        0f, 0f, 1f, 1f, //58
        0f, 0f, 1f, 1f, //59
        0f, 0f, 1f, 1f, //60
        0f, 0f, 1f, 1f, //61
        0f, 0f, 1f, 1f, //62
        0f, 0f, 1f, 1f, //63
        0f, 0f, 1f, 1f, //64
        0f, 0f, 1f, 1f, //65
        0f, 0f, 1f, 1f, //66
        0f, 0f, 1f, 1f, //67
        0f, 0f, 1f, 1f, //68
        0f, 0f, 1f, 1f, //69
        0f, 0f, 1f, 1f, //70
        0f, 0f, 1f, 1f, //71
        0f, 0f, 1f, 1f, //72
        0f, 0f, 1f, 1f, //73
        0f, 0f, 1f, 1f, //74
        0f, 0f, 1f, 1f, //75
        0f, 0f, 1f, 1f, //76
        0f, 0f, 1f, 1f, //77
        0f, 0f, 1f, 1f, //78
        0f, 0f, 1f, 1f, //79
        0f, 0f, 1f, 1f, //80
        0f, 0f, 1f, 1f, //81
        0f, 0f, 1f, 1f, //82
        0f, 0f, 1f, 1f //83
    )

    private var indexesSize = 0

    init {
        //This is the part that does not work
        /*
        val frontVerticesList = arrayListOf<Float>()
        val frontIndexesList = arrayListOf<Int>()
        val vertices1 = GraphicUtils.getBezierCurveVertices(0.3f, firstLetterSBezierPointList.subList(0, 4))
        vertices1.removeLast()
        Timber.d("dcheck vertices1 %s", vertices1)
        val vertices2 = GraphicUtils.getBezierCurveVertices(0.3f, firstLetterSBezierPointList.subList(3, firstLetterSBezierPointList.size))
        Timber.d("dcheck vertices2 %s", vertices2)
        val vertices3 = GraphicUtils.getBezierCurveVertices(0.3f, secondLetterSBezierPointList.subList(0, 4))
        vertices3.removeLast()
        Timber.d("dcheck vertices3 %s", vertices3)
        val vertices4 = GraphicUtils.getBezierCurveVertices(0.3f, secondLetterSBezierPointList.subList(3, secondLetterSBezierPointList.size))
        Timber.d("dcheck vertices4 %s", vertices4)
        if (vertices1.size == vertices3.size) {
            for (i in vertices1.indices) {
                frontVerticesList.add(vertices1[i].x)
                frontVerticesList.add(vertices1[i].y)
                frontVerticesList.add(vertices1[i].z)
                frontVerticesList.add(vertices3[i].x)
                frontVerticesList.add(vertices3[i].y)
                frontVerticesList.add(vertices3[i].z)
            }
        }
        if (vertices2.size == vertices4.size) {
            for (i in vertices1.indices) {
                frontVerticesList.add(vertices2[i].x)
                frontVerticesList.add(vertices2[i].y)
                frontVerticesList.add(vertices2[i].z)
                frontVerticesList.add(vertices4[i].x)
                frontVerticesList.add(vertices4[i].y)
                frontVerticesList.add(vertices4[i].z)
            }
        }
        Timber.d("dcheck size %s frontVerticesList %s", frontVerticesList.size, frontVerticesList)
        for (i in 0 until frontVerticesList.size step 2) {
            if (i+3 < frontVerticesList.size) {
                frontIndexesList.add(i)
                frontIndexesList.add(i+2)
                frontIndexesList.add(i+1)
                frontIndexesList.add(i+1)
                frontIndexesList.add(i+2)
                frontIndexesList.add(i+3)
            }
        }
        Timber.d("dcheck size %s frontIndexesList %s", frontIndexesList.size, frontIndexesList)

        val backVerticesList = arrayListOf<Float>()
        val backIndexesList = arrayListOf<Int>()
        if (vertices1.size == vertices3.size) {
            for (i in vertices1.indices) {
                backVerticesList.add(vertices1[i].x)
                backVerticesList.add(vertices1[i].y)
                backVerticesList.add(-vertices1[i].z)
                backVerticesList.add(vertices3[i].x)
                backVerticesList.add(vertices3[i].y)
                backVerticesList.add(-vertices3[i].z)
            }
        }
        if (vertices2.size == vertices4.size) {
            for (i in vertices1.indices) {
                backVerticesList.add(vertices1[i].x)
                backVerticesList.add(vertices1[i].y)
                backVerticesList.add(-vertices1[i].z)
                backVerticesList.add(vertices3[i].x)
                backVerticesList.add(vertices3[i].y)
                backVerticesList.add(-vertices3[i].z)
            }
        }
        Timber.d("dcheck size %s backVerticesList %s", backVerticesList.size, backVerticesList)
        for (i in frontIndexesList) {
            backIndexesList.add(i + frontVerticesList.size)
        }
        Timber.d("dcheck size %s backIndexesList %s", backIndexesList.size, backIndexesList)

        val colorList = arrayListOf<Float>()
        for (i in 0 until frontVerticesList.size) {
            colorList.add(0f)
            colorList.add(0f)
            colorList.add(1f)
            colorList.add(1f)
        }
        for (i in 0 until backVerticesList.size) {
            colorList.add(0f)
            colorList.add(1f)
            colorList.add(0f)
            colorList.add(1f)
        }
        Timber.d("dcheck size %s colorList %s", colorList.size, colorList)

        val fullVerticesList = arrayListOf<Float>()
        fullVerticesList.addAll(frontVerticesList)
        fullVerticesList.addAll(backVerticesList)
        val fullIndexesList = arrayListOf<Int>()
        fullIndexesList.addAll(frontIndexesList)
        fullIndexesList.addAll(backIndexesList)

        val fullIndexArray = fullIndexesList.toIntArray()
        val colorArray = colorList.toFloatArray()
        val fullVerticesArray = FloatArray(fullVerticesList.size)
        var verticesIndex = 0
        for (vertex in fullVerticesList) {
            fullVerticesArray[verticesIndex++] = vertex
        }
        */

        indexesSize = fullIndexArray.size

        indexBuffer = IntBuffer.allocate(fullIndexArray.size)
        indexBuffer?.put(fullIndexArray)
        indexBuffer?.position(0)

        // initialize vertex byte buffer for shape coordinates
        val bb: ByteBuffer =
            ByteBuffer.allocateDirect(fullVerticesArray.size * 4) // (# of coordinate values * 4 bytes per float)
        bb.order(ByteOrder.nativeOrder())
        vertexBuffer = bb.asFloatBuffer()
        vertexBuffer?.put(fullVerticesArray)
        vertexBuffer?.position(0)
        vertexCount = fullVerticesArray.size / COORDINATES_PER_VERTEX

        //initialize color byte buffer
        val cb: ByteBuffer =
            ByteBuffer.allocateDirect(colorArray.size * 4) // (# of coordinate values * 4 bytes per float)
        cb.order(ByteOrder.nativeOrder())
        colorBuffer = cb.asFloatBuffer()
        colorBuffer?.put(colorArray)
        colorBuffer?.position(0)

        // prepare shaders and OpenGL program
        val vertexShader = renderer.loadShader(
            GLES32.GL_VERTEX_SHADER,
            VERTEX_SHADER_CODE
        )
        val fragmentShader = renderer.loadShader(
            GLES32.GL_FRAGMENT_SHADER,
            FRAGMENT_SHADER_CODE
        )
        mProgram = GLES32.glCreateProgram() // create empty OpenGL Program
        GLES32.glAttachShader(mProgram, vertexShader) // add the vertex shader to program
        GLES32.glAttachShader(mProgram, fragmentShader) // add the fragment shader to program
        GLES32.glLinkProgram(mProgram) // link the  OpenGL program to create an executable
        GLES32.glUseProgram(mProgram) // Add program to OpenGL environment

        // get handle to vertex shader's aVertexColor member
        mColorHandle = GLES32.glGetAttribLocation(mProgram, "aVertexColor")
        // get handle to vertex shader's aVertexPosition member
        mPositionHandle = GLES32.glGetAttribLocation(mProgram, "aVertexPosition")
        // Enable a handle to the triangle vertices
        GLES32.glEnableVertexAttribArray(mColorHandle)
        // Enable a handle to the triangle vertices
        GLES32.glEnableVertexAttribArray(mPositionHandle)
        // get handle to shape's transformation matrix
        mMVPMatrixHandle = GLES32.glGetUniformLocation(mProgram, "uMVPMatrix")
        renderer.checkGlError("glGetUniformLocation")
    }

    fun draw(mvpMatrix: FloatArray) {
        // Apply the projection and view transformation
        GLES32.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0)
        renderer.checkGlError("glUniformMatrix4fv")
        //set the attribute of the vertex to point to the vertex buffer
        GLES32.glVertexAttribPointer(
            mPositionHandle, COORDINATES_PER_VERTEX,
            GLES32.GL_FLOAT, false, VERTEX_STRIDE, vertexBuffer
        )
        GLES32.glVertexAttribPointer(
            mColorHandle, COLOR_PER_VERTEX,
            GLES32.GL_FLOAT, false, COLOR_STRIDE, colorBuffer
        )
        // Draw the triangle
        GLES32.glDrawElements(
            GLES32.GL_TRIANGLES,
            indexesSize,
            GLES32.GL_UNSIGNED_INT,
            indexBuffer
        )
    }

}