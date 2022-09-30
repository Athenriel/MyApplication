package com.example.myapplication.ui.frame

import android.opengl.GLES32
import com.example.myapplication.ui.renderer.OpenGLImperialRenderer
import com.example.myapplication.utils.GraphicUtils
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.IntBuffer

/**
 * Created by Athenriel on 9/29/2022
 */
class ImperialFrame(private val renderer: OpenGLImperialRenderer) {

    private var indexFirstIBuffer: IntBuffer? = null
    private var vertexFirstIBuffer: FloatBuffer? = null
    private var colorFirstIBuffer: FloatBuffer? = null

    private var indexMBuffer: IntBuffer? = null
    private var vertexMBuffer: FloatBuffer? = null
    private var colorMBuffer: FloatBuffer? = null

    private var indexPBuffer: IntBuffer? = null
    private var vertexPBuffer: FloatBuffer? = null
    private var colorPBuffer: FloatBuffer? = null

    private var indexEBuffer: IntBuffer? = null
    private var vertexEBuffer: FloatBuffer? = null
    private var colorEBuffer: FloatBuffer? = null

    private var indexRBuffer: IntBuffer? = null
    private var vertexRBuffer: FloatBuffer? = null
    private var colorRBuffer: FloatBuffer? = null

    private var indexSecondIBuffer: IntBuffer? = null
    private var vertexSecondIBuffer: FloatBuffer? = null
    private var colorSecondIBuffer: FloatBuffer? = null

    private var indexABuffer: IntBuffer? = null
    private var vertexABuffer: FloatBuffer? = null
    private var colorABuffer: FloatBuffer? = null

    private var indexLBuffer: IntBuffer? = null
    private var vertexLBuffer: FloatBuffer? = null
    private var colorLBuffer: FloatBuffer? = null

    private var mProgram = 0
    private var mPositionHandle = 0
    private var mMVPMatrixHandle = 0
    private var mColorHandle = 0

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

    private val letterFirstIVertices = floatArrayOf(
        //front
        -1f, 3f, 1f, //0
        1f, 3f, 1f, //1
        1f, -3f, 1f, //2
        -1f, -3f, 1f, //3
        //back
        -1f, 3f, -1f, //4
        1f, 3f, -1f, //5
        1f, -3f, -1f, //6
        -1f, -3f, -1f //7
    )

    private val letterSecondIVertices = floatArrayOf(
        //front
        -1f, 3f, 1f, //0
        1f, 3f, 1f, //1
        1f, -3f, 1f, //2
        -1f, -3f, 1f, //3
        //back
        -1f, 3f, -1f, //4
        1f, 3f, -1f, //5
        1f, -3f, -1f, //6
        -1f, -3f, -1f //7
    )

    private val letterIIndices = intArrayOf(
        //front
        0, 1, 2,
        2, 3, 0,
        //back
        4, 5, 6,
        6, 7, 4,
        //top
        0, 1, 5,
        5, 4, 0,
        //bottom
        3, 2, 6,
        6, 7, 3,
        //outer side 2
        1, 2, 6,
        6, 5, 1,
        //outer side 1
        0, 3, 7,
        7, 4, 0
    )

    private val letterIColor = floatArrayOf(
        //front
        1f, 1f, 1f, 1f, //0
        1f, 1f, 1f, 1f, //1
        1f, 1f, 1f, 1f, //2
        1f, 1f, 1f, 1f, //3
        //back
        1f, 1f, 1f, 1f, //4
        1f, 1f, 1f, 1f, //5
        1f, 1f, 1f, 1f, //6
        1f, 1f, 1f, 1f //7
    )

    private val letterMVertices = floatArrayOf(
        //front
        -2f, 3f, 1f, //0
        -1f, 3f, 1f, //1
        0f, -1f, 1f, //2
        1f, 3f, 1f, //3
        2f, 3f, 1f, //4
        3.5f, -3f, 1f, //5
        2.5f, -3f, 1f, //6
        1.5f, 1f, 1f, //7
        0.5f, -3f, 1f, //8
        -0.5f, -3f, 1f, //9
        -1.5f, 1f, 1f, //10
        -2.5f, -3f, 1f, //11
        -3.5f, -3f, 1f, //12
        //back
        -2f, 3f, -1f, //13
        -1f, 3f, -1f, //14
        0f, -1f, -1f, //15
        1f, 3f, -1f, //16
        2f, 3f, -1f, //17
        3.5f, -3f, -1f, //18
        2.5f, -3f, -1f, //19
        1.5f, 1f, -1f, //20
        0.5f, -3f, -1f, //21
        -0.5f, -3f, -1f, //22
        -1.5f, 1f, -1f, //23
        -2.5f, -3f, -1f, //24
        -3.5f, -3f, -1f //25
    )

    private val letterMIndices = intArrayOf(
        //front
        0, 1, 2,
        2, 3, 4,
        4, 5, 7,
        5, 6, 7,
        2, 4, 7,
        2, 7, 8,
        8, 9, 2,
        9, 10, 2,
        2, 10, 0,
        0, 10, 12,
        10, 11, 12,
        //back
        13, 14, 15,
        15, 16, 17,
        17, 18, 20,
        18, 19, 20,
        15, 17, 20,
        15, 20, 21,
        21, 22, 15,
        22, 23, 15,
        15, 23, 13,
        13, 23, 25,
        23, 24, 25,
        //top 1
        0, 1, 14,
        14, 13, 0,
        //inner side 1
        1, 2, 15,
        15, 14, 1,
        //inner side 2
        2, 3, 16,
        16, 15, 2,
        //top 2
        3, 4, 17,
        17, 16, 3,
        //outer side 2
        4, 5, 18,
        18, 17, 4,
        //bottom 3
        5, 6, 19,
        19, 18, 5,
        //lower side 4
        6, 7, 20,
        20, 19, 6,
        //lower side 3
        7, 8, 21,
        21, 20, 7,
        //bottom 2
        8, 9, 22,
        22, 21, 8,
        //lower side 2
        9, 10, 23,
        23, 22, 9,
        //lower side 1
        10, 11, 24,
        24, 23, 10,
        //bottom 1
        11, 12, 25,
        25, 24, 11,
        //outer side 1
        12, 0, 13,
        13, 25, 12
    )

    private val letterMColor = floatArrayOf(
        //front
        1f, 1f, 1f, 1f, //0
        1f, 1f, 1f, 1f, //1
        1f, 1f, 1f, 1f, //2
        1f, 1f, 1f, 1f, //3
        1f, 1f, 1f, 1f, //4
        1f, 1f, 1f, 1f, //5
        1f, 1f, 1f, 1f, //6
        1f, 1f, 1f, 1f, //7
        1f, 1f, 1f, 1f, //8
        1f, 1f, 1f, 1f, //9
        1f, 1f, 1f, 1f, //10
        1f, 1f, 1f, 1f, //11
        1f, 1f, 1f, 1f, //12
        //back
        1f, 1f, 1f, 1f, //13
        1f, 1f, 1f, 1f, //14
        1f, 1f, 1f, 1f, //15
        1f, 1f, 1f, 1f, //16
        1f, 1f, 1f, 1f, //17
        1f, 1f, 1f, 1f, //18
        1f, 1f, 1f, 1f, //19
        1f, 1f, 1f, 1f, //20
        1f, 1f, 1f, 1f, //21
        1f, 1f, 1f, 1f, //22
        1f, 1f, 1f, 1f, //23
        1f, 1f, 1f, 1f, //24
        1f, 1f, 1f, 1f //25
    )

