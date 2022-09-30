package com.example.myapplication.ui.frame

import android.opengl.GLES32
import com.example.myapplication.ui.renderer.OpenGLLogoRenderer
import com.example.myapplication.utils.GraphicUtils
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.IntBuffer
import kotlin.math.abs

/**
 * Created by Athenriel on 9/30/2022
 */
class LogoFrame(private val renderer: OpenGLLogoRenderer) {

    private var indexMBuffer: IntBuffer? = null
    private var vertexMBuffer: FloatBuffer? = null
    private var colorMBuffer: FloatBuffer? = null

    private var indexEBuffer: IntBuffer? = null
    private var vertexEBuffer: FloatBuffer? = null
    private var colorEBuffer: FloatBuffer? = null

    private var indexRBuffer: IntBuffer? = null
    private var vertexRBuffer: FloatBuffer? = null
    private var colorRBuffer: FloatBuffer? = null

    private var indexSphere1Buffer: IntBuffer? = null
    private var vertexSphere1Buffer: FloatBuffer? = null
    private var colorSphere1Buffer: FloatBuffer? = null

    private var indexSphere2Buffer: IntBuffer? = null
    private var vertexSphere2Buffer: FloatBuffer? = null
    private var colorSphere2Buffer: FloatBuffer? = null

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
        0f, 0f, 0f, 0.5f, //0
        0f, 0f, 0f, 0.5f, //1
        0f, 0f, 0f, 0.5f, //2
        0f, 0f, 0f, 0.5f, //3
        0f, 0f, 0f, 0.5f, //4
        0f, 0f, 0f, 0.5f, //5
        0f, 0f, 0f, 0.5f, //6
        0f, 0f, 0f, 0.5f, //7
        0f, 0f, 0f, 0.5f, //8
        0f, 0f, 0f, 0.5f, //9
        0f, 0f, 0f, 0.5f, //10
        0f, 0f, 0f, 0.5f, //11
        0f, 0f, 0f, 0.5f, //12
        //back
        0f, 1f, 0f, 1f, //13
        0f, 1f, 0f, 1f, //14
        0f, 1f, 0f, 1f, //15
        0f, 1f, 0f, 1f, //16
        0f, 1f, 0f, 1f, //17
        0f, 1f, 0f, 1f, //18
        0f, 1f, 0f, 1f, //19
        0f, 1f, 0f, 1f, //20
        0f, 1f, 0f, 1f, //21
        0f, 1f, 0f, 1f, //22
        0f, 1f, 0f, 1f, //23
        0f, 1f, 0f, 1f, //24
        0f, 1f, 0f, 1f //25
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
        0f, 0f, 0f, 0.5f, //0
        0f, 0f, 0f, 0.5f, //1
        0f, 0f, 0f, 0.5f, //2
        0f, 0f, 0f, 0.5f, //3
        0f, 0f, 0f, 0.5f, //4
        0f, 0f, 0f, 0.5f, //5
        0f, 0f, 0f, 0.5f, //6
        0f, 0f, 0f, 0.5f, //7
        0f, 0f, 0f, 0.5f, //8
        0f, 0f, 0f, 0.5f, //9
        0f, 0f, 0f, 0.5f, //10
        0f, 0f, 0f, 0.5f, //11
        0f, 0f, 0f, 0.5f, //12
        0f, 0f, 0f, 0.5f, //13
        0f, 0f, 0f, 0.5f, //14
        0f, 0f, 0f, 0.5f, //15
        0f, 0f, 0f, 0.5f, //16
        0f, 0f, 0f, 0.5f, //17
        0f, 0f, 0f, 0.5f, //18
        0f, 0f, 0f, 0.5f, //19
        0f, 0f, 0f, 0.5f, //20
        0f, 0f, 0f, 0.5f, //21
        0f, 0f, 0f, 0.5f, //22
        0f, 0f, 0f, 0.5f, //23
        0f, 0f, 0f, 0.5f, //24
        0f, 0f, 0f, 0.5f, //25
        0f, 0f, 0f, 0.5f, //26
        0f, 0f, 0f, 0.5f, //27
        0f, 0f, 0f, 0.5f, //28
        0f, 0f, 0f, 0.5f, //29
        0f, 0f, 0f, 0.5f, //30
        0f, 0f, 0f, 0.5f, //31
        0f, 0f, 0f, 0.5f, //32
        0f, 0f, 0f, 0.5f, //33
        0f, 0f, 0f, 0.5f, //34
        0f, 0f, 0f, 0.5f, //35
        0f, 0f, 0f, 0.5f, //36
        0f, 0f, 0f, 0.5f, //37
        0f, 0f, 0f, 0.5f, //38
        0f, 0f, 0f, 0.5f, //39
        0f, 0f, 0f, 0.5f, //40
        0f, 0f, 0f, 0.5f, //41
        0f, 0f, 0f, 0.5f, //42
        0f, 0f, 0f, 0.5f, //43
        0f, 0f, 0f, 0.5f, //44
        0f, 0f, 0f, 0.5f, //45
        0f, 0f, 0f, 0.5f, //46
        0f, 0f, 0f, 0.5f, //47
        0f, 0f, 0f, 0.5f, //48
        //back
        1f, 0f, 0f, 1f, //49
        1f, 0f, 0f, 1f, //50
        1f, 0f, 0f, 1f, //51
        1f, 0f, 0f, 1f, //52
        1f, 0f, 0f, 1f, //53
        1f, 0f, 0f, 1f, //54
        1f, 0f, 0f, 1f, //55
        1f, 0f, 0f, 1f, //56
        1f, 0f, 0f, 1f, //57
        1f, 0f, 0f, 1f, //58
        1f, 0f, 0f, 1f, //59
        1f, 0f, 0f, 1f, //60
        1f, 0f, 0f, 1f, //61
        1f, 0f, 0f, 1f, //62
        1f, 0f, 0f, 1f, //63
        1f, 0f, 0f, 1f, //64
        1f, 0f, 0f, 1f, //65
        1f, 0f, 0f, 1f, //66
        1f, 0f, 0f, 1f, //67
        1f, 0f, 0f, 1f, //68
        1f, 0f, 0f, 1f, //69
        1f, 0f, 0f, 1f, //70
        1f, 0f, 0f, 1f, //71
        1f, 0f, 0f, 1f, //72
        1f, 0f, 0f, 1f, //73
        1f, 0f, 0f, 1f, //74
        1f, 0f, 0f, 1f, //75
        1f, 0f, 0f, 1f, //76
        1f, 0f, 0f, 1f, //77
        1f, 0f, 0f, 1f, //78
        1f, 0f, 0f, 1f, //79
        1f, 0f, 0f, 1f, //80
        1f, 0f, 0f, 1f, //81
        1f, 0f, 0f, 1f, //82
        1f, 0f, 0f, 1f, //83
        1f, 0f, 0f, 1f, //84
        1f, 0f, 0f, 1f, //85
        1f, 0f, 0f, 1f, //86
        1f, 0f, 0f, 1f, //87
        1f, 0f, 0f, 1f, //88
        1f, 0f, 0f, 1f, //89
        1f, 0f, 0f, 1f, //90
        1f, 0f, 0f, 1f, //91
        1f, 0f, 0f, 1f, //92
        1f, 0f, 0f, 1f, //93
        1f, 0f, 0f, 1f, //94
        1f, 0f, 0f, 1f, //95
        1f, 0f, 0f, 1f, //96
        1f, 0f, 0f, 1f, //97
        //r front
        0f, 0f, 0f, 0.5f, //98
        0f, 0f, 0f, 0.5f, //99
        //r back
        1f, 0f, 0f, 1f, //100
        1f, 0f, 0f, 1f //101
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
        0f, 0f, 0f, 0.5f, //0
        0f, 0f, 0f, 0.5f, //1
        0f, 0f, 0f, 0.5f, //2
        0f, 0f, 0f, 0.5f, //3
        0f, 0f, 0f, 0.5f, //4
        0f, 0f, 0f, 0.5f, //5
        0f, 0f, 0f, 0.5f, //6
        0f, 0f, 0f, 0.5f, //7
        0f, 0f, 0f, 0.5f, //8
        0f, 0f, 0f, 0.5f, //9
        0f, 0f, 0f, 0.5f, //10
        0f, 0f, 0f, 0.5f, //11
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