    private val letterPVertices = floatArrayOf(
        //front
        -2f, 3f, 1f, //0
        1f, 3f, 1f, //1
        1.2936f, 2.946699f, 1f, //2
        1.5728f, 2.849599f, 1f, //3
        1.835199f, 2.7129f, 1f, //4
        2.0784f, 2.5408f, 1f, //5
        2.3f, 2.3375f, 1f, //6
        2.497599f, 2.1072f, 1f, //7
        2.668799f, 1.8541f, 1f, //8
        2.8112f, 1.5824f, 1f, //9
        2.9224f, 1.2963f, 1f, //10
        3f, 1f, 1f, //11
        3f, 1f, 1f, //12
        2.922399f, 0.703699f, 1f, //13
        2.8112f, 0.4176f, 1f, //14
        2.6688f, 0.1459f, 1f, //15
        2.497599f, -0.107199f, 1f, //16
        2.3f, -0.337499f, 1f, //17
        2.0784f, -0.5408f, 1f, //18
        1.8352f, -0.712899f, 1f, //19
        1.572799f, -0.8496f, 1f, //20
        1.2936f, -0.9467f, 1f, //21
        1f, -1f, 1f, //22
        0f, -1f, 1f, //23
        0f, 2f, 1f, //24
        1f, 2f, 1f, //25
        1.158949f, 1.966599f, 1f, //26
        1.3056f, 1.9248f, 1f, //27
        1.439649f, 1.8722f, 1f, //28
        1.5608f, 1.8064f, 1f, //29
        1.668749f, 1.725f, 1f, //30
        1.763199f, 1.6256f, 1f, //31
        1.84385f, 1.5058f, 1f, //32
        1.9104f, 1.3632f, 1f, //33
        1.962549f, 1.1954f, 1f, //34
        2f, 1f, 1f, //35
        1.962549f, 0.8046f, 1f, //36
        1.9104f, 0.6368f, 1f, //37
        1.84385f, 0.4942f, 1f, //38
        1.763199f, 0.3744f, 1f, //39
        1.668749f, 0.275f, 1f, //40
        1.5608f, 0.1936f, 1f, //41
        1.439649f, 0.1278f, 1f, //42
        1.3056f, 0.0752f, 1f, //43
        1.158949f, 0.033401f, 1f, //44
        1f, 0f, 1f, //45
        0f, 0f, 1f, //46
        0f, -3f, 1f, //47
        -2f, -3f, 1f, //48
        //back
        -2f, 3f, -1f, //49
        1f, 3f, -1f, //50
        1.2936f, 2.946699f, -1f, //51
        1.5728f, 2.849599f, -1f, //52
        1.835199f, 2.7129f, -1f, //53
        2.0784f, 2.5408f, -1f, //54
        2.3f, 2.3375f, -1f, //55
        2.497599f, 2.1072f, -1f, //56
        2.668799f, 1.8541f, -1f, //57
        2.8112f, 1.5824f, -1f, //58
        2.9224f, 1.2963f, -1f, //59
        3f, 1f, -1f, //60
        3f, 1f, -1f, //61
        2.922399f, 0.703699f, -1f, //62
        2.8112f, 0.4176f, -1f, //63
        2.6688f, 0.1459f, -1f, //64
        2.497599f, -0.107199f, -1f, //65
        2.3f, -0.337499f, -1f, //66
        2.0784f, -0.5408f, -1f, //67
        1.8352f, -0.712899f, -1f, //68
        1.572799f, -0.8496f, -1f, //69
        1.2936f, -0.9467f, -1f, //70
        1f, -1f, -1f, //71
        0f, -1f, -1f, //72
        0f, 2f, -1f, //73
        1f, 2f, -1f, //74
        1.158949f, 1.966599f, -1f, //75
        1.3056f, 1.9248f, -1f, //76
        1.439649f, 1.8722f, -1f, //77
        1.5608f, 1.8064f, -1f, //78
        1.668749f, 1.725f, -1f, //79
        1.763199f, 1.6256f, -1f, //80
        1.84385f, 1.5058f, -1f, //81
        1.9104f, 1.3632f, -1f, //82
        1.962549f, 1.1954f, -1f, //83
        2f, 1f, -1f, //84
        1.962549f, 0.8046f, -1f, //85
        1.9104f, 0.6368f, -1f, //86
        1.84385f, 0.4942f, -1f, //87
        1.763199f, 0.3744f, -1f, //88
        1.668749f, 0.275f, -1f, //89
        1.5608f, 0.1936f, -1f, //90
        1.439649f, 0.1278f, -1f, //91
        1.3056f, 0.0752f, -1f, //92
        1.158949f, 0.033401f, -1f, //93
        1f, 0f, -1f, //94
        0f, 0f, -1f, //95
        0f, -3f, -1f, //96
        -2f, -3f, -1f //97
    )

    private val letterPIndices = intArrayOf(
        //front
        0, 24, 1,
        1, 24, 25,
        25, 1, 2,
        2, 26, 25,
        2, 3, 26,
        26, 27, 3,
        3, 4, 27,
        27, 28, 4,
        4, 5, 28,
        28, 29, 5,
        5, 6, 29,
        29, 30, 6,
        6, 7, 30,
        30, 31, 7,
        7, 8, 31,
        31, 32, 8,
        8, 9, 32,
        32, 33, 9,
        9, 10, 33,
        33, 34, 10,
        10, 11, 34,
        34, 35, 11,
        11, 12, 35,
        35, 36, 12,
        12, 13, 36,
        36, 37, 13,
        13, 14, 37,
        37, 38, 14,
        14, 15, 38,
        38, 39, 15,
        15, 16, 39,
        39, 40, 16,
        16, 17, 40,
        40, 41, 17,
        17, 18, 41,
        41, 42, 18,
        18, 19, 42,
        42, 43, 19,
        19, 20, 43,
        43, 44, 20,
        20, 21, 44,
        44, 45, 21,
        21, 22, 45,
        45, 46, 22,
        22, 23, 46,
        23, 47, 48,
        48, 23, 0,
        0, 24, 23,
        //back
        49, 73, 50,
        50, 73, 74,
        74, 50, 51,
        51, 75, 74,
        51, 52, 75,
        75, 76, 52,
        52, 53, 76,
        76, 77, 53,
        53, 54, 77,
        77, 78, 54,
        54, 55, 78,
        78, 79, 55,
        55, 56, 79,
        79, 80, 56,
        56, 57, 80,
        80, 81, 57,
        57, 58, 81,
        81, 82, 58,
        58, 59, 82,
        82, 83, 59,
        59, 60, 83,
        83, 84, 60,
        60, 61, 84,
        84, 85, 61,
        61, 62, 85,
        85, 86, 62,
        62, 63, 86,
        86, 87, 63,
        63, 64, 87,
        87, 88, 64,
        64, 65, 88,
        88, 89, 65,
        65, 66, 89,
        89, 90, 66,
        66, 67, 90,
        90, 91, 67,
        67, 68, 91,
        91, 92, 68,
        68, 69, 92,
        92, 93, 69,
        69, 70, 93,
        93, 94, 70,
        70, 71, 94,
        94, 95, 71,
        71, 72, 95,
        72, 96, 97,
        97, 72, 49,
        49, 73, 72,
        //outer side
        0, 1, 50,
        50, 49, 0,
        1, 2, 51,
        51, 50, 1,
        2, 3, 52,
        52, 51, 2,
        3, 4, 53,
        53, 52, 3,
        4, 5, 54,
        54, 53, 4,
        5, 6, 55,
        55, 54, 5,
        6, 7, 56,
        56, 55, 6,
        7, 8, 57,
        57, 56, 7,
        8, 9, 58,
        58, 57, 8,
        9, 10, 59,
        59, 58, 9,
        10, 11, 60,
        60, 59, 10,
        11, 12, 61,
        61, 60, 11,
        12, 13, 62,
        62, 61, 12,
        13, 14, 63,
        63, 62, 13,
        14, 15, 64,
        64, 63, 14,
        15, 16, 65,
        65, 64, 15,
        16, 17, 66,
        66, 65, 16,
        17, 18, 67,
        67, 66, 17,
        18, 19, 68,
        68, 67, 18,
        19, 20, 69,
        69, 68, 19,
        20, 21, 70,
        70, 69, 20,
        21, 22, 71,
        71, 70, 21,
        22, 23, 72,
        72, 71, 22,
        23, 47, 96,
        96, 72, 23,
        47, 48, 97,
        97, 96, 47,
        48, 0, 49,
        49, 97, 48,
        //inner side
        24, 25, 74,
        74, 73, 24,
        25, 26, 75,
        75, 74, 25,
        26, 27, 76,
        76, 75, 26,
        27, 28, 77,
        77, 76, 27,
        28, 29, 78,
        78, 77, 28,
        29, 30, 79,
        79, 78, 29,
        30, 31, 80,
        80, 79, 30,
        31, 32, 81,
        81, 80, 31,
        32, 33, 82,
        82, 81, 32,
        33, 34, 83,
        83, 82, 33,
        34, 35, 84,
        84, 83, 34,
        35, 36, 85,
        85, 84, 35,
        36, 37, 86,
        86, 85, 36,
        37, 38, 87,
        87, 86, 37,
        38, 39, 88,
        88, 87, 38,
        39, 40, 89,
        89, 88, 39,
        40, 41, 90,
        90, 89, 40,
        41, 42, 91,
        91, 90, 41,
        42, 43, 92,
        92, 91, 42,
        43, 44, 93,
        93, 92, 43,
        44, 45, 94,
        94, 93, 44,
        45, 46, 95,
        95, 94, 45,
        46, 24, 73,
        73, 95, 46
    )

    private val letterPColor = floatArrayOf(
        //front
        1f, 1f, 1f, 1f, //0
        1f, 1f, 1f, 1f, //1
        1f, 1f, 1f, 1f, //2
        1f, 1f, 1f, 1f, //3
        1f, 1f, 1f, 1f, //4
        1f, 1f, 1f, 1f, //5
        1f, 1f, 1f, 1f, //6
        1f, 1f, 1f, 1f, //7
        1f, 1f, 1f, 1f, //8
        1f, 1f, 1f, 1f, //9
        1f, 1f, 1f, 1f, //10
        1f, 1f, 1f, 1f, //11
        1f, 1f, 1f, 1f, //12
        1f, 1f, 1f, 1f, //13
        1f, 1f, 1f, 1f, //14
        1f, 1f, 1f, 1f, //15
        1f, 1f, 1f, 1f, //16
        1f, 1f, 1f, 1f, //17
        1f, 1f, 1f, 1f, //18
        1f, 1f, 1f, 1f, //19
        1f, 1f, 1f, 1f, //20
        1f, 1f, 1f, 1f, //21
        1f, 1f, 1f, 1f, //22
        1f, 1f, 1f, 1f, //23
        1f, 1f, 1f, 1f, //24
        1f, 1f, 1f, 1f, //25
        1f, 1f, 1f, 1f, //26
        1f, 1f, 1f, 1f, //27
        1f, 1f, 1f, 1f, //28
        1f, 1f, 1f, 1f, //29
        1f, 1f, 1f, 1f, //30
        1f, 1f, 1f, 1f, //31
        1f, 1f, 1f, 1f, //32
        1f, 1f, 1f, 1f, //33
        1f, 1f, 1f, 1f, //34
        1f, 1f, 1f, 1f, //35
        1f, 1f, 1f, 1f, //36
        1f, 1f, 1f, 1f, //37
        1f, 1f, 1f, 1f, //38
        1f, 1f, 1f, 1f, //39
        1f, 1f, 1f, 1f, //40
        1f, 1f, 1f, 1f, //41
        1f, 1f, 1f, 1f, //42
        1f, 1f, 1f, 1f, //43
        1f, 1f, 1f, 1f, //44
        1f, 1f, 1f, 1f, //45
        1f, 1f, 1f, 1f, //46
        1f, 1f, 1f, 1f, //47
        1f, 1f, 1f, 1f, //48
        //back
        1f, 1f, 1f, 1f, //49
        1f, 1f, 1f, 1f, //50
        1f, 1f, 1f, 1f, //51
        1f, 1f, 1f, 1f, //52
        1f, 1f, 1f, 1f, //53
        1f, 1f, 1f, 1f, //54
        1f, 1f, 1f, 1f, //55
        1f, 1f, 1f, 1f, //56
        1f, 1f, 1f, 1f, //57
        1f, 1f, 1f, 1f, //58
        1f, 1f, 1f, 1f, //59
        1f, 1f, 1f, 1f, //60
        1f, 1f, 1f, 1f, //61
        1f, 1f, 1f, 1f, //62
        1f, 1f, 1f, 1f, //63
        1f, 1f, 1f, 1f, //64
        1f, 1f, 1f, 1f, //65
        1f, 1f, 1f, 1f, //66
        1f, 1f, 1f, 1f, //67
        1f, 1f, 1f, 1f, //68
        1f, 1f, 1f, 1f, //69
        1f, 1f, 1f, 1f, //70
        1f, 1f, 1f, 1f, //71
        1f, 1f, 1f, 1f, //72
        1f, 1f, 1f, 1f, //73
        1f, 1f, 1f, 1f, //74
        1f, 1f, 1f, 1f, //75
        1f, 1f, 1f, 1f, //76
        1f, 1f, 1f, 1f, //77
        1f, 1f, 1f, 1f, //78
        1f, 1f, 1f, 1f, //79
        1f, 1f, 1f, 1f, //80
        1f, 1f, 1f, 1f, //81
        1f, 1f, 1f, 1f, //82
        1f, 1f, 1f, 1f, //83
        1f, 1f, 1f, 1f, //84
        1f, 1f, 1f, 1f, //85
        1f, 1f, 1f, 1f, //86
        1f, 1f, 1f, 1f, //87
        1f, 1f, 1f, 1f, //88
        1f, 1f, 1f, 1f, //89
        1f, 1f, 1f, 1f, //90
        1f, 1f, 1f, 1f, //91
        1f, 1f, 1f, 1f, //92
        1f, 1f, 1f, 1f, //93
        1f, 1f, 1f, 1f, //94
        1f, 1f, 1f, 1f, //95
        1f, 1f, 1f, 1f, //96
        1f, 1f, 1f, 1f //97
    )