    private var indexesSphere1Size = 0
    private var indexesSphere2Size = 0

    init {
        GraphicUtils.translateFloatArrayVertices(letterRVertices, -8f, 0f, 0f)
        GraphicUtils.translateFloatArrayVertices(letterEVertices, -1f, 0f, 0f)
        GraphicUtils.translateFloatArrayVertices(letterMVertices, 6f, 0f, 0f)

        val radius = 2f
        val latitudes = 50
        val longitudes = 50
        val dist = 3f

        val vertices1List = GraphicUtils.getVerticesForSphere(radius, latitudes, longitudes, dist)
        val vertices2List = GraphicUtils.getVerticesForSphere(radius, latitudes, longitudes, -dist)
        val indexes1Array = GraphicUtils.getTrianglesIndexesForSphere(latitudes, longitudes)
        val indexes2Array = GraphicUtils.getTrianglesIndexesForSphere(latitudes, longitudes)

        val vertices1Array = FloatArray(vertices1List.size * 3)
        var verticesIndex = 0
        for (threeDModel in vertices1List) {
            vertices1Array[verticesIndex++] = threeDModel.x
            vertices1Array[verticesIndex++] = threeDModel.y
            vertices1Array[verticesIndex++] = threeDModel.z
        }

        verticesIndex = 0
        val vertices2Array = FloatArray(vertices2List.size * 3)
        for (threeDModel in vertices2List) {
            vertices2Array[verticesIndex++] = threeDModel.x
            vertices2Array[verticesIndex++] = threeDModel.y
            vertices2Array[verticesIndex++] = threeDModel.z
        }

        val color1Array = arrayListOf<Float>()
        for (i in 0..latitudes) {
            var tColor = -0.5f
            val tColorIncrement = 1f / (longitudes + 1)
            for (j in 0..longitudes) {
                color1Array.add(1f)
                color1Array.add(abs(tColor) + 0.5f)
                color1Array.add(abs(tColor) + 0.5f)
                color1Array.add(0.5f)
                tColor += tColorIncrement
            }
        }

        val color2Array = arrayListOf<Float>()
        for (i in 0..latitudes) {
            var tColor = -0.5f
            val tColorIncrement = 1f / (longitudes + 1)
            for (j in 0..longitudes) {
                color2Array.add(abs(tColor) + 0.5f)
                color2Array.add(1f)
                color2Array.add(abs(tColor) + 0.5f)
                color2Array.add(0.5f)
                tColor += tColorIncrement
            }
        }

        GraphicUtils.translateFloatArrayVertices(vertices1Array, -3f, 8f, -2f)
        GraphicUtils.translateFloatArrayVertices(vertices2Array, 3f, 10f, 2f)

        //Sphere 1
        indexesSphere1Size = indexes1Array.size

        indexSphere1Buffer = IntBuffer.allocate(indexes1Array.size)
        indexSphere1Buffer?.put(indexes1Array.toIntArray())
        indexSphere1Buffer?.position(0)

        // initialize vertex byte buffer for shape coordinates
        val bb1: ByteBuffer =
            ByteBuffer.allocateDirect(vertices1Array.size * 4) // (# of coordinate values * 4 bytes per float)
        bb1.order(ByteOrder.nativeOrder())
        vertexSphere1Buffer = bb1.asFloatBuffer()
        vertexSphere1Buffer?.put(vertices1Array)
        vertexSphere1Buffer?.position(0)

        //initialize color byte buffer
        val cb1: ByteBuffer =
            ByteBuffer.allocateDirect(color1Array.size * 4) // (# of coordinate values * 4 bytes per float)
        cb1.order(ByteOrder.nativeOrder())
        colorSphere1Buffer = cb1.asFloatBuffer()
        colorSphere1Buffer?.put(color1Array.toFloatArray())
        colorSphere1Buffer?.position(0)

        //Sphere 2
        indexesSphere2Size = indexes2Array.size

        indexSphere2Buffer = IntBuffer.allocate(indexes2Array.size)
        indexSphere2Buffer?.put(indexes2Array.toIntArray())
        indexSphere2Buffer?.position(0)

        // initialize vertex byte buffer for shape coordinates
        val bb2: ByteBuffer =
            ByteBuffer.allocateDirect(vertices2Array.size * 4) // (# of coordinate values * 4 bytes per float)
        bb2.order(ByteOrder.nativeOrder())
        vertexSphere2Buffer = bb2.asFloatBuffer()
        vertexSphere2Buffer?.put(vertices2Array)
        vertexSphere2Buffer?.position(0)

        //initialize color byte buffer
        val cb2: ByteBuffer =
            ByteBuffer.allocateDirect(color2Array.size * 4) // (# of coordinate values * 4 bytes per float)
        cb2.order(ByteOrder.nativeOrder())
        colorSphere2Buffer = cb2.asFloatBuffer()
        colorSphere2Buffer?.put(color2Array.toFloatArray())
        colorSphere2Buffer?.position(0)

        //M
        indexMBuffer = IntBuffer.allocate(letterMIndices.size)
        indexMBuffer?.put(letterMIndices)
        indexMBuffer?.position(0)

        // initialize vertex byte buffer for shape coordinates
        val bb3: ByteBuffer =
            ByteBuffer.allocateDirect(letterMVertices.size * 4) // (# of coordinate values * 4 bytes per float)
        bb3.order(ByteOrder.nativeOrder())
        vertexMBuffer = bb3.asFloatBuffer()
        vertexMBuffer?.put(letterMVertices)
        vertexMBuffer?.position(0)

        //initialize color byte buffer
        val cb3: ByteBuffer =
            ByteBuffer.allocateDirect(letterMColor.size * 4) // (# of coordinate values * 4 bytes per float)
        cb3.order(ByteOrder.nativeOrder())
        colorMBuffer = cb3.asFloatBuffer()
        colorMBuffer?.put(letterMColor)
        colorMBuffer?.position(0)

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

        //Sphere 1
        //set the attribute of the vertex to point to the vertex buffer
        GLES32.glVertexAttribPointer(
            mPositionHandle, COORDINATES_PER_VERTEX,
            GLES32.GL_FLOAT, false, VERTEX_STRIDE, vertexSphere1Buffer
        )
        GLES32.glVertexAttribPointer(
            mColorHandle, COLOR_PER_VERTEX,
            GLES32.GL_FLOAT, false, COLOR_STRIDE, colorSphere1Buffer
        )
        // Draw the triangle
        GLES32.glDrawElements(
            GLES32.GL_TRIANGLES,
            indexesSphere1Size,
            GLES32.GL_UNSIGNED_INT,
            indexSphere1Buffer
        )

        //Sphere 2
        //set the attribute of the vertex to point to the vertex buffer
        GLES32.glVertexAttribPointer(
            mPositionHandle, COORDINATES_PER_VERTEX,
            GLES32.GL_FLOAT, false, VERTEX_STRIDE, vertexSphere2Buffer
        )
        GLES32.glVertexAttribPointer(
            mColorHandle, COLOR_PER_VERTEX,
            GLES32.GL_FLOAT, false, COLOR_STRIDE, colorSphere2Buffer
        )
        // Draw the triangle
        GLES32.glDrawElements(
            GLES32.GL_TRIANGLES,
            indexesSphere2Size,
            GLES32.GL_UNSIGNED_INT,
            indexSphere2Buffer
        )
    }

}