    private val letterRVertices = floatArrayOf(
        //front
        -2f, 3f, 1f, //0
        1f, 3f, 1f, //1
        1.2936f, 2.946699f, 1f, //2
        1.5728f, 2.849599f, 1f, //3
        1.835199f, 2.7129f, 1f, //4
        2.0784f, 2.5408f, 1f, //5
        2.3f, 2.3375f, 1f, //6
        2.497599f, 2.1072f, 1f, //7
        2.668799f, 1.8541f, 1f, //8
        2.8112f, 1.5824f, 1f, //9
        2.9224f, 1.2963f, 1f, //10
        3f, 1f, 1f, //11
        3f, 1f, 1f, //12
        2.922399f, 0.703699f, 1f, //13
        2.8112f, 0.4176f, 1f, //14
        2.6688f, 0.1459f, 1f, //15
        2.497599f, -0.107199f, 1f, //16
        2.3f, -0.337499f, 1f, //17
        2.0784f, -0.5408f, 1f, //18
        1.8352f, -0.712899f, 1f, //19
        1.572799f, -0.8496f, 1f, //20
        1.2936f, -0.9467f, 1f, //21
        1f, -1f, 1f, //22
        0f, -1f, 1f, //23
        0f, 2f, 1f, //24
        1f, 2f, 1f, //25
        1.158949f, 1.966599f, 1f, //26
        1.3056f, 1.9248f, 1f, //27
        1.439649f, 1.8722f, 1f, //28
        1.5608f, 1.8064f, 1f, //29
        1.668749f, 1.725f, 1f, //30
        1.763199f, 1.6256f, 1f, //31
        1.84385f, 1.5058f, 1f, //32
        1.9104f, 1.3632f, 1f, //33
        1.962549f, 1.1954f, 1f, //34
        2f, 1f, 1f, //35
        1.962549f, 0.8046f, 1f, //36
        1.9104f, 0.6368f, 1f, //37
        1.84385f, 0.4942f, 1f, //38
        1.763199f, 0.3744f, 1f, //39
        1.668749f, 0.275f, 1f, //40
        1.5608f, 0.1936f, 1f, //41
        1.439649f, 0.1278f, 1f, //42
        1.3056f, 0.0752f, 1f, //43
        1.158949f, 0.033401f, 1f, //44
        1f, 0f, 1f, //45
        0f, 0f, 1f, //46
        0f, -3f, 1f, //47
        -2f, -3f, 1f, //48
        //back
        -2f, 3f, -1f, //49
        1f, 3f, -1f, //50
        1.2936f, 2.946699f, -1f, //51
        1.5728f, 2.849599f, -1f, //52
        1.835199f, 2.7129f, -1f, //53
        2.0784f, 2.5408f, -1f, //54
        2.3f, 2.3375f, -1f, //55
        2.497599f, 2.1072f, -1f, //56
        2.668799f, 1.8541f, -1f, //57
        2.8112f, 1.5824f, -1f, //58
        2.9224f, 1.2963f, -1f, //59
        3f, 1f, -1f, //60
        3f, 1f, -1f, //61
        2.922399f, 0.703699f, -1f, //62
        2.8112f, 0.4176f, -1f, //63
        2.6688f, 0.1459f, -1f, //64
        2.497599f, -0.107199f, -1f, //65
        2.3f, -0.337499f, -1f, //66
        2.0784f, -0.5408f, -1f, //67
        1.8352f, -0.712899f, -1f, //68
        1.572799f, -0.8496f, -1f, //69
        1.2936f, -0.9467f, -1f, //70
        1f, -1f, -1f, //71
        0f, -1f, -1f, //72
        0f, 2f, -1f, //73
        1f, 2f, -1f, //74
        1.158949f, 1.966599f, -1f, //75
        1.3056f, 1.9248f, -1f, //76
        1.439649f, 1.8722f, -1f, //77
        1.5608f, 1.8064f, -1f, //78
        1.668749f, 1.725f, -1f, //79
        1.763199f, 1.6256f, -1f, //80
        1.84385f, 1.5058f, -1f, //81
        1.9104f, 1.3632f, -1f, //82
        1.962549f, 1.1954f, -1f, //83
        2f, 1f, -1f, //84
        1.962549f, 0.8046f, -1f, //85
        1.9104f, 0.6368f, -1f, //86
        1.84385f, 0.4942f, -1f, //87
        1.763199f, 0.3744f, -1f, //88
        1.668749f, 0.275f, -1f, //89
        1.5608f, 0.1936f, -1f, //90
        1.439649f, 0.1278f, -1f, //91
        1.3056f, 0.0752f, -1f, //92
        1.158949f, 0.033401f, -1f, //93
        1f, 0f, -1f, //94
        0f, 0f, -1f, //95
        0f, -3f, -1f, //96
        -2f, -3f, -1f, //97
        //r front
        1f, -3f, 1f, //98
        3f, -3f, 1f, //99
        //r back
        1f, -3f, -1f, //100
        3f, -3f, -1f //101
    )

    private val letterRIndices = intArrayOf(
        //front
        0, 24, 1,
        1, 24, 25,
        25, 1, 2,
        2, 26, 25,
        2, 3, 26,
        26, 27, 3,
        3, 4, 27,
        27, 28, 4,
        4, 5, 28,
        28, 29, 5,
        5, 6, 29,
        29, 30, 6,
        6, 7, 30,
        30, 31, 7,
        7, 8, 31,
        31, 32, 8,
        8, 9, 32,
        32, 33, 9,
        9, 10, 33,
        33, 34, 10,
        10, 11, 34,
        34, 35, 11,
        11, 12, 35,
        35, 36, 12,
        12, 13, 36,
        36, 37, 13,
        13, 14, 37,
        37, 38, 14,
        14, 15, 38,
        38, 39, 15,
        15, 16, 39,
        39, 40, 16,
        16, 17, 40,
        40, 41, 17,
        17, 18, 41,
        41, 42, 18,
        18, 19, 42,
        42, 43, 19,
        19, 20, 43,
        43, 44, 20,
        20, 21, 44,
        44, 45, 21,
        21, 22, 45,
        45, 46, 22,
        22, 23, 46,
        23, 47, 48,
        48, 23, 0,
        0, 24, 23,
        //back
        49, 73, 50,
        50, 73, 74,
        74, 50, 51,
        51, 75, 74,
        51, 52, 75,
        75, 76, 52,
        52, 53, 76,
        76, 77, 53,
        53, 54, 77,
        77, 78, 54,
        54, 55, 78,
        78, 79, 55,
        55, 56, 79,
        79, 80, 56,
        56, 57, 80,
        80, 81, 57,
        57, 58, 81,
        81, 82, 58,
        58, 59, 82,
        82, 83, 59,
        59, 60, 83,
        83, 84, 60,
        60, 61, 84,
        84, 85, 61,
        61, 62, 85,
        85, 86, 62,
        62, 63, 86,
        86, 87, 63,
        63, 64, 87,
        87, 88, 64,
        64, 65, 88,
        88, 89, 65,
        65, 66, 89,
        89, 90, 66,
        66, 67, 90,
        90, 91, 67,
        67, 68, 91,
        91, 92, 68,
        68, 69, 92,
        92, 93, 69,
        69, 70, 93,
        93, 94, 70,
        70, 71, 94,
        94, 95, 71,
        71, 72, 95,
        72, 96, 97,
        97, 72, 49,
        49, 73, 72,
        //outer side
        0, 1, 50,
        50, 49, 0,
        1, 2, 51,
        51, 50, 1,
        2, 3, 52,
        52, 51, 2,
        3, 4, 53,
        53, 52, 3,
        4, 5, 54,
        54, 53, 4,
        5, 6, 55,
        55, 54, 5,
        6, 7, 56,
        56, 55, 6,
        7, 8, 57,
        57, 56, 7,
        8, 9, 58,
        58, 57, 8,
        9, 10, 59,
        59, 58, 9,
        10, 11, 60,
        60, 59, 10,
        11, 12, 61,
        61, 60, 11,
        12, 13, 62,
        62, 61, 12,
        13, 14, 63,
        63, 62, 13,
        14, 15, 64,
        64, 63, 14,
        15, 16, 65,
        65, 64, 15,
        16, 17, 66,
        66, 65, 16,
        17, 18, 67,
        67, 66, 17,
        18, 19, 68,
        68, 67, 18,
        19, 20, 69,
        69, 68, 19,
        20, 21, 70,
        70, 69, 20,
        21, 22, 71,
        71, 70, 21,
        22, 23, 72,
        72, 71, 22,
        23, 47, 96,
        96, 72, 23,
        47, 48, 97,
        97, 96, 47,
        48, 0, 49,
        49, 97, 48,
        //inner side
        24, 25, 74,
        74, 73, 24,
        25, 26, 75,
        75, 74, 25,
        26, 27, 76,
        76, 75, 26,
        27, 28, 77,
        77, 76, 27,
        28, 29, 78,
        78, 77, 28,
        29, 30, 79,
        79, 78, 29,
        30, 31, 80,
        80, 79, 30,
        31, 32, 81,
        81, 80, 31,
        32, 33, 82,
        82, 81, 32,
        33, 34, 83,
        83, 82, 33,
        34, 35, 84,
        84, 83, 34,
        35, 36, 85,
        85, 84, 35,
        36, 37, 86,
        86, 85, 36,
        37, 38, 87,
        87, 86, 37,
        38, 39, 88,
        88, 87, 38,
        39, 40, 89,
        89, 88, 39,
        40, 41, 90,
        90, 89, 40,
        41, 42, 91,
        91, 90, 41,
        42, 43, 92,
        92, 91, 42,
        43, 44, 93,
        93, 92, 43,
        44, 45, 94,
        94, 93, 44,
        45, 46, 95,
        95, 94, 45,
        46, 24, 73,
        73, 95, 46,
        //r front
        23, 98, 99,
        99, 22, 23,
        //r back
        72, 100, 101,
        101, 71, 72,
        //r side
        23, 98, 100,
        100, 72, 23,
        98, 99, 101,
        101, 100, 98,
        99, 22, 71,
        71, 101, 99
    )

    private val letterRColor = floatArrayOf(
        //front
        1f, 1f, 1f, 1f, //0
        1f, 1f, 1f, 1f, //1
        1f, 1f, 1f, 1f, //2
        1f, 1f, 1f, 1f, //3
        1f, 1f, 1f, 1f, //4
        1f, 1f, 1f, 1f, //5
        1f, 1f, 1f, 1f, //6
        1f, 1f, 1f, 1f, //7
        1f, 1f, 1f, 1f, //8
        1f, 1f, 1f, 1f, //9
        1f, 1f, 1f, 1f, //10
        1f, 1f, 1f, 1f, //11
        1f, 1f, 1f, 1f, //12
        1f, 1f, 1f, 1f, //13
        1f, 1f, 1f, 1f, //14
        1f, 1f, 1f, 1f, //15
        1f, 1f, 1f, 1f, //16
        1f, 1f, 1f, 1f, //17
        1f, 1f, 1f, 1f, //18
        1f, 1f, 1f, 1f, //19
        1f, 1f, 1f, 1f, //20
        1f, 1f, 1f, 1f, //21
        1f, 1f, 1f, 1f, //22
        1f, 1f, 1f, 1f, //23
        1f, 1f, 1f, 1f, //24
        1f, 1f, 1f, 1f, //25
        1f, 1f, 1f, 1f, //26
        1f, 1f, 1f, 1f, //27
        1f, 1f, 1f, 1f, //28
        1f, 1f, 1f, 1f, //29
        1f, 1f, 1f, 1f, //30
        1f, 1f, 1f, 1f, //31
        1f, 1f, 1f, 1f, //32
        1f, 1f, 1f, 1f, //33
        1f, 1f, 1f, 1f, //34
        1f, 1f, 1f, 1f, //35
        1f, 1f, 1f, 1f, //36
        1f, 1f, 1f, 1f, //37
        1f, 1f, 1f, 1f, //38
        1f, 1f, 1f, 1f, //39
        1f, 1f, 1f, 1f, //40
        1f, 1f, 1f, 1f, //41
        1f, 1f, 1f, 1f, //42
        1f, 1f, 1f, 1f, //43
        1f, 1f, 1f, 1f, //44
        1f, 1f, 1f, 1f, //45
        1f, 1f, 1f, 1f, //46
        1f, 1f, 1f, 1f, //47
        1f, 1f, 1f, 1f, //48
        //back
        1f, 1f, 1f, 1f, //49
        1f, 1f, 1f, 1f, //50
        1f, 1f, 1f, 1f, //51
        1f, 1f, 1f, 1f, //52
        1f, 1f, 1f, 1f, //53
        1f, 1f, 1f, 1f, //54
        1f, 1f, 1f, 1f, //55
        1f, 1f, 1f, 1f, //56
        1f, 1f, 1f, 1f, //57
        1f, 1f, 1f, 1f, //58
        1f, 1f, 1f, 1f, //59
        1f, 1f, 1f, 1f, //60
        1f, 1f, 1f, 1f, //61
        1f, 1f, 1f, 1f, //62
        1f, 1f, 1f, 1f, //63
        1f, 1f, 1f, 1f, //64
        1f, 1f, 1f, 1f, //65
        1f, 1f, 1f, 1f, //66
        1f, 1f, 1f, 1f, //67
        1f, 1f, 1f, 1f, //68
        1f, 1f, 1f, 1f, //69
        1f, 1f, 1f, 1f, //70
        1f, 1f, 1f, 1f, //71
        1f, 1f, 1f, 1f, //72
        1f, 1f, 1f, 1f, //73
        1f, 1f, 1f, 1f, //74
        1f, 1f, 1f, 1f, //75
        1f, 1f, 1f, 1f, //76
        1f, 1f, 1f, 1f, //77
        1f, 1f, 1f, 1f, //78
        1f, 1f, 1f, 1f, //79
        1f, 1f, 1f, 1f, //80
        1f, 1f, 1f, 1f, //81
        1f, 1f, 1f, 1f, //82
        1f, 1f, 1f, 1f, //83
        1f, 1f, 1f, 1f, //84
        1f, 1f, 1f, 1f, //85
        1f, 1f, 1f, 1f, //86
        1f, 1f, 1f, 1f, //87
        1f, 1f, 1f, 1f, //88
        1f, 1f, 1f, 1f, //89
        1f, 1f, 1f, 1f, //90
        1f, 1f, 1f, 1f, //91
        1f, 1f, 1f, 1f, //92
        1f, 1f, 1f, 1f, //93
        1f, 1f, 1f, 1f, //94
        1f, 1f, 1f, 1f, //95
        1f, 1f, 1f, 1f, //96
        1f, 1f, 1f, 1f, //97
        //r front
        1f, 1f, 1f, 1f, //98
        1f, 1f, 1f, 1f, //99
        //r back
        1f, 1f, 1f, 1f, //100
        1f, 1f, 1f, 1f //101
    )

    private val letterEVertices = floatArrayOf(
        //front
        -2f, 3f, 1f, //0
        2f, 3f, 1f, //1
        2f, 1.6666f, 1f, //2
        0f, 1.6666f, 1f, //3
        0f, 0.6666f, 1f, //4
        1f, 0.6666f, 1f, //5
        1f, -0.6666f, 1f, //6
        0f, -0.6666f, 1f, //7
        0f, -1.6666f, 1f, //8
        2f, -1.6666f, 1f, //9
        2f, -3f, 1f, //10
        -2f, -3f, 1f, //11
        //back
        -2f, 3f, -1f, //12
        2f, 3f, -1f, //13
        2f, 1.6666f, -1f, //14
        0f, 1.6666f, -1f, //15
        0f, 0.6666f, -1f, //16
        1f, 0.6666f, -1f, //17
        1f, -0.6666f, -1f, //18
        0f, -0.6666f, -1f, //19
        0f, -1.6666f, -1f, //20
        2f, -1.6666f, -1f, //21
        2f, -3f, -1f, //22
        -2f, -3f, -1f //23
    )

    private val letterEIndices = intArrayOf(
        //front
        0, 3, 1,
        1, 2, 3,
        3, 0, 11,
        3, 8, 11,
        4, 5, 6,
        6, 7, 4,
        11, 8, 10,
        8, 9, 10,
        //back
        12, 15, 13,
        13, 14, 15,
        15, 12, 23,
        15, 20, 23,
        16, 17, 18,
        18, 19, 16,
        23, 20, 22,
        20, 21, 22,
        //side
        0, 1, 13,
        13, 12, 0,
        1, 2, 14,
        14, 13, 1,
        2, 3, 15,
        15, 14, 2,
        3, 4, 16,
        16, 15, 3,
        4, 5, 17,
        17, 16, 4,
        5, 6, 18,
        18, 17, 5,
        6, 7, 19,
        19, 18, 6,
        7, 8, 20,
        20, 19, 7,
        8, 9, 21,
        21, 20, 8,
        9, 10, 22,
        22, 21, 9,
        10, 11, 23,
        23, 22, 10,
        11, 0, 12,
        12, 23, 11
    )

    private val letterEColor = floatArrayOf(
        //front
        1f, 1f, 1f, 1f, //0
        1f, 1f, 1f, 1f, //1
        1f, 1f, 1f, 1f, //2
        1f, 1f, 1f, 1f, //3
        1f, 1f, 1f, 1f, //4
        1f, 1f, 1f, 1f, //5
        1f, 1f, 1f, 1f, //6
        1f, 1f, 1f, 1f, //7
        1f, 1f, 1f, 1f, //8
        1f, 1f, 1f, 1f, //9
        1f, 1f, 1f, 1f, //10
        1f, 1f, 1f, 1f, //11
        //back
        1f, 1f, 1f, 1f, //12
        1f, 1f, 1f, 1f, //13
        1f, 1f, 1f, 1f, //14
        1f, 1f, 1f, 1f, //15
        1f, 1f, 1f, 1f, //16
        1f, 1f, 1f, 1f, //17
        1f, 1f, 1f, 1f, //18
        1f, 1f, 1f, 1f, //19
        1f, 1f, 1f, 1f, //20
        1f, 1f, 1f, 1f, //21
        1f, 1f, 1f, 1f, //22
        1f, 1f, 1f, 1f //23
    )

    private val letterAVertices = floatArrayOf(
        //front
        -1f, 3f, 1f, //0
        1f, 3f, 1f, //1
        3f, -3f, 1f, //2
        2f, -3f, 1f, //3
        1f, -1f, 1f, //4
        -1f, -1f, 1f, //5
        -2f, -3f, 1f, //6
        -3f, -3f, 1f, //7
        0f, 2f, 1f, //8
        1f, 0f, 1f, //9
        -1f, 0f, 1f, //10
        //back
        -1f, 3f, -1f, //11
        1f, 3f, -1f, //12
        3f, -3f, -1f, //13
        2f, -3f, -1f, //14
        1f, -1f, -1f, //15
        -1f, -1f, -1f, //16
        -2f, -3f, -1f, //17
        -3f, -3f, -1f, //18
        0f, 2f, -1f, //19
        1f, 0f, -1f, //20
        -1f, 0f, -1f //21
    )

    private val letterAIndices = intArrayOf(
        //front
        0, 1, 8,
        8, 9, 1,
        1, 9, 2,
        4, 2, 3,
        4, 9, 2,
        4, 5, 9,
        9, 10, 5,
        10, 8, 0,
        0, 10, 7,
        5, 6, 7,
        5, 7, 10,
        //back
        11, 12, 19,
        19, 20, 12,
        12, 20, 13,
        15, 13, 14,
        15, 20, 13,
        15, 16, 20,
        20, 21, 16,
        21, 19, 11,
        11, 21, 18,
        16, 17, 18,
        16, 18, 21,
        //outer side
        0, 1, 12,
        12, 11, 0,
        1, 2, 13,
        13, 12, 1,
        2, 3, 14,
        14, 13, 2,
        3, 4, 15,
        15, 14, 3,
        4, 5, 16,
        16, 15, 4,
        5, 6, 17,
        17, 16, 5,
        6, 7, 18,
        18, 17, 6,
        7, 0, 11,
        11, 18, 7,
        //inner side
        8, 9, 20,
        20, 19, 8,
        9, 10, 21,
        21, 20, 9,
        10, 8, 19,
        19, 21, 10
    )

    private val letterAColor = floatArrayOf(
        //front
        1f, 1f, 1f, 1f, //0
        1f, 1f, 1f, 1f, //1
        1f, 1f, 1f, 1f, //2
        1f, 1f, 1f, 1f, //3
        1f, 1f, 1f, 1f, //4
        1f, 1f, 1f, 1f, //5
        1f, 1f, 1f, 1f, //6
        1f, 1f, 1f, 1f, //7
        1f, 1f, 1f, 1f, //8
        1f, 1f, 1f, 1f, //9
        1f, 1f, 1f, 1f, //10
        //back
        1f, 1f, 1f, 1f, //0
        1f, 1f, 1f, 1f, //1
        1f, 1f, 1f, 1f, //2
        1f, 1f, 1f, 1f, //3
        1f, 1f, 1f, 1f, //4
        1f, 1f, 1f, 1f, //5
        1f, 1f, 1f, 1f, //6
        1f, 1f, 1f, 1f, //7
        1f, 1f, 1f, 1f, //8
        1f, 1f, 1f, 1f, //9
        1f, 1f, 1f, 1f //10
    )

    private val letterLVertices = floatArrayOf(
        //front
        -2f, 3f, 1f, //0
        0f, 3f, 1f, //1
        0f, -1f, 1f, //2
        2f, -1f, 1f, //3
        2f, -3f, 1f, //4
        -2f, -3f, 1f, //5
        //back
        -2f, 3f, -1f, //6
        0f, 3f, -1f, //7
        0f, -1f, -1f, //8
        2f, -1f, -1f, //9
        2f, -3f, -1f, //10
        -2f, -3f, -1f //11
    )

    private val letterLIndices = intArrayOf(
        //front
        0, 1, 2,
        2, 3, 4,
        4, 5, 2,
        2, 5, 0,
        //back
        6, 7, 8,
        8, 9, 10,
        10, 11, 8,
        8, 11, 6,
        //side
        0, 1, 7,
        7, 6, 0,
        1, 2, 8,
        8, 7, 1,
        2, 3, 9,
        9, 8, 2,
        3, 4, 10,
        10, 9, 3,
        4, 5, 11,
        11, 10, 4,
        5, 0, 6,
        6, 11, 5
    )

    private val letterLColor = floatArrayOf(
        //front
        1f, 1f, 1f, 1f, //0
        1f, 1f, 1f, 1f, //1
        1f, 1f, 1f, 1f, //2
        1f, 1f, 1f, 1f, //3
        1f, 1f, 1f, 1f, //4
        1f, 1f, 1f, 1f, //5
        //back
        1f, 1f, 1f, 1f, //6
        1f, 1f, 1f, 1f, //7
        1f, 1f, 1f, 1f, //8
        1f, 1f, 1f, 1f, //9
        1f, 1f, 1f, 1f, //10
        1f, 1f, 1f, 1f //11
    )

    init {
        GraphicUtils.translateFloatArrayVertices(letterFirstIVertices, -20f, 0f, 0f)
        GraphicUtils.translateFloatArrayVertices(letterMVertices, -14f, 0f, 0f)
        GraphicUtils.translateFloatArrayVertices(letterPVertices, -7f, 0f, 0f)
        GraphicUtils.translateFloatArrayVertices(letterEVertices, -1f, 0f, 0f)
        GraphicUtils.translateFloatArrayVertices(letterRVertices, 4f, 0f, 0f)
        GraphicUtils.translateFloatArrayVertices(letterSecondIVertices, 9f, 0f, 0f)
        GraphicUtils.translateFloatArrayVertices(letterAVertices, 14f, 0f, 0f)
        GraphicUtils.translateFloatArrayVertices(letterLVertices, 20f, 0f, 0f)

        // First I
        indexFirstIBuffer = IntBuffer.allocate(letterIIndices.size)
        indexFirstIBuffer?.put(letterIIndices)
        indexFirstIBuffer?.position(0)

        // initialize vertex byte buffer for shape coordinates
        val bb1: ByteBuffer =
            ByteBuffer.allocateDirect(letterFirstIVertices.size * 4) // (# of coordinate values * 4 bytes per float)
        bb1.order(ByteOrder.nativeOrder())
        vertexFirstIBuffer = bb1.asFloatBuffer()
        vertexFirstIBuffer?.put(letterFirstIVertices)
        vertexFirstIBuffer?.position(0)

        //initialize color byte buffer
        val cb1: ByteBuffer =
            ByteBuffer.allocateDirect(letterIColor.size * 4) // (# of coordinate values * 4 bytes per float)
        cb1.order(ByteOrder.nativeOrder())
        colorFirstIBuffer = cb1.asFloatBuffer()
        colorFirstIBuffer?.put(letterIColor)
        colorFirstIBuffer?.position(0)

        //M
        indexMBuffer = IntBuffer.allocate(letterMIndices.size)
        indexMBuffer?.put(letterMIndices)
        indexMBuffer?.position(0)

        // initialize vertex byte buffer for shape coordinates
        val bb2: ByteBuffer =
            ByteBuffer.allocateDirect(letterMVertices.size * 4) // (# of coordinate values * 4 bytes per float)
        bb2.order(ByteOrder.nativeOrder())
        vertexMBuffer = bb2.asFloatBuffer()
        vertexMBuffer?.put(letterMVertices)
        vertexMBuffer?.position(0)

        //initialize color byte buffer
        val cb2: ByteBuffer =
            ByteBuffer.allocateDirect(letterMColor.size * 4) // (# of coordinate values * 4 bytes per float)
        cb2.order(ByteOrder.nativeOrder())
        colorMBuffer = cb2.asFloatBuffer()
        colorMBuffer?.put(letterMColor)
        colorMBuffer?.position(0)

        //P
        indexPBuffer = IntBuffer.allocate(letterPIndices.size)
        indexPBuffer?.put(letterPIndices)
        indexPBuffer?.position(0)

        // initialize vertex byte buffer for shape coordinates
        val bb3: ByteBuffer =
            ByteBuffer.allocateDirect(letterPVertices.size * 4) // (# of coordinate values * 4 bytes per float)
        bb3.order(ByteOrder.nativeOrder())
        vertexPBuffer = bb3.asFloatBuffer()
        vertexPBuffer?.put(letterPVertices)
        vertexPBuffer?.position(0)

        //initialize color byte buffer
        val cb3: ByteBuffer =
            ByteBuffer.allocateDirect(letterPColor.size * 4) // (# of coordinate values * 4 bytes per float)
        cb3.order(ByteOrder.nativeOrder())
        colorPBuffer = cb3.asFloatBuffer()
        colorPBuffer?.put(letterPColor)
        colorPBuffer?.position(0)

        //E
        indexEBuffer = IntBuffer.allocate(letterEIndices.size)
        indexEBuffer?.put(letterEIndices)
        indexEBuffer?.position(0)

        // initialize vertex byte buffer for shape coordinates
        val bb4: ByteBuffer =
            ByteBuffer.allocateDirect(letterEVertices.size * 4) // (# of coordinate values * 4 bytes per float)
        bb4.order(ByteOrder.nativeOrder())
        vertexEBuffer = bb4.asFloatBuffer()
        vertexEBuffer?.put(letterEVertices)
        vertexEBuffer?.position(0)

        //initialize color byte buffer
        val cb4: ByteBuffer =
            ByteBuffer.allocateDirect(letterEColor.size * 4) // (# of coordinate values * 4 bytes per float)
        cb4.order(ByteOrder.nativeOrder())
        colorEBuffer = cb4.asFloatBuffer()
        colorEBuffer?.put(letterEColor)
        colorEBuffer?.position(0)

        //R
        indexRBuffer = IntBuffer.allocate(letterRIndices.size)
        indexRBuffer?.put(letterRIndices)
        indexRBuffer?.position(0)

        // initialize vertex byte buffer for shape coordinates
        val bb5: ByteBuffer =
            ByteBuffer.allocateDirect(letterRVertices.size * 4) // (# of coordinate values * 4 bytes per float)
        bb5.order(ByteOrder.nativeOrder())
        vertexRBuffer = bb5.asFloatBuffer()
        vertexRBuffer?.put(letterRVertices)
        vertexRBuffer?.position(0)

        //initialize color byte buffer
        val cb5: ByteBuffer =
            ByteBuffer.allocateDirect(letterRColor.size * 4) // (# of coordinate values * 4 bytes per float)
        cb5.order(ByteOrder.nativeOrder())
        colorRBuffer = cb5.asFloatBuffer()
        colorRBuffer?.put(letterRColor)
        colorRBuffer?.position(0)

        //Second I
        indexSecondIBuffer = IntBuffer.allocate(letterIIndices.size)
        indexSecondIBuffer?.put(letterIIndices)
        indexSecondIBuffer?.position(0)

        // initialize vertex byte buffer for shape coordinates
        val bb6: ByteBuffer =
            ByteBuffer.allocateDirect(letterSecondIVertices.size * 4) // (# of coordinate values * 4 bytes per float)
        bb6.order(ByteOrder.nativeOrder())
        vertexSecondIBuffer = bb6.asFloatBuffer()
        vertexSecondIBuffer?.put(letterSecondIVertices)
        vertexSecondIBuffer?.position(0)

        //initialize color byte buffer
        val cb6: ByteBuffer =
            ByteBuffer.allocateDirect(letterIColor.size * 4) // (# of coordinate values * 4 bytes per float)
        cb6.order(ByteOrder.nativeOrder())
        colorSecondIBuffer = cb6.asFloatBuffer()
        colorSecondIBuffer?.put(letterIColor)
        colorSecondIBuffer?.position(0)

        //A
        indexABuffer = IntBuffer.allocate(letterAIndices.size)
        indexABuffer?.put(letterAIndices)
        indexABuffer?.position(0)

        // initialize vertex byte buffer for shape coordinates
        val bb7: ByteBuffer =
            ByteBuffer.allocateDirect(letterAVertices.size * 4) // (# of coordinate values * 4 bytes per float)
        bb7.order(ByteOrder.nativeOrder())
        vertexABuffer = bb7.asFloatBuffer()
        vertexABuffer?.put(letterAVertices)
        vertexABuffer?.position(0)

        //initialize color byte buffer
        val cb7: ByteBuffer =
            ByteBuffer.allocateDirect(letterAColor.size * 4) // (# of coordinate values * 4 bytes per float)
        cb7.order(ByteOrder.nativeOrder())
        colorABuffer = cb7.asFloatBuffer()
        colorABuffer?.put(letterAColor)
        colorABuffer?.position(0)

        //L
        indexLBuffer = IntBuffer.allocate(letterLIndices.size)
        indexLBuffer?.put(letterLIndices)
        indexLBuffer?.position(0)

        // initialize vertex byte buffer for shape coordinates
        val bb8: ByteBuffer =
            ByteBuffer.allocateDirect(letterLVertices.size * 4) // (# of coordinate values * 4 bytes per float)
        bb8.order(ByteOrder.nativeOrder())
        vertexLBuffer = bb8.asFloatBuffer()
        vertexLBuffer?.put(letterLVertices)
        vertexLBuffer?.position(0)

        //initialize color byte buffer
        val cb8: ByteBuffer =
            ByteBuffer.allocateDirect(letterLColor.size * 4) // (# of coordinate values * 4 bytes per float)
        cb8.order(ByteOrder.nativeOrder())
        colorLBuffer = cb8.asFloatBuffer()
        colorLBuffer?.put(letterLColor)
        colorLBuffer?.position(0)

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

        //First I
        //set the attribute of the vertex to point to the vertex buffer
        GLES32.glVertexAttribPointer(
            mPositionHandle, COORDINATES_PER_VERTEX,
            GLES32.GL_FLOAT, false, VERTEX_STRIDE, vertexFirstIBuffer
        )
        GLES32.glVertexAttribPointer(
            mColorHandle, COLOR_PER_VERTEX,
            GLES32.GL_FLOAT, false, COLOR_STRIDE, colorFirstIBuffer
        )
        // Draw the triangle
        GLES32.glDrawElements(
            GLES32.GL_TRIANGLES,
            letterIIndices.size,
            GLES32.GL_UNSIGNED_INT,
            indexFirstIBuffer
        )

        //M
        //set the attribute of the vertex to point to the vertex buffer
        GLES32.glVertexAttribPointer(
            mPositionHandle, COORDINATES_PER_VERTEX,
            GLES32.GL_FLOAT, false, VERTEX_STRIDE, vertexMBuffer
        )
        GLES32.glVertexAttribPointer(
            mColorHandle, COLOR_PER_VERTEX,
            GLES32.GL_FLOAT, false, COLOR_STRIDE, colorMBuffer
        )
        // Draw the triangle
        GLES32.glDrawElements(
            GLES32.GL_TRIANGLES,
            letterMIndices.size,
            GLES32.GL_UNSIGNED_INT,
            indexMBuffer
        )

        //P
        //set the attribute of the vertex to point to the vertex buffer
        GLES32.glVertexAttribPointer(
            mPositionHandle, COORDINATES_PER_VERTEX,
            GLES32.GL_FLOAT, false, VERTEX_STRIDE, vertexPBuffer
        )
        GLES32.glVertexAttribPointer(
            mColorHandle, COLOR_PER_VERTEX,
            GLES32.GL_FLOAT, false, COLOR_STRIDE, colorPBuffer
        )
        // Draw the triangle
        GLES32.glDrawElements(
            GLES32.GL_TRIANGLES,
            letterPIndices.size,
            GLES32.GL_UNSIGNED_INT,
            indexPBuffer
        )

        //E
        //set the attribute of the vertex to point to the vertex buffer
        GLES32.glVertexAttribPointer(
            mPositionHandle, COORDINATES_PER_VERTEX,
            GLES32.GL_FLOAT, false, VERTEX_STRIDE, vertexEBuffer
        )
        GLES32.glVertexAttribPointer(
            mColorHandle, COLOR_PER_VERTEX,
            GLES32.GL_FLOAT, false, COLOR_STRIDE, colorEBuffer
        )
        // Draw the triangle
        GLES32.glDrawElements(
            GLES32.GL_TRIANGLES,
            letterEIndices.size,
            GLES32.GL_UNSIGNED_INT,
            indexEBuffer
        )

        //R
        //set the attribute of the vertex to point to the vertex buffer
        GLES32.glVertexAttribPointer(
            mPositionHandle, COORDINATES_PER_VERTEX,
            GLES32.GL_FLOAT, false, VERTEX_STRIDE, vertexRBuffer
        )
        GLES32.glVertexAttribPointer(
            mColorHandle, COLOR_PER_VERTEX,
            GLES32.GL_FLOAT, false, COLOR_STRIDE, colorRBuffer
        )
        // Draw the triangle
        GLES32.glDrawElements(
            GLES32.GL_TRIANGLES,
            letterRIndices.size,
            GLES32.GL_UNSIGNED_INT,
            indexRBuffer
        )

        //Second I
        //set the attribute of the vertex to point to the vertex buffer
        GLES32.glVertexAttribPointer(
            mPositionHandle, COORDINATES_PER_VERTEX,
            GLES32.GL_FLOAT, false, VERTEX_STRIDE, vertexSecondIBuffer
        )
        GLES32.glVertexAttribPointer(
            mColorHandle, COLOR_PER_VERTEX,
            GLES32.GL_FLOAT, false, COLOR_STRIDE, colorSecondIBuffer
        )
        // Draw the triangle
        GLES32.glDrawElements(
            GLES32.GL_TRIANGLES,
            letterIIndices.size,
            GLES32.GL_UNSIGNED_INT,
            indexSecondIBuffer
        )

        //A
        //set the attribute of the vertex to point to the vertex buffer
        GLES32.glVertexAttribPointer(
            mPositionHandle, COORDINATES_PER_VERTEX,
            GLES32.GL_FLOAT, false, VERTEX_STRIDE, vertexABuffer
        )
        GLES32.glVertexAttribPointer(
            mColorHandle, COLOR_PER_VERTEX,
            GLES32.GL_FLOAT, false, COLOR_STRIDE, colorABuffer
        )
        // Draw the triangle
        GLES32.glDrawElements(
            GLES32.GL_TRIANGLES,
            letterAIndices.size,
            GLES32.GL_UNSIGNED_INT,
            indexABuffer
        )

        //L
        //set the attribute of the vertex to point to the vertex buffer
        GLES32.glVertexAttribPointer(
            mPositionHandle, COORDINATES_PER_VERTEX,
            GLES32.GL_FLOAT, false, VERTEX_STRIDE, vertexLBuffer
        )
        GLES32.glVertexAttribPointer(
            mColorHandle, COLOR_PER_VERTEX,
            GLES32.GL_FLOAT, false, COLOR_STRIDE, colorLBuffer
        )
        // Draw the triangle
        GLES32.glDrawElements(
            GLES32.GL_TRIANGLES,
            letterLIndices.size,
            GLES32.GL_UNSIGNED_INT,
            indexLBuffer
        )

    }

}