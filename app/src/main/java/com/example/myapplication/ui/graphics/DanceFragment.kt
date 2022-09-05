package com.example.myapplication.ui.graphics

import android.graphics.Paint
import android.os.Bundle
import android.view.View
import com.example.myapplication.databinding.FragmentDanceBinding
import com.example.myapplication.model.*
import com.example.myapplication.ui.BaseFragment
import com.example.myapplication.utils.GraphicUtils
import timber.log.Timber
import java.util.*

/**
 * Created by Athenriel on 9/1/2022
 */
class DanceFragment : BaseFragment<FragmentDanceBinding>(FragmentDanceBinding::inflate) {

    private var frame = -1
    private val timer = Timer()
    private var paused = false
    private var viewDestroyed = false
    private val initialCubeModel = ThreeDCubeModel().apply {
        setCoordinateList(
            listOf(
                ThreeDCoordinatesModel(-1.0, -1.0, -1.0),
                ThreeDCoordinatesModel(-1.0, -1.0, 1.0),
                ThreeDCoordinatesModel(-1.0, 1.0, -1.0),
                ThreeDCoordinatesModel(-1.0, 1.0, 1.0),
                ThreeDCoordinatesModel(1.0, -1.0, -1.0),
                ThreeDCoordinatesModel(1.0, -1.0, 1.0),
                ThreeDCoordinatesModel(1.0, 1.0, -1.0),
                ThreeDCoordinatesModel(1.0, 1.0, 1.0)
            )
        )
    }
    private val threeDCubeListModel = ThreeDCubeListModel()
    private val lettersCubeListModel = ThreeDCubeListModel()
    private val cubesByFrameList = mutableListOf<CubesByFrameModel>()
    private val lettersByFrameList = mutableListOf<CubesByFrameModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDestroyed = false
        setTimerTask()
        setInitialCubes()
        loadAllFrameTransformationsFirst()
        loadAllFrameTransformationsSecond()
    }

    override fun onResume() {
        super.onResume()
        paused = false
    }

    override fun onPause() {
        super.onPause()
        paused = true
    }

    private fun timeWarning(time: Long, method: String) {
        if (time >= 13) {
            Timber.d("dcheck %s warning time to complete %s", method, time)
        }
    }

    private fun setInitialCubes() {
        val time = System.currentTimeMillis()
        var cubeList = mutableListOf<ThreeDCubeModel>()

        //hashtag diagonal 1
        var cube = ThreeDCubeModel().apply {
            setCoordinateList(initialCubeModel.getCoordinateList())
        }
        cube.scale(10.0, 100.0, 10.0)
        cube.quaternionRotate(30f, 0.0, 0.0, 1.0)
        cube.translate(220.0, 120.0, 0.0)
        cube.setPaintColor(29, 155, 240)
        cube.setPaintStyle(Paint.Style.FILL_AND_STROKE)
        cubeList.add(cube)

        //hashtag diagonal 2
        cube = ThreeDCubeModel().apply {
            setCoordinateList(initialCubeModel.getCoordinateList())
        }
        cube.scale(10.0, 100.0, 10.0)
        cube.quaternionRotate(30f, 0.0, 0.0, 1.0)
        cube.translate(280.0, 120.0, 0.0)
        cube.setPaintColor(29, 155, 240)
        cube.setPaintStyle(Paint.Style.FILL_AND_STROKE)
        cubeList.add(cube)

        //hashtag horizontal 1
        cube = ThreeDCubeModel().apply {
            setCoordinateList(initialCubeModel.getCoordinateList())
        }
        cube.scale(100.0, 10.0, 10.0)
        cube.translate(245.0, 90.0, 0.0)
        cube.setPaintColor(29, 155, 240)
        cube.setPaintStyle(Paint.Style.FILL_AND_STROKE)
        cubeList.add(cube)

        //hashtag horizontal 2
        cube = ThreeDCubeModel().apply {
            setCoordinateList(initialCubeModel.getCoordinateList())
        }
        cube.scale(100.0, 10.0, 10.0)
        cube.translate(245.0, 150.0, 0.0)
        cube.setPaintColor(29, 155, 240)
        cube.setPaintStyle(Paint.Style.FILL_AND_STROKE)
        cubeList.add(cube)

        //F1 vertical
        cube = ThreeDCubeModel().apply {
            setCoordinateList(initialCubeModel.getCoordinateList())
        }
        cube.scale(10.0, 100.0, 10.0)
        cube.translate(400.0, 120.0, 0.0)
        cube.setPaintColor(29, 155, 240)
        cube.setPaintStyle(Paint.Style.FILL_AND_STROKE)
        cubeList.add(cube)

        //F1 horizontal 1
        cube = ThreeDCubeModel().apply {
            setCoordinateList(initialCubeModel.getCoordinateList())
        }
        cube.scale(50.0, 10.0, 10.0)
        cube.translate(440.0, 30.0, 0.0)
        cube.setPaintColor(29, 155, 240)
        cube.setPaintStyle(Paint.Style.FILL_AND_STROKE)
        cubeList.add(cube)

        //F1 horizontal 2
        cube = ThreeDCubeModel().apply {
            setCoordinateList(initialCubeModel.getCoordinateList())
        }
        cube.scale(50.0, 10.0, 10.0)
        cube.translate(440.0, 120.0, 0.0)
        cube.setPaintColor(29, 155, 240)
        cube.setPaintStyle(Paint.Style.FILL_AND_STROKE)
        cubeList.add(cube)

        //F2 vertical
        cube = ThreeDCubeModel().apply {
            setCoordinateList(initialCubeModel.getCoordinateList())
        }
        cube.scale(10.0, 100.0, 10.0)
        cube.translate(540.0, 120.0, 0.0)
        cube.setPaintColor(29, 155, 240)
        cube.setPaintStyle(Paint.Style.FILL_AND_STROKE)
        cubeList.add(cube)

        //F2 horizontal 1
        cube = ThreeDCubeModel().apply {
            setCoordinateList(initialCubeModel.getCoordinateList())
        }
        cube.scale(50.0, 10.0, 10.0)
        cube.translate(580.0, 30.0, 0.0)
        cube.setPaintColor(29, 155, 240)
        cube.setPaintStyle(Paint.Style.FILL_AND_STROKE)
        cubeList.add(cube)

        //F2 horizontal 2
        cube = ThreeDCubeModel().apply {
            setCoordinateList(initialCubeModel.getCoordinateList())
        }
        cube.scale(50.0, 10.0, 10.0)
        cube.translate(580.0, 120.0, 0.0)
        cube.setPaintColor(29, 155, 240)
        cube.setPaintStyle(Paint.Style.FILL_AND_STROKE)
        cubeList.add(cube)

        //X diagonal 1
        cube = ThreeDCubeModel().apply {
            setCoordinateList(initialCubeModel.getCoordinateList())
        }
        cube.scale(10.0, 110.0, 10.0)
        cube.quaternionRotate(30f, 0.0, 0.0, -1.0)
        cube.translate(710.0, 115.0, 0.0)
        cube.setPaintColor(29, 155, 240)
        cube.setPaintStyle(Paint.Style.FILL_AND_STROKE)
        cubeList.add(cube)

        //X diagonal 2
        cube = ThreeDCubeModel().apply {
            setCoordinateList(initialCubeModel.getCoordinateList())
        }
        cube.scale(10.0, 110.0, 10.0)
        cube.quaternionRotate(30f, 0.0, 0.0, 1.0)
        cube.translate(710.0, 115.0, 0.0)
        cube.setPaintColor(29, 155, 240)
        cube.setPaintStyle(Paint.Style.FILL_AND_STROKE)
        cubeList.add(cube)

        //I
        cube = ThreeDCubeModel().apply {
            setCoordinateList(initialCubeModel.getCoordinateList())
        }
        cube.scale(10.0, 100.0, 10.0)
        cube.translate(830.0, 120.0, 0.0)
        cube.setPaintColor(29, 155, 240)
        cube.setPaintStyle(Paint.Style.FILL_AND_STROKE)
        cubeList.add(cube)

        //V diagonal 1
        cube = ThreeDCubeModel().apply {
            setCoordinateList(initialCubeModel.getCoordinateList())
        }
        cube.scale(10.0, 100.0, 10.0)
        cube.quaternionRotate(25f, 0.0, 0.0, -1.0)
        cube.translate(930.0, 115.0, 0.0)
        cube.setPaintColor(29, 155, 240)
        cube.setPaintStyle(Paint.Style.FILL_AND_STROKE)
        cubeList.add(cube)

        //V diagonal 2
        cube = ThreeDCubeModel().apply {
            setCoordinateList(initialCubeModel.getCoordinateList())
        }
        cube.scale(10.0, 100.0, 10.0)
        cube.quaternionRotate(25f, 0.0, 0.0, 1.0)
        cube.translate(1010.0, 115.0, 0.0)
        cube.setPaintColor(29, 155, 240)
        cube.setPaintStyle(Paint.Style.FILL_AND_STROKE)
        cubeList.add(cube)

        lettersCubeListModel.setDataList(cubeList)

        cubeList = mutableListOf()
        //left ear
        cube = ThreeDCubeModel().apply {
            setCoordinateList(initialCubeModel.getCoordinateList())
        }
        cube.scale(10.0, 50.0, 15.0)
        cube.translate(490.0, 520.0, 10.0)
        cube.setPaintColor(72, 77, 45)
        cube.setPaintStyle(Paint.Style.FILL_AND_STROKE)
        cubeList.add(cube)

        //right ear
        cube = ThreeDCubeModel().apply {
            setCoordinateList(initialCubeModel.getCoordinateList())
        }
        cube.scale(10.0, 50.0, 15.0)
        cube.translate(550.0, 520.0, 10.0)
        cube.setPaintColor(72, 77, 45)
        cube.setPaintStyle(Paint.Style.FILL_AND_STROKE)
        cubeList.add(cube)

        //head
        cube = ThreeDCubeModel().apply {
            setCoordinateList(initialCubeModel.getCoordinateList())
        }
        cube.scale(40.0, 40.0, 40.0)
        cube.translate(520.0, 610.0, 10.0)
        cube.setPaintColor(72, 77, 45)
        cube.setPaintStyle(Paint.Style.FILL_AND_STROKE)
        cubeList.add(cube)

        //neck
        cube = ThreeDCubeModel().apply {
            setCoordinateList(initialCubeModel.getCoordinateList())
        }
        cube.scale(20.0, 15.0, 20.0)
        cube.translate(520.0, 665.0, 10.0)
        cube.setPaintColor(21, 17, 16)
        cube.setPaintStyle(Paint.Style.FILL_AND_STROKE)
        cubeList.add(cube)

        //torso
        cube = ThreeDCubeModel().apply {
            setCoordinateList(initialCubeModel.getCoordinateList())
        }
        cube.scale(80.0, 70.0, 40.0)
        cube.translate(520.0, 750.0, 10.0)
        cube.setPaintColor(91, 50, 37)
        cube.setPaintStyle(Paint.Style.FILL_AND_STROKE)
        cubeList.add(cube)

        //stomach
        cube = ThreeDCubeModel().apply {
            setCoordinateList(initialCubeModel.getCoordinateList())
        }
        cube.scale(60.0, 50.0, 30.0)
        cube.translate(520.0, 870.0, 10.0)
        cube.setPaintColor(91, 50, 37)
        cube.setPaintStyle(Paint.Style.FILL_AND_STROKE)
        cubeList.add(cube)

        //waist
        cube = ThreeDCubeModel().apply {
            setCoordinateList(initialCubeModel.getCoordinateList())
        }
        cube.scale(70.0, 50.0, 30.0)
        cube.translate(520.0, 970.0, 10.0)
        cube.setPaintColor(105, 101, 97)
        cube.setPaintStyle(Paint.Style.FILL_AND_STROKE)
        cubeList.add(cube)

        //left shoulder
        cube = ThreeDCubeModel().apply {
            setCoordinateList(initialCubeModel.getCoordinateList())
        }
        cube.scale(30.0, 40.0, 30.0)
        cube.translate(410.0, 730.0, 10.0)
        cube.setPaintColor(91, 50, 37)
        cube.setPaintStyle(Paint.Style.FILL_AND_STROKE)
        cubeList.add(cube)

        //left arm
        cube = ThreeDCubeModel().apply {
            setCoordinateList(initialCubeModel.getCoordinateList())
        }
        cube.scale(20.0, 50.0, 30.0)
        cube.translate(410.0, 820.0, 10.0)
        cube.setPaintColor(105, 101, 97)
        cube.setPaintStyle(Paint.Style.FILL_AND_STROKE)
        cubeList.add(cube)

        //left elbow
        cube = ThreeDCubeModel().apply {
            setCoordinateList(initialCubeModel.getCoordinateList())
        }
        cube.scale(20.0, 20.0, 20.0)
        cube.translate(410.0, 890.0, 10.0)
        cube.setPaintColor(105, 101, 97)
        cube.setPaintStyle(Paint.Style.FILL_AND_STROKE)
        cubeList.add(cube)

        //left forearm
        cube = ThreeDCubeModel().apply {
            setCoordinateList(initialCubeModel.getCoordinateList())
        }
        cube.scale(20.0, 40.0, 20.0)
        cube.translate(410.0, 950.0, 10.0)
        cube.setPaintColor(105, 101, 97)
        cube.setPaintStyle(Paint.Style.FILL_AND_STROKE)
        cubeList.add(cube)

        //left hand
        cube = ThreeDCubeModel().apply {
            setCoordinateList(initialCubeModel.getCoordinateList())
        }
        cube.scale(20.0, 30.0, 20.0)
        cube.translate(410.0, 1020.0, 10.0)
        cube.setPaintColor(35, 20, 9)
        cube.setPaintStyle(Paint.Style.FILL_AND_STROKE)
        cubeList.add(cube)

        //right shoulder
        cube = ThreeDCubeModel().apply {
            setCoordinateList(initialCubeModel.getCoordinateList())
        }
        cube.scale(30.0, 40.0, 30.0)
        cube.translate(630.0, 730.0, 10.0)
        cube.setPaintColor(91, 50, 37)
        cube.setPaintStyle(Paint.Style.FILL_AND_STROKE)
        cubeList.add(cube)

        //right arm
        cube = ThreeDCubeModel().apply {
            setCoordinateList(initialCubeModel.getCoordinateList())
        }
        cube.scale(20.0, 50.0, 30.0)
        cube.translate(630.0, 820.0, 10.0)
        cube.setPaintColor(105, 101, 97)
        cube.setPaintStyle(Paint.Style.FILL_AND_STROKE)
        cubeList.add(cube)

        //right elbow
        cube = ThreeDCubeModel().apply {
            setCoordinateList(initialCubeModel.getCoordinateList())
        }
        cube.scale(20.0, 20.0, 20.0)
        cube.translate(630.0, 890.0, 10.0)
        cube.setPaintColor(105, 101, 97)
        cube.setPaintStyle(Paint.Style.FILL_AND_STROKE)
        cubeList.add(cube)

        //right forearm
        cube = ThreeDCubeModel().apply {
            setCoordinateList(initialCubeModel.getCoordinateList())
        }
        cube.scale(20.0, 40.0, 20.0)
        cube.translate(630.0, 950.0, 10.0)
        cube.setPaintColor(105, 101, 97)
        cube.setPaintStyle(Paint.Style.FILL_AND_STROKE)
        cubeList.add(cube)

        //right hand
        cube = ThreeDCubeModel().apply {
            setCoordinateList(initialCubeModel.getCoordinateList())
        }
        cube.scale(20.0, 30.0, 20.0)
        cube.translate(630.0, 1020.0, 10.0)
        cube.setPaintColor(35, 20, 9)
        cube.setPaintStyle(Paint.Style.FILL_AND_STROKE)
        cubeList.add(cube)

        //left femur
        cube = ThreeDCubeModel().apply {
            setCoordinateList(initialCubeModel.getCoordinateList())
        }
        cube.scale(30.0, 80.0, 30.0)
        cube.translate(485.0, 1100.0, 10.0)
        cube.setPaintColor(26, 17, 10)
        cube.setPaintStyle(Paint.Style.FILL_AND_STROKE)
        cubeList.add(cube)

        //left knee
        cube = ThreeDCubeModel().apply {
            setCoordinateList(initialCubeModel.getCoordinateList())
        }
        cube.scale(30.0, 30.0, 20.0)
        cube.translate(485.0, 1210.0, 10.0)
        cube.setPaintColor(26, 17, 10)
        cube.setPaintStyle(Paint.Style.FILL_AND_STROKE)
        cubeList.add(cube)

        //left leg
        cube = ThreeDCubeModel().apply {
            setCoordinateList(initialCubeModel.getCoordinateList())
        }
        cube.scale(25.0, 40.0, 15.0)
        cube.translate(485.0, 1280.0, 10.0)
        cube.setPaintColor(91, 50, 37)
        cube.setPaintStyle(Paint.Style.FILL_AND_STROKE)
        cubeList.add(cube)

        //left foot
        cube = ThreeDCubeModel().apply {
            setCoordinateList(initialCubeModel.getCoordinateList())
        }
        cube.scale(25.0, 30.0, 30.0)
        cube.translate(485.0, 1350.0, 20.0)
        cube.setPaintColor(91, 50, 37)
        cube.setPaintStyle(Paint.Style.FILL_AND_STROKE)
        cubeList.add(cube)

        //right femur
        cube = ThreeDCubeModel().apply {
            setCoordinateList(initialCubeModel.getCoordinateList())
        }
        cube.scale(30.0, 80.0, 30.0)
        cube.translate(555.0, 1100.0, 10.0)
        cube.setPaintColor(26, 17, 10)
        cube.setPaintStyle(Paint.Style.FILL_AND_STROKE)
        cubeList.add(cube)

        //right knee
        cube = ThreeDCubeModel().apply {
            setCoordinateList(initialCubeModel.getCoordinateList())
        }
        cube.scale(30.0, 30.0, 20.0)
        cube.translate(555.0, 1210.0, 10.0)
        cube.setPaintColor(26, 17, 10)
        cube.setPaintStyle(Paint.Style.FILL_AND_STROKE)
        cubeList.add(cube)

        //right leg
        cube = ThreeDCubeModel().apply {
            setCoordinateList(initialCubeModel.getCoordinateList())
        }
        cube.scale(25.0, 40.0, 15.0)
        cube.translate(555.0, 1280.0, 10.0)
        cube.setPaintColor(91, 50, 37)
        cube.setPaintStyle(Paint.Style.FILL_AND_STROKE)
        cubeList.add(cube)

        //right foot
        cube = ThreeDCubeModel().apply {
            setCoordinateList(initialCubeModel.getCoordinateList())
        }
        cube.scale(25.0, 30.0, 30.0)
        cube.translate(555.0, 1350.0, 20.0)
        cube.setPaintColor(91, 50, 37)
        cube.setPaintStyle(Paint.Style.FILL_AND_STROKE)
        cubeList.add(cube)

        threeDCubeListModel.setDataList(cubeList)
        timeWarning(System.currentTimeMillis() - time, "setInitialCubes")
    }

    private fun loadAllFrameTransformationsFirst() {
        val time = System.currentTimeMillis()
        var transformationList = mutableListOf<CubeTransformationListByFrameModel>()

        //letters
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //hashtag diagonal 1
                        CubeTransformationByFrameModel(
                            30f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            1.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            30f,
                            0.0,
                            0.0,
                            1.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //hashtag diagonal 2
                        CubeTransformationByFrameModel(
                            30f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            1.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            30f,
                            0.0,
                            0.0,
                            1.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //hashtag horizontal 1
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //hashtag horizontal 2
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //F1 vertical
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            1.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //F1 horizontal 1
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //F1 horizontal 2
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //F2 vertical
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            1.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //F2 horizontal 1
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //F2 horizontal 2
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //X diagonal 1
                        CubeTransformationByFrameModel(
                            30f,
                            0.0,
                            0.0,
                            1.0
                        ),
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            -1.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            30f,
                            0.0,
                            0.0,
                            -1.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //X diagonal 2
                        CubeTransformationByFrameModel(
                            30f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            1.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            30f,
                            0.0,
                            0.0,
                            1.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //I
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            1.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //V diagonal 1
                        CubeTransformationByFrameModel(
                            25f,
                            0.0,
                            0.0,
                            1.0
                        ),
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            -1.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            25f,
                            0.0,
                            0.0,
                            -1.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //V diagonal 2
                        CubeTransformationByFrameModel(
                            25f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            1.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            25f,
                            0.0,
                            0.0,
                            1.0
                        )
                    )
                )
            }
        )
        lettersByFrameList.add(
            CubesByFrameModel(
                0,
                transformationList
            )
        )

        //frame 0
        transformationList = mutableListOf()
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left ear
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right ear
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //head
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //neck
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //torso
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //stomach
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //waist
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left shoulder
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left arm
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left elbow
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left forearm
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left hand
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right shoulder
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right arm
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right elbow
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right forearm
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right hand
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left femur
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left knee
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left leg
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left foot
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right femur
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right knee
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right leg
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right foot
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        cubesByFrameList.add(
            CubesByFrameModel(
                0,
                transformationList
            )
        )

        //frame 1
        transformationList = mutableListOf()
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left ear
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            1.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right ear
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            1.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //head
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            1.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //neck
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            1.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //torso
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            1.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //stomach
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            1.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //waist
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            1.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left shoulder
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            1.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left arm
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            1.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left elbow
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            1.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left forearm
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            1.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left hand
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            1.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right shoulder
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            1.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right arm
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            1.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right elbow
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            1.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right forearm
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            1.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right hand
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            1.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left femur
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            1.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left knee
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            1.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left leg
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            1.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left foot
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            1.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right femur
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            1.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right knee
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            1.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right leg
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            1.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right foot
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            1.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        )
                    )
                )
            }
        )
        cubesByFrameList.add(
            CubesByFrameModel(
                1,
                transformationList
            )
        )

        //frame 2
        transformationList = mutableListOf()
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left ear
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -2.0,
                            1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right ear
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -2.0,
                            1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //head
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            -1.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //neck
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            -1.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //torso
                        CubeTransformationByFrameModel(
                            2f,
                            0.0,
                            -1.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //stomach
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            -1.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //waist
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            -1.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left shoulder
                        CubeTransformationByFrameModel(
                            2f,
                            0.0,
                            -1.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            1.0,
                            0.0,
                            2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left arm
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            -1.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        ),
                        CubeTransformationByFrameModel(
                            2f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            2.0,
                            0.0,
                            3.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left elbow
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            -1.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        ),
                        CubeTransformationByFrameModel(
                            2f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            3.0,
                            0.0,
                            3.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left forearm
                        CubeTransformationByFrameModel(
                            2f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            3.0,
                            0.0,
                            3.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left hand
                        CubeTransformationByFrameModel(
                            2f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            2f,
                            0.0,
                            1.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            3.0,
                            0.0,
                            3.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right shoulder
                        CubeTransformationByFrameModel(
                            2f,
                            0.0,
                            -1.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -2.0,
                            0.0,
                            2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right arm
                        CubeTransformationByFrameModel(
                            2f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            -1.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -2.0,
                            0.0,
                            6.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right elbow
                        CubeTransformationByFrameModel(
                            2f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            -1.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -3.0,
                            0.0,
                            6.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right forearm
                        CubeTransformationByFrameModel(
                            25f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            25f,
                            0.0,
                            -1.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -15.0,
                            -12.0,
                            12.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right hand
                        CubeTransformationByFrameModel(
                            25f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            25f,
                            0.0,
                            -1.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -30.0,
                            -26.0,
                            12.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left femur
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            1.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left knee
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -1.0,
                            0.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left leg
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -2.0,
                            0.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left foot
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -3.0,
                            0.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right femur
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            -1.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right knee
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            2.0,
                            0.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right leg
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            3.0,
                            0.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right foot
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            4.0,
                            0.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        cubesByFrameList.add(
            CubesByFrameModel(
                2,
                transformationList
            )
        )

        //frame 3
        transformationList = mutableListOf()
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left ear
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -2.0,
                            1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right ear
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -2.0,
                            1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //head
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            -1.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //neck
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            -1.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //torso
                        CubeTransformationByFrameModel(
                            2f,
                            0.0,
                            -1.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //stomach
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            -1.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //waist
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            -1.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left shoulder
                        CubeTransformationByFrameModel(
                            2f,
                            0.0,
                            -1.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            1.0,
                            0.0,
                            2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left arm
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            -1.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        ),
                        CubeTransformationByFrameModel(
                            2f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            2.0,
                            0.0,
                            3.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left elbow
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            -1.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        ),
                        CubeTransformationByFrameModel(
                            2f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            3.0,
                            0.0,
                            3.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left forearm
                        CubeTransformationByFrameModel(
                            2f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            3.0,
                            0.0,
                            3.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left hand
                        CubeTransformationByFrameModel(
                            2f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            2f,
                            0.0,
                            1.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            3.0,
                            0.0,
                            3.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right shoulder
                        CubeTransformationByFrameModel(
                            2f,
                            0.0,
                            -1.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -2.0,
                            0.0,
                            2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right arm
                        CubeTransformationByFrameModel(
                            2f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            -1.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -2.0,
                            0.0,
                            6.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right elbow
                        CubeTransformationByFrameModel(
                            2f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            -1.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -3.0,
                            0.0,
                            6.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right forearm
                        CubeTransformationByFrameModel(
                            25f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            25f,
                            0.0,
                            -1.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -15.0,
                            -12.0,
                            12.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right hand
                        CubeTransformationByFrameModel(
                            25f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            25f,
                            0.0,
                            -1.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -30.0,
                            -26.0,
                            12.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left femur
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            1.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left knee
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -1.0,
                            0.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left leg
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -2.0,
                            0.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left foot
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -3.0,
                            0.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right femur
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            -1.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right knee
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            2.0,
                            0.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right leg
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            3.0,
                            0.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right foot
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            4.0,
                            0.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        cubesByFrameList.add(
            CubesByFrameModel(
                3,
                transformationList
            )
        )

        //frame 4
        transformationList = mutableListOf()
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left ear
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -2.0,
                            1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right ear
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -2.0,
                            1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //head
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            -1.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //neck
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            -1.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //torso
                        CubeTransformationByFrameModel(
                            2f,
                            0.0,
                            -1.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //stomach
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            -1.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //waist
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            -1.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left shoulder
                        CubeTransformationByFrameModel(
                            2f,
                            0.0,
                            -1.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            1.0,
                            0.0,
                            2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left arm
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            -1.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        ),
                        CubeTransformationByFrameModel(
                            2f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            2.0,
                            0.0,
                            3.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left elbow
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            -1.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        ),
                        CubeTransformationByFrameModel(
                            2f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            3.0,
                            0.0,
                            3.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left forearm
                        CubeTransformationByFrameModel(
                            2f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            3.0,
                            0.0,
                            3.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left hand
                        CubeTransformationByFrameModel(
                            2f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            2f,
                            0.0,
                            1.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            3.0,
                            0.0,
                            3.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right shoulder
                        CubeTransformationByFrameModel(
                            2f,
                            0.0,
                            -1.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -2.0,
                            0.0,
                            2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right arm
                        CubeTransformationByFrameModel(
                            2f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            -1.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -2.0,
                            0.0,
                            6.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right elbow
                        CubeTransformationByFrameModel(
                            2f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            -1.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -3.0,
                            0.0,
                            6.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right forearm
                        CubeTransformationByFrameModel(
                            25f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            25f,
                            0.0,
                            -1.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -15.0,
                            -12.0,
                            12.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right hand
                        CubeTransformationByFrameModel(
                            25f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            25f,
                            0.0,
                            -1.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -30.0,
                            -26.0,
                            12.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left femur
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            1.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left knee
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -1.0,
                            0.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left leg
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -2.0,
                            0.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left foot
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -3.0,
                            0.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right femur
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            -1.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right knee
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            2.0,
                            0.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right leg
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            3.0,
                            0.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right foot
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            4.0,
                            0.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        cubesByFrameList.add(
            CubesByFrameModel(
                4,
                transformationList
            )
        )

        //frame 5
        transformationList = mutableListOf()
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left ear
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -2.0,
                            1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right ear
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -2.0,
                            1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //head
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            -1.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //neck
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            -1.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //torso
                        CubeTransformationByFrameModel(
                            2f,
                            0.0,
                            -1.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //stomach
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            -1.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //waist
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            -1.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left shoulder
                        CubeTransformationByFrameModel(
                            2f,
                            0.0,
                            -1.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            1.0,
                            0.0,
                            2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left arm
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            -1.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        ),
                        CubeTransformationByFrameModel(
                            2f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            2.0,
                            0.0,
                            3.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left elbow
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            -1.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        ),
                        CubeTransformationByFrameModel(
                            2f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            3.0,
                            0.0,
                            3.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left forearm
                        CubeTransformationByFrameModel(
                            2f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            3.0,
                            0.0,
                            3.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left hand
                        CubeTransformationByFrameModel(
                            2f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            2f,
                            0.0,
                            1.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            3.0,
                            0.0,
                            3.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right shoulder
                        CubeTransformationByFrameModel(
                            2f,
                            0.0,
                            -1.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -2.0,
                            0.0,
                            2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right arm
                        CubeTransformationByFrameModel(
                            2f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            -1.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -2.0,
                            0.0,
                            6.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right elbow
                        CubeTransformationByFrameModel(
                            2f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            -1.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -3.0,
                            0.0,
                            6.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right forearm
                        CubeTransformationByFrameModel(
                            25f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            25f,
                            0.0,
                            -1.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -15.0,
                            -12.0,
                            12.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right hand
                        CubeTransformationByFrameModel(
                            25f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            25f,
                            0.0,
                            -1.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -30.0,
                            -26.0,
                            12.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left femur
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            1.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left knee
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -1.0,
                            0.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left leg
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -2.0,
                            0.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left foot
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -3.0,
                            0.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right femur
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            -1.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right knee
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            2.0,
                            0.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right leg
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            3.0,
                            0.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right foot
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            4.0,
                            0.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        cubesByFrameList.add(
            CubesByFrameModel(
                5,
                transformationList
            )
        )

        //frame 6
        transformationList = mutableListOf()
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left ear
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -2.0,
                            1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right ear
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -2.0,
                            1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //head
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            -1.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //neck
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            -1.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //torso
                        CubeTransformationByFrameModel(
                            2f,
                            0.0,
                            -1.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //stomach
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            -1.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //waist
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            -1.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left shoulder
                        CubeTransformationByFrameModel(
                            2f,
                            0.0,
                            -1.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            1.0,
                            0.0,
                            2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left arm
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            -1.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        ),
                        CubeTransformationByFrameModel(
                            2f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            2.0,
                            0.0,
                            3.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left elbow
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            -1.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        ),
                        CubeTransformationByFrameModel(
                            2f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            3.0,
                            0.0,
                            3.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left forearm
                        CubeTransformationByFrameModel(
                            2f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            3.0,
                            0.0,
                            3.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left hand
                        CubeTransformationByFrameModel(
                            2f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            2f,
                            0.0,
                            1.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            3.0,
                            0.0,
                            3.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right shoulder
                        CubeTransformationByFrameModel(
                            2f,
                            0.0,
                            -1.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -2.0,
                            0.0,
                            2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right arm
                        CubeTransformationByFrameModel(
                            2f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            -1.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -2.0,
                            0.0,
                            6.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right elbow
                        CubeTransformationByFrameModel(
                            2f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            -1.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -3.0,
                            0.0,
                            6.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right forearm
                        CubeTransformationByFrameModel(
                            25f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            25f,
                            0.0,
                            -1.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -15.0,
                            -12.0,
                            12.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right hand
                        CubeTransformationByFrameModel(
                            25f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            25f,
                            0.0,
                            -1.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -30.0,
                            -26.0,
                            12.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left femur
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            1.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left knee
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -1.0,
                            0.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left leg
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -2.0,
                            0.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left foot
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -3.0,
                            0.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right femur
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            -1.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right knee
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            2.0,
                            0.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right leg
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            3.0,
                            0.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right foot
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            4.0,
                            0.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        cubesByFrameList.add(
            CubesByFrameModel(
                6,
                transformationList
            )
        )

        //frame 7
        transformationList = mutableListOf()
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left ear
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            2.0,
                            -1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right ear
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            2.0,
                            -1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //head
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            1.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //neck
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            1.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //torso
                        CubeTransformationByFrameModel(
                            2f,
                            0.0,
                            1.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //stomach
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            1.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //waist
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            1.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left shoulder
                        CubeTransformationByFrameModel(
                            2f,
                            0.0,
                            1.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -1.0,
                            0.0,
                            -2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left arm
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            1.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        ),
                        CubeTransformationByFrameModel(
                            2f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -2.0,
                            0.0,
                            -3.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left elbow
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            1.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        ),
                        CubeTransformationByFrameModel(
                            2f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -3.0,
                            0.0,
                            -3.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left forearm
                        CubeTransformationByFrameModel(
                            2f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -3.0,
                            0.0,
                            -3.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left hand
                        CubeTransformationByFrameModel(
                            2f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            2f,
                            0.0,
                            -1.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -3.0,
                            0.0,
                            -3.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right shoulder
                        CubeTransformationByFrameModel(
                            2f,
                            0.0,
                            1.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            2.0,
                            0.0,
                            -2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right arm
                        CubeTransformationByFrameModel(
                            2f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            1.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            2.0,
                            0.0,
                            -6.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right elbow
                        CubeTransformationByFrameModel(
                            2f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            1.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            3.0,
                            0.0,
                            -6.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right forearm
                        CubeTransformationByFrameModel(
                            25f,
                            0.0,
                            1.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            25f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            15.0,
                            12.0,
                            -12.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right hand
                        CubeTransformationByFrameModel(
                            25f,
                            0.0,
                            1.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            25f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            30.0,
                            26.0,
                            -12.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left femur
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -1.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left knee
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            1.0,
                            0.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left leg
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            2.0,
                            0.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left foot
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            3.0,
                            0.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right femur
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            1.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right knee
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -2.0,
                            0.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right leg
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -3.0,
                            0.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right foot
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -4.0,
                            0.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        cubesByFrameList.add(
            CubesByFrameModel(
                7,
                transformationList
            )
        )

        //frame 8
        transformationList = mutableListOf()
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left ear
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            2.0,
                            -1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right ear
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            2.0,
                            -1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //head
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            1.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //neck
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            1.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //torso
                        CubeTransformationByFrameModel(
                            2f,
                            0.0,
                            1.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //stomach
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            1.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //waist
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            1.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left shoulder
                        CubeTransformationByFrameModel(
                            2f,
                            0.0,
                            1.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -1.0,
                            0.0,
                            -2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left arm
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            1.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        ),
                        CubeTransformationByFrameModel(
                            2f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -2.0,
                            0.0,
                            -3.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left elbow
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            1.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        ),
                        CubeTransformationByFrameModel(
                            2f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -3.0,
                            0.0,
                            -3.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left forearm
                        CubeTransformationByFrameModel(
                            2f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -3.0,
                            0.0,
                            -3.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left hand
                        CubeTransformationByFrameModel(
                            2f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            2f,
                            0.0,
                            -1.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -3.0,
                            0.0,
                            -3.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right shoulder
                        CubeTransformationByFrameModel(
                            2f,
                            0.0,
                            1.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            2.0,
                            0.0,
                            -2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right arm
                        CubeTransformationByFrameModel(
                            2f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            1.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            2.0,
                            0.0,
                            -6.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right elbow
                        CubeTransformationByFrameModel(
                            2f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            1.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            3.0,
                            0.0,
                            -6.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right forearm
                        CubeTransformationByFrameModel(
                            25f,
                            0.0,
                            1.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            25f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            15.0,
                            12.0,
                            -12.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right hand
                        CubeTransformationByFrameModel(
                            25f,
                            0.0,
                            1.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            25f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            30.0,
                            26.0,
                            -12.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left femur
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            -1.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left knee
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            1.0,
                            0.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left leg
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            2.0,
                            0.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left foot
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            3.0,
                            0.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right femur
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            1.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right knee
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -2.0,
                            0.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right leg
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -3.0,
                            0.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right foot
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -4.0,
                            0.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        cubesByFrameList.add(
            CubesByFrameModel(
                8,
                transformationList
            )
        )

        //frame 9
        transformationList = mutableListOf()
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left ear
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            2.0,
                            -1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right ear
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            2.0,
                            -1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //head
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            1.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //neck
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            1.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //torso
                        CubeTransformationByFrameModel(
                            2f,
                            0.0,
                            1.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //stomach
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            1.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //waist
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            1.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left shoulder
                        CubeTransformationByFrameModel(
                            2f,
                            0.0,
                            1.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -1.0,
                            0.0,
                            -2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left arm
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            1.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        ),
                        CubeTransformationByFrameModel(
                            2f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -2.0,
                            0.0,
                            -3.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left elbow
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            1.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        ),
                        CubeTransformationByFrameModel(
                            2f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -3.0,
                            0.0,
                            -3.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left forearm
                        CubeTransformationByFrameModel(
                            2f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -3.0,
                            0.0,
                            -3.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left hand
                        CubeTransformationByFrameModel(
                            2f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            2f,
                            0.0,
                            -1.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -3.0,
                            0.0,
                            -3.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right shoulder
                        CubeTransformationByFrameModel(
                            2f,
                            0.0,
                            1.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            2.0,
                            0.0,
                            -2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right arm
                        CubeTransformationByFrameModel(
                            2f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            1.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            2.0,
                            0.0,
                            -6.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right elbow
                        CubeTransformationByFrameModel(
                            2f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            1.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            3.0,
                            0.0,
                            -6.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right forearm
                        CubeTransformationByFrameModel(
                            25f,
                            0.0,
                            1.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            25f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            15.0,
                            12.0,
                            -12.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right hand
                        CubeTransformationByFrameModel(
                            25f,
                            0.0,
                            1.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            25f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            30.0,
                            26.0,
                            -12.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left femur
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -1.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left knee
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            1.0,
                            0.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left leg
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            2.0,
                            0.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left foot
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            3.0,
                            0.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right femur
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            1.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right knee
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -2.0,
                            0.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right leg
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -3.0,
                            0.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right foot
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -4.0,
                            0.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        cubesByFrameList.add(
            CubesByFrameModel(
                9,
                transformationList
            )
        )

        //frame 5
        transformationList = mutableListOf()
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left ear
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            2.0,
                            -1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right ear
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            2.0,
                            -1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //head
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            1.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //neck
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            1.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //torso
                        CubeTransformationByFrameModel(
                            2f,
                            0.0,
                            1.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //stomach
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            1.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //waist
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            1.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left shoulder
                        CubeTransformationByFrameModel(
                            2f,
                            0.0,
                            1.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -1.0,
                            0.0,
                            -2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left arm
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            1.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        ),
                        CubeTransformationByFrameModel(
                            2f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -2.0,
                            0.0,
                            -3.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left elbow
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            1.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        ),
                        CubeTransformationByFrameModel(
                            2f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -3.0,
                            0.0,
                            -3.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left forearm
                        CubeTransformationByFrameModel(
                            2f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -3.0,
                            0.0,
                            -3.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left hand
                        CubeTransformationByFrameModel(
                            2f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            2f,
                            0.0,
                            -1.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -3.0,
                            0.0,
                            -3.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right shoulder
                        CubeTransformationByFrameModel(
                            2f,
                            0.0,
                            1.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            2.0,
                            0.0,
                            -2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right arm
                        CubeTransformationByFrameModel(
                            2f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            1.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            2.0,
                            0.0,
                            -6.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right elbow
                        CubeTransformationByFrameModel(
                            2f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            1.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            3.0,
                            0.0,
                            -6.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right forearm
                        CubeTransformationByFrameModel(
                            25f,
                            0.0,
                            1.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            25f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            15.0,
                            12.0,
                            -12.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right hand
                        CubeTransformationByFrameModel(
                            25f,
                            0.0,
                            1.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            25f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            30.0,
                            26.0,
                            -12.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left femur
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            -1.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left knee
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            1.0,
                            0.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left leg
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            2.0,
                            0.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left foot
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            3.0,
                            0.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right femur
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            1.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right knee
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -2.0,
                            0.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right leg
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -3.0,
                            0.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right foot
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -4.0,
                            0.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        cubesByFrameList.add(
            CubesByFrameModel(
                10,
                transformationList
            )
        )

        //frame 11
        transformationList = mutableListOf()
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left ear
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            2.0,
                            -1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right ear
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            2.0,
                            -1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //head
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            1.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //neck
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            1.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //torso
                        CubeTransformationByFrameModel(
                            2f,
                            0.0,
                            1.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //stomach
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            1.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //waist
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            1.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left shoulder
                        CubeTransformationByFrameModel(
                            2f,
                            0.0,
                            1.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -1.0,
                            0.0,
                            -2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left arm
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            1.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        ),
                        CubeTransformationByFrameModel(
                            2f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -2.0,
                            0.0,
                            -3.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left elbow
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            1.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        ),
                        CubeTransformationByFrameModel(
                            2f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -3.0,
                            0.0,
                            -3.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left forearm
                        CubeTransformationByFrameModel(
                            2f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -3.0,
                            0.0,
                            -3.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left hand
                        CubeTransformationByFrameModel(
                            2f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            2f,
                            0.0,
                            -1.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -3.0,
                            0.0,
                            -3.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right shoulder
                        CubeTransformationByFrameModel(
                            2f,
                            0.0,
                            1.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            2.0,
                            0.0,
                            -2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right arm
                        CubeTransformationByFrameModel(
                            2f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            1.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            2.0,
                            0.0,
                            -6.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right elbow
                        CubeTransformationByFrameModel(
                            2f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            1.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            3.0,
                            0.0,
                            -6.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right forearm
                        CubeTransformationByFrameModel(
                            25f,
                            0.0,
                            1.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            25f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            15.0,
                            12.0,
                            -12.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right hand
                        CubeTransformationByFrameModel(
                            25f,
                            0.0,
                            1.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            25f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            30.0,
                            26.0,
                            -12.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left femur
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -1.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left knee
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            1.0,
                            0.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left leg
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            2.0,
                            0.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left foot
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            3.0,
                            0.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right femur
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            1.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right knee
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -2.0,
                            0.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right leg
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -3.0,
                            0.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right foot
                        CubeTransformationByFrameModel(
                            1f,
                            0.0,
                            0.0,
                            1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -4.0,
                            0.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        cubesByFrameList.add(
            CubesByFrameModel(
                11,
                transformationList
            )
        )

        //frame 12
        transformationList = mutableListOf()
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left ear
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right ear
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //head
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //neck
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //torso
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //stomach
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            1.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //waist
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left shoulder
                        CubeTransformationByFrameModel(
                            2f,
                            1.0,
                            0.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left arm
                        CubeTransformationByFrameModel(
                            2f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left elbow
                        CubeTransformationByFrameModel(
                            2f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            4.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left forearm
                        CubeTransformationByFrameModel(
                            4f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            9.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left hand
                        CubeTransformationByFrameModel(
                            4f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            -1.0,
                            16.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right shoulder
                        CubeTransformationByFrameModel(
                            2f,
                            1.0,
                            0.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right arm
                        CubeTransformationByFrameModel(
                            2f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right elbow
                        CubeTransformationByFrameModel(
                            2f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            4.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right forearm
                        CubeTransformationByFrameModel(
                            4f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            9.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right hand
                        CubeTransformationByFrameModel(
                            4f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            -1.0,
                            16.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left femur
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left knee
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left leg
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left foot
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right femur
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right knee
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right leg
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right foot
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        cubesByFrameList.add(
            CubesByFrameModel(
                12,
                transformationList
            )
        )

        //frame 13
        transformationList = mutableListOf()
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left ear
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right ear
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //head
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //neck
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //torso
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //stomach
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            1.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //waist
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left shoulder
                        CubeTransformationByFrameModel(
                            2f,
                            1.0,
                            0.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left arm
                        CubeTransformationByFrameModel(
                            2f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left elbow
                        CubeTransformationByFrameModel(
                            2f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            4.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left forearm
                        CubeTransformationByFrameModel(
                            4f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            9.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left hand
                        CubeTransformationByFrameModel(
                            4f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            -1.0,
                            16.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right shoulder
                        CubeTransformationByFrameModel(
                            2f,
                            1.0,
                            0.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right arm
                        CubeTransformationByFrameModel(
                            2f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right elbow
                        CubeTransformationByFrameModel(
                            2f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            4.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right forearm
                        CubeTransformationByFrameModel(
                            4f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            9.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right hand
                        CubeTransformationByFrameModel(
                            4f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            -1.0,
                            16.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left femur
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left knee
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left leg
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left foot
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right femur
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right knee
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right leg
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right foot
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        cubesByFrameList.add(
            CubesByFrameModel(
                13,
                transformationList
            )
        )

        //frame 14
        transformationList = mutableListOf()
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left ear
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right ear
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //head
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //neck
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //torso
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //stomach
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            1.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //waist
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left shoulder
                        CubeTransformationByFrameModel(
                            2f,
                            1.0,
                            0.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left arm
                        CubeTransformationByFrameModel(
                            2f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left elbow
                        CubeTransformationByFrameModel(
                            2f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            4.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left forearm
                        CubeTransformationByFrameModel(
                            4f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            9.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left hand
                        CubeTransformationByFrameModel(
                            4f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            -1.0,
                            16.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right shoulder
                        CubeTransformationByFrameModel(
                            2f,
                            1.0,
                            0.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right arm
                        CubeTransformationByFrameModel(
                            2f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right elbow
                        CubeTransformationByFrameModel(
                            2f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            4.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right forearm
                        CubeTransformationByFrameModel(
                            4f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            9.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right hand
                        CubeTransformationByFrameModel(
                            4f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            -1.0,
                            16.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left femur
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left knee
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left leg
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left foot
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right femur
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right knee
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right leg
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right foot
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        cubesByFrameList.add(
            CubesByFrameModel(
                14,
                transformationList
            )
        )

        //frame 15
        transformationList = mutableListOf()
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left ear
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right ear
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //head
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //neck
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //torso
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //stomach
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            1.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //waist
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left shoulder
                        CubeTransformationByFrameModel(
                            2f,
                            1.0,
                            0.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left arm
                        CubeTransformationByFrameModel(
                            2f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left elbow
                        CubeTransformationByFrameModel(
                            2f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            4.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left forearm
                        CubeTransformationByFrameModel(
                            4f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            9.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left hand
                        CubeTransformationByFrameModel(
                            4f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            -1.0,
                            16.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right shoulder
                        CubeTransformationByFrameModel(
                            2f,
                            1.0,
                            0.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right arm
                        CubeTransformationByFrameModel(
                            2f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right elbow
                        CubeTransformationByFrameModel(
                            2f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            4.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right forearm
                        CubeTransformationByFrameModel(
                            4f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            9.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right hand
                        CubeTransformationByFrameModel(
                            4f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            -1.0,
                            16.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left femur
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left knee
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left leg
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left foot
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right femur
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right knee
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right leg
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right foot
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        cubesByFrameList.add(
            CubesByFrameModel(
                15,
                transformationList
            )
        )

        //frame 16
        transformationList = mutableListOf()
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left ear
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right ear
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //head
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //neck
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //torso
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //stomach
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            1.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //waist
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left shoulder
                        CubeTransformationByFrameModel(
                            2f,
                            1.0,
                            0.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left arm
                        CubeTransformationByFrameModel(
                            2f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left elbow
                        CubeTransformationByFrameModel(
                            2f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            4.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left forearm
                        CubeTransformationByFrameModel(
                            4f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            9.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left hand
                        CubeTransformationByFrameModel(
                            4f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            -1.0,
                            16.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right shoulder
                        CubeTransformationByFrameModel(
                            2f,
                            1.0,
                            0.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right arm
                        CubeTransformationByFrameModel(
                            2f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right elbow
                        CubeTransformationByFrameModel(
                            2f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            4.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right forearm
                        CubeTransformationByFrameModel(
                            4f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            9.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right hand
                        CubeTransformationByFrameModel(
                            4f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            -1.0,
                            16.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left femur
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left knee
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left leg
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left foot
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right femur
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right knee
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right leg
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right foot
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        cubesByFrameList.add(
            CubesByFrameModel(
                16,
                transformationList
            )
        )

        //frame 17
        transformationList = mutableListOf()
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left ear
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            -1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right ear
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            -1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //head
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            -1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //neck
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            -1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //torso
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            -1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //stomach
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            -1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //waist
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            -1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left shoulder
                        CubeTransformationByFrameModel(
                            4f,
                            0.0,
                            0.0,
                            1.0,
                            GraphicUtils.RotationType.REFERENCE
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -4.0,
                            6.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left arm
                        CubeTransformationByFrameModel(
                            4f,
                            0.0,
                            0.0,
                            1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -6.0,
                            -3.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left elbow
                        CubeTransformationByFrameModel(
                            4f,
                            0.0,
                            0.0,
                            1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -11.0,
                            -4.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left forearm
                        CubeTransformationByFrameModel(
                            8f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -12.0,
                            -10.0,
                            8.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left hand
                        CubeTransformationByFrameModel(
                            8f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -12.0,
                            -16.0,
                            14.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right shoulder
                        CubeTransformationByFrameModel(
                            4f,
                            0.0,
                            0.0,
                            -1.0,
                            GraphicUtils.RotationType.REFERENCE
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            4.0,
                            6.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right arm
                        CubeTransformationByFrameModel(
                            4f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            6.0,
                            -3.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right elbow
                        CubeTransformationByFrameModel(
                            4f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            11.0,
                            -4.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right forearm
                        CubeTransformationByFrameModel(
                            8f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            12.0,
                            -10.0,
                            8.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right hand
                        CubeTransformationByFrameModel(
                            8f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            12.0,
                            -16.0,
                            14.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left femur
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left knee
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left leg
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left foot
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right femur
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right knee
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right leg
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right foot
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        cubesByFrameList.add(
            CubesByFrameModel(
                17,
                transformationList
            )
        )

        //frame 18
        transformationList = mutableListOf()
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left ear
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            -1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right ear
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            -1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //head
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            -1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //neck
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            -1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //torso
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            -1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //stomach
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            -1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //waist
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            -1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left shoulder
                        CubeTransformationByFrameModel(
                            4f,
                            0.0,
                            0.0,
                            1.0,
                            GraphicUtils.RotationType.REFERENCE
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -4.0,
                            6.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left arm
                        CubeTransformationByFrameModel(
                            4f,
                            0.0,
                            0.0,
                            1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -6.0,
                            -3.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left elbow
                        CubeTransformationByFrameModel(
                            4f,
                            0.0,
                            0.0,
                            1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -11.0,
                            -4.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left forearm
                        CubeTransformationByFrameModel(
                            8f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -12.0,
                            -10.0,
                            8.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left hand
                        CubeTransformationByFrameModel(
                            8f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -12.0,
                            -16.0,
                            14.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right shoulder
                        CubeTransformationByFrameModel(
                            4f,
                            0.0,
                            0.0,
                            -1.0,
                            GraphicUtils.RotationType.REFERENCE
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            4.0,
                            6.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right arm
                        CubeTransformationByFrameModel(
                            4f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            6.0,
                            -3.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right elbow
                        CubeTransformationByFrameModel(
                            4f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            11.0,
                            -4.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right forearm
                        CubeTransformationByFrameModel(
                            8f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            12.0,
                            -10.0,
                            8.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right hand
                        CubeTransformationByFrameModel(
                            8f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            12.0,
                            -16.0,
                            14.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left femur
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left knee
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left leg
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left foot
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right femur
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right knee
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right leg
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right foot
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        cubesByFrameList.add(
            CubesByFrameModel(
                18,
                transformationList
            )
        )

        //frame 19
        transformationList = mutableListOf()
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left ear
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            -1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right ear
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            -1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //head
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            -1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //neck
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            -1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //torso
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            -1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //stomach
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            -1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //waist
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            -1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left shoulder
                        CubeTransformationByFrameModel(
                            4f,
                            0.0,
                            0.0,
                            1.0,
                            GraphicUtils.RotationType.REFERENCE
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -4.0,
                            6.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left arm
                        CubeTransformationByFrameModel(
                            4f,
                            0.0,
                            0.0,
                            1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -6.0,
                            -3.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left elbow
                        CubeTransformationByFrameModel(
                            4f,
                            0.0,
                            0.0,
                            1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -11.0,
                            -4.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left forearm
                        CubeTransformationByFrameModel(
                            8f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -12.0,
                            -10.0,
                            8.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left hand
                        CubeTransformationByFrameModel(
                            8f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -12.0,
                            -16.0,
                            14.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right shoulder
                        CubeTransformationByFrameModel(
                            4f,
                            0.0,
                            0.0,
                            -1.0,
                            GraphicUtils.RotationType.REFERENCE
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            4.0,
                            6.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right arm
                        CubeTransformationByFrameModel(
                            4f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            6.0,
                            -3.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right elbow
                        CubeTransformationByFrameModel(
                            4f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            11.0,
                            -4.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right forearm
                        CubeTransformationByFrameModel(
                            8f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            12.0,
                            -10.0,
                            8.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right hand
                        CubeTransformationByFrameModel(
                            8f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            12.0,
                            -16.0,
                            14.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left femur
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left knee
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left leg
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left foot
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right femur
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right knee
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right leg
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right foot
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        cubesByFrameList.add(
            CubesByFrameModel(
                19,
                transformationList
            )
        )

        //frame 20
        transformationList = mutableListOf()
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left ear
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            -1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right ear
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            -1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //head
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            -1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //neck
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            -1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //torso
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            -1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //stomach
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            -1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //waist
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            -1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left shoulder
                        CubeTransformationByFrameModel(
                            4f,
                            0.0,
                            0.0,
                            1.0,
                            GraphicUtils.RotationType.REFERENCE
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -4.0,
                            6.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left arm
                        CubeTransformationByFrameModel(
                            4f,
                            0.0,
                            0.0,
                            1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -6.0,
                            -3.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left elbow
                        CubeTransformationByFrameModel(
                            4f,
                            0.0,
                            0.0,
                            1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -11.0,
                            -4.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left forearm
                        CubeTransformationByFrameModel(
                            8f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -12.0,
                            -10.0,
                            8.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left hand
                        CubeTransformationByFrameModel(
                            8f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -12.0,
                            -16.0,
                            14.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right shoulder
                        CubeTransformationByFrameModel(
                            4f,
                            0.0,
                            0.0,
                            -1.0,
                            GraphicUtils.RotationType.REFERENCE
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            4.0,
                            6.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right arm
                        CubeTransformationByFrameModel(
                            4f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            6.0,
                            -3.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right elbow
                        CubeTransformationByFrameModel(
                            4f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            11.0,
                            -4.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right forearm
                        CubeTransformationByFrameModel(
                            8f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            12.0,
                            -10.0,
                            8.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right hand
                        CubeTransformationByFrameModel(
                            8f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            12.0,
                            -16.0,
                            14.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left femur
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left knee
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left leg
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left foot
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right femur
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right knee
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right leg
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right foot
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        cubesByFrameList.add(
            CubesByFrameModel(
                20,
                transformationList
            )
        )

        //frame 21
        transformationList = mutableListOf()
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left ear
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            -1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right ear
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            -1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //head
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            -1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //neck
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            -1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //torso
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            -1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //stomach
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            -1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //waist
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            -1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left shoulder
                        CubeTransformationByFrameModel(
                            4f,
                            0.0,
                            0.0,
                            1.0,
                            GraphicUtils.RotationType.REFERENCE
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -4.0,
                            6.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left arm
                        CubeTransformationByFrameModel(
                            4f,
                            0.0,
                            0.0,
                            1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -6.0,
                            -3.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left elbow
                        CubeTransformationByFrameModel(
                            4f,
                            0.0,
                            0.0,
                            1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -11.0,
                            -4.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left forearm
                        CubeTransformationByFrameModel(
                            8f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -12.0,
                            -10.0,
                            8.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left hand
                        CubeTransformationByFrameModel(
                            8f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -12.0,
                            -16.0,
                            14.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right shoulder
                        CubeTransformationByFrameModel(
                            4f,
                            0.0,
                            0.0,
                            -1.0,
                            GraphicUtils.RotationType.REFERENCE
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            4.0,
                            6.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right arm
                        CubeTransformationByFrameModel(
                            4f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            6.0,
                            -3.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right elbow
                        CubeTransformationByFrameModel(
                            4f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            11.0,
                            -4.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right forearm
                        CubeTransformationByFrameModel(
                            8f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            12.0,
                            -10.0,
                            8.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right hand
                        CubeTransformationByFrameModel(
                            8f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            12.0,
                            -16.0,
                            14.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left femur
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left knee
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left leg
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left foot
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right femur
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right knee
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right leg
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right foot
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        cubesByFrameList.add(
            CubesByFrameModel(
                21,
                transformationList
            )
        )

        //frame 22
        transformationList = mutableListOf()
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left ear
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right ear
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //head
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //neck
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //torso
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //stomach
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //waist
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left shoulder
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left arm
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left elbow
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left forearm
                        CubeTransformationByFrameModel(
                            8f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            -8.0,
                            2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left hand
                        CubeTransformationByFrameModel(
                            8f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            -18.0,
                            4.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right shoulder
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right arm
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right elbow
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right forearm
                        CubeTransformationByFrameModel(
                            8f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            -8.0,
                            2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right hand
                        CubeTransformationByFrameModel(
                            8f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            -18.0,
                            4.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left femur
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left knee
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left leg
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left foot
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right femur
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right knee
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right leg
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right foot
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        cubesByFrameList.add(
            CubesByFrameModel(
                22,
                transformationList
            )
        )

        //frame 23
        transformationList = mutableListOf()
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left ear
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right ear
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //head
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //neck
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //torso
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //stomach
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //waist
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left shoulder
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left arm
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left elbow
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left forearm
                        CubeTransformationByFrameModel(
                            8f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            -8.0,
                            2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left hand
                        CubeTransformationByFrameModel(
                            8f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            -18.0,
                            4.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right shoulder
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right arm
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right elbow
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right forearm
                        CubeTransformationByFrameModel(
                            8f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            -8.0,
                            2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right hand
                        CubeTransformationByFrameModel(
                            8f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            -18.0,
                            4.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left femur
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left knee
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left leg
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left foot
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right femur
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right knee
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right leg
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right foot
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        cubesByFrameList.add(
            CubesByFrameModel(
                23,
                transformationList
            )
        )

        //frame 24
        transformationList = mutableListOf()
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left ear
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right ear
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //head
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //neck
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //torso
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //stomach
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //waist
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left shoulder
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left arm
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left elbow
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left forearm
                        CubeTransformationByFrameModel(
                            8f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            -8.0,
                            2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left hand
                        CubeTransformationByFrameModel(
                            8f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            -18.0,
                            4.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right shoulder
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right arm
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right elbow
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right forearm
                        CubeTransformationByFrameModel(
                            8f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            -8.0,
                            2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right hand
                        CubeTransformationByFrameModel(
                            8f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            -18.0,
                            4.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left femur
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left knee
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left leg
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left foot
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right femur
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right knee
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right leg
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right foot
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        cubesByFrameList.add(
            CubesByFrameModel(
                24,
                transformationList
            )
        )

        //frame 25
        transformationList = mutableListOf()
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left ear
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right ear
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //head
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //neck
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //torso
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //stomach
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //waist
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left shoulder
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left arm
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left elbow
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left forearm
                        CubeTransformationByFrameModel(
                            8f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            -8.0,
                            2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left hand
                        CubeTransformationByFrameModel(
                            8f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            -18.0,
                            4.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right shoulder
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right arm
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right elbow
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right forearm
                        CubeTransformationByFrameModel(
                            8f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            -8.0,
                            2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right hand
                        CubeTransformationByFrameModel(
                            8f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            -18.0,
                            4.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left femur
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left knee
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left leg
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left foot
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right femur
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right knee
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right leg
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right foot
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        cubesByFrameList.add(
            CubesByFrameModel(
                25,
                transformationList
            )
        )

        //frame 26
        transformationList = mutableListOf()
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left ear
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right ear
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //head
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //neck
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //torso
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //stomach
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //waist
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left shoulder
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left arm
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left elbow
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left forearm
                        CubeTransformationByFrameModel(
                            8f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            -8.0,
                            2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left hand
                        CubeTransformationByFrameModel(
                            8f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            -18.0,
                            4.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right shoulder
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right arm
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right elbow
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right forearm
                        CubeTransformationByFrameModel(
                            8f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            -8.0,
                            2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right hand
                        CubeTransformationByFrameModel(
                            8f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            -18.0,
                            4.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left femur
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left knee
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left leg
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left foot
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right femur
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right knee
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right leg
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right foot
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        cubesByFrameList.add(
            CubesByFrameModel(
                26,
                transformationList
            )
        )

        timeWarning(System.currentTimeMillis() - time, "loadAllFrameTransformations1")
    }

    private fun loadAllFrameTransformationsSecond() {
        val time = System.currentTimeMillis()

        var transformationList = mutableListOf<CubeTransformationListByFrameModel>()

        //frame 27
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left ear
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right ear
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //head
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //neck
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //torso
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //stomach
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //waist
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left shoulder
                        CubeTransformationByFrameModel(
                            4f,
                            1.0,
                            0.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left arm
                        CubeTransformationByFrameModel(
                            4f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            4.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left elbow
                        CubeTransformationByFrameModel(
                            4f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            8.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left forearm
                        CubeTransformationByFrameModel(
                            4f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            4f,
                            0.0,
                            0.0,
                            1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            2.0,
                            -4.0,
                            4.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left hand
                        CubeTransformationByFrameModel(
                            4f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            4f,
                            0.0,
                            0.0,
                            1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            4.0,
                            -8.0,
                            2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right shoulder
                        CubeTransformationByFrameModel(
                            4f,
                            1.0,
                            0.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right arm
                        CubeTransformationByFrameModel(
                            4f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            4.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right elbow
                        CubeTransformationByFrameModel(
                            4f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            8.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right forearm
                        CubeTransformationByFrameModel(
                            4f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            4f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -2.0,
                            -4.0,
                            4.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right hand
                        CubeTransformationByFrameModel(
                            4f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            4f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -4.0,
                            -8.0,
                            2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left femur
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left knee
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left leg
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left foot
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right femur
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right knee
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right leg
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right foot
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        cubesByFrameList.add(
            CubesByFrameModel(
                27,
                transformationList
            )
        )

        //frame 28
        transformationList = mutableListOf()
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left ear
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right ear
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //head
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //neck
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //torso
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //stomach
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //waist
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left shoulder
                        CubeTransformationByFrameModel(
                            4f,
                            1.0,
                            0.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left arm
                        CubeTransformationByFrameModel(
                            4f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            4.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left elbow
                        CubeTransformationByFrameModel(
                            4f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            8.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left forearm
                        CubeTransformationByFrameModel(
                            4f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            4f,
                            0.0,
                            0.0,
                            1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            2.0,
                            -4.0,
                            4.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left hand
                        CubeTransformationByFrameModel(
                            4f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            4f,
                            0.0,
                            0.0,
                            1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            4.0,
                            -8.0,
                            2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right shoulder
                        CubeTransformationByFrameModel(
                            4f,
                            1.0,
                            0.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right arm
                        CubeTransformationByFrameModel(
                            4f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            4.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right elbow
                        CubeTransformationByFrameModel(
                            4f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            8.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right forearm
                        CubeTransformationByFrameModel(
                            4f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            4f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -2.0,
                            -4.0,
                            4.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right hand
                        CubeTransformationByFrameModel(
                            4f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            4f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -4.0,
                            -8.0,
                            2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left femur
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left knee
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left leg
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left foot
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right femur
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right knee
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right leg
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right foot
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        cubesByFrameList.add(
            CubesByFrameModel(
                28,
                transformationList
            )
        )

        //frame 29
        transformationList = mutableListOf()
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left ear
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right ear
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //head
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //neck
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //torso
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //stomach
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //waist
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left shoulder
                        CubeTransformationByFrameModel(
                            4f,
                            1.0,
                            0.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left arm
                        CubeTransformationByFrameModel(
                            4f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            4.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left elbow
                        CubeTransformationByFrameModel(
                            4f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            8.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left forearm
                        CubeTransformationByFrameModel(
                            4f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            4f,
                            0.0,
                            0.0,
                            1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            2.0,
                            -4.0,
                            4.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left hand
                        CubeTransformationByFrameModel(
                            4f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            4f,
                            0.0,
                            0.0,
                            1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            4.0,
                            -8.0,
                            2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right shoulder
                        CubeTransformationByFrameModel(
                            4f,
                            1.0,
                            0.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right arm
                        CubeTransformationByFrameModel(
                            4f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            4.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right elbow
                        CubeTransformationByFrameModel(
                            4f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            8.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right forearm
                        CubeTransformationByFrameModel(
                            4f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            4f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -2.0,
                            -4.0,
                            4.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right hand
                        CubeTransformationByFrameModel(
                            4f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            4f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -4.0,
                            -8.0,
                            2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left femur
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left knee
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left leg
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left foot
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right femur
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right knee
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right leg
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right foot
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        cubesByFrameList.add(
            CubesByFrameModel(
                29,
                transformationList
            )
        )

        //frame 30
        transformationList = mutableListOf()
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left ear
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right ear
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //head
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //neck
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //torso
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //stomach
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //waist
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left shoulder
                        CubeTransformationByFrameModel(
                            4f,
                            1.0,
                            0.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left arm
                        CubeTransformationByFrameModel(
                            4f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            4.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left elbow
                        CubeTransformationByFrameModel(
                            4f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            8.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left forearm
                        CubeTransformationByFrameModel(
                            4f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            4f,
                            0.0,
                            0.0,
                            1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            2.0,
                            -4.0,
                            4.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left hand
                        CubeTransformationByFrameModel(
                            4f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            4f,
                            0.0,
                            0.0,
                            1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            4.0,
                            -8.0,
                            2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right shoulder
                        CubeTransformationByFrameModel(
                            4f,
                            1.0,
                            0.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right arm
                        CubeTransformationByFrameModel(
                            4f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            4.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right elbow
                        CubeTransformationByFrameModel(
                            4f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            8.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right forearm
                        CubeTransformationByFrameModel(
                            4f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            4f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -2.0,
                            -4.0,
                            4.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right hand
                        CubeTransformationByFrameModel(
                            4f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            4f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -4.0,
                            -8.0,
                            2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left femur
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left knee
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left leg
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left foot
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right femur
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right knee
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right leg
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right foot
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        cubesByFrameList.add(
            CubesByFrameModel(
                30,
                transformationList
            )
        )

        //frame 31
        transformationList = mutableListOf()
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left ear
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right ear
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //head
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //neck
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //torso
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //stomach
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //waist
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left shoulder
                        CubeTransformationByFrameModel(
                            4f,
                            1.0,
                            0.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left arm
                        CubeTransformationByFrameModel(
                            4f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            4.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left elbow
                        CubeTransformationByFrameModel(
                            4f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            8.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left forearm
                        CubeTransformationByFrameModel(
                            4f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            4f,
                            0.0,
                            0.0,
                            1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            2.0,
                            -4.0,
                            4.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left hand
                        CubeTransformationByFrameModel(
                            4f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            4f,
                            0.0,
                            0.0,
                            1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            4.0,
                            -8.0,
                            2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right shoulder
                        CubeTransformationByFrameModel(
                            4f,
                            1.0,
                            0.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right arm
                        CubeTransformationByFrameModel(
                            4f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            4.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right elbow
                        CubeTransformationByFrameModel(
                            4f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            8.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right forearm
                        CubeTransformationByFrameModel(
                            4f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            4f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -2.0,
                            -4.0,
                            4.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right hand
                        CubeTransformationByFrameModel(
                            4f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            4f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -4.0,
                            -8.0,
                            2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left femur
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left knee
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left leg
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left foot
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right femur
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right knee
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right leg
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right foot
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        cubesByFrameList.add(
            CubesByFrameModel(
                31,
                transformationList
            )
        )

        //frame 32
        transformationList = mutableListOf()
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left ear
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right ear
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //head
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //neck
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //torso
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //stomach
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //waist
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left shoulder
                        CubeTransformationByFrameModel(
                            4f,
                            -1.0,
                            0.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left arm
                        CubeTransformationByFrameModel(
                            4f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -4.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left elbow
                        CubeTransformationByFrameModel(
                            4f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -8.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left forearm
                        CubeTransformationByFrameModel(
                            4f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            4f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -2.0,
                            4.0,
                            -4.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left hand
                        CubeTransformationByFrameModel(
                            4f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            4f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -4.0,
                            8.0,
                            -2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right shoulder
                        CubeTransformationByFrameModel(
                            4f,
                            -1.0,
                            0.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right arm
                        CubeTransformationByFrameModel(
                            4f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -4.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right elbow
                        CubeTransformationByFrameModel(
                            4f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -8.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right forearm
                        CubeTransformationByFrameModel(
                            4f,
                            0.0,
                            0.0,
                            1.0
                        ),
                        CubeTransformationByFrameModel(
                            4f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            2.0,
                            4.0,
                            -4.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right hand
                        CubeTransformationByFrameModel(
                            4f,
                            0.0,
                            0.0,
                            1.0
                        ),
                        CubeTransformationByFrameModel(
                            4f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            4.0,
                            8.0,
                            -2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left femur
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left knee
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left leg
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left foot
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right femur
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right knee
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right leg
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right foot
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        cubesByFrameList.add(
            CubesByFrameModel(
                32,
                transformationList
            )
        )

        //frame 33
        transformationList = mutableListOf()
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left ear
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right ear
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //head
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //neck
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //torso
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //stomach
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //waist
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left shoulder
                        CubeTransformationByFrameModel(
                            4f,
                            -1.0,
                            0.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left arm
                        CubeTransformationByFrameModel(
                            4f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -4.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left elbow
                        CubeTransformationByFrameModel(
                            4f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -8.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left forearm
                        CubeTransformationByFrameModel(
                            4f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            4f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -2.0,
                            4.0,
                            -4.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left hand
                        CubeTransformationByFrameModel(
                            4f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            4f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -4.0,
                            8.0,
                            -2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right shoulder
                        CubeTransformationByFrameModel(
                            4f,
                            -1.0,
                            0.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right arm
                        CubeTransformationByFrameModel(
                            4f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -4.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right elbow
                        CubeTransformationByFrameModel(
                            4f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -8.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right forearm
                        CubeTransformationByFrameModel(
                            4f,
                            0.0,
                            0.0,
                            1.0
                        ),
                        CubeTransformationByFrameModel(
                            4f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            2.0,
                            4.0,
                            -4.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right hand
                        CubeTransformationByFrameModel(
                            4f,
                            0.0,
                            0.0,
                            1.0
                        ),
                        CubeTransformationByFrameModel(
                            4f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            4.0,
                            8.0,
                            -2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left femur
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left knee
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left leg
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left foot
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right femur
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right knee
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right leg
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right foot
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        cubesByFrameList.add(
            CubesByFrameModel(
                33,
                transformationList
            )
        )

        //frame 34
        transformationList = mutableListOf()
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left ear
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right ear
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //head
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //neck
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //torso
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //stomach
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //waist
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left shoulder
                        CubeTransformationByFrameModel(
                            4f,
                            -1.0,
                            0.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left arm
                        CubeTransformationByFrameModel(
                            4f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -4.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left elbow
                        CubeTransformationByFrameModel(
                            4f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -8.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left forearm
                        CubeTransformationByFrameModel(
                            4f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            4f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -2.0,
                            4.0,
                            -4.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left hand
                        CubeTransformationByFrameModel(
                            4f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            4f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -4.0,
                            8.0,
                            -2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right shoulder
                        CubeTransformationByFrameModel(
                            4f,
                            -1.0,
                            0.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right arm
                        CubeTransformationByFrameModel(
                            4f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -4.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right elbow
                        CubeTransformationByFrameModel(
                            4f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -8.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right forearm
                        CubeTransformationByFrameModel(
                            4f,
                            0.0,
                            0.0,
                            1.0
                        ),
                        CubeTransformationByFrameModel(
                            4f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            2.0,
                            4.0,
                            -4.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right hand
                        CubeTransformationByFrameModel(
                            4f,
                            0.0,
                            0.0,
                            1.0
                        ),
                        CubeTransformationByFrameModel(
                            4f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            4.0,
                            8.0,
                            -2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left femur
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left knee
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left leg
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left foot
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right femur
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right knee
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right leg
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right foot
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        cubesByFrameList.add(
            CubesByFrameModel(
                34,
                transformationList
            )
        )

        //frame 35
        transformationList = mutableListOf()
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left ear
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right ear
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //head
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //neck
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //torso
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //stomach
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //waist
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left shoulder
                        CubeTransformationByFrameModel(
                            4f,
                            -1.0,
                            0.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left arm
                        CubeTransformationByFrameModel(
                            4f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -4.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left elbow
                        CubeTransformationByFrameModel(
                            4f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -8.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left forearm
                        CubeTransformationByFrameModel(
                            4f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            4f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -2.0,
                            4.0,
                            -4.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left hand
                        CubeTransformationByFrameModel(
                            4f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            4f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -4.0,
                            8.0,
                            -2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right shoulder
                        CubeTransformationByFrameModel(
                            4f,
                            -1.0,
                            0.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right arm
                        CubeTransformationByFrameModel(
                            4f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -4.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right elbow
                        CubeTransformationByFrameModel(
                            4f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -8.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right forearm
                        CubeTransformationByFrameModel(
                            4f,
                            0.0,
                            0.0,
                            1.0
                        ),
                        CubeTransformationByFrameModel(
                            4f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            2.0,
                            4.0,
                            -4.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right hand
                        CubeTransformationByFrameModel(
                            4f,
                            0.0,
                            0.0,
                            1.0
                        ),
                        CubeTransformationByFrameModel(
                            4f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            4.0,
                            8.0,
                            -2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left femur
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left knee
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left leg
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left foot
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right femur
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right knee
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right leg
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right foot
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        cubesByFrameList.add(
            CubesByFrameModel(
                35,
                transformationList
            )
        )

        //frame 36
        transformationList = mutableListOf()
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left ear
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right ear
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //head
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //neck
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //torso
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //stomach
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //waist
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left shoulder
                        CubeTransformationByFrameModel(
                            4f,
                            -1.0,
                            0.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left arm
                        CubeTransformationByFrameModel(
                            4f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -4.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left elbow
                        CubeTransformationByFrameModel(
                            4f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -8.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left forearm
                        CubeTransformationByFrameModel(
                            4f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            4f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -2.0,
                            4.0,
                            -4.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left hand
                        CubeTransformationByFrameModel(
                            4f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            4f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -4.0,
                            8.0,
                            -2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right shoulder
                        CubeTransformationByFrameModel(
                            4f,
                            -1.0,
                            0.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right arm
                        CubeTransformationByFrameModel(
                            4f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -4.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right elbow
                        CubeTransformationByFrameModel(
                            4f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -8.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right forearm
                        CubeTransformationByFrameModel(
                            4f,
                            0.0,
                            0.0,
                            1.0
                        ),
                        CubeTransformationByFrameModel(
                            4f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            2.0,
                            4.0,
                            -4.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right hand
                        CubeTransformationByFrameModel(
                            4f,
                            0.0,
                            0.0,
                            1.0
                        ),
                        CubeTransformationByFrameModel(
                            4f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            4.0,
                            8.0,
                            -2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left femur
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left knee
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left leg
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left foot
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right femur
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right knee
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right leg
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right foot
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        cubesByFrameList.add(
            CubesByFrameModel(
                36,
                transformationList
            )
        )

        //frame 37
        transformationList = mutableListOf()
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left ear
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right ear
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //head
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //neck
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //torso
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //stomach
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //waist
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left shoulder
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left arm
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left elbow
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left forearm
                        CubeTransformationByFrameModel(
                            8f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            8.0,
                            -2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left hand
                        CubeTransformationByFrameModel(
                            8f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            18.0,
                            -4.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right shoulder
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right arm
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right elbow
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right forearm
                        CubeTransformationByFrameModel(
                            8f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            8.0,
                            -2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right hand
                        CubeTransformationByFrameModel(
                            8f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            18.0,
                            -4.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left femur
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left knee
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left leg
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left foot
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right femur
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right knee
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right leg
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right foot
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        cubesByFrameList.add(
            CubesByFrameModel(
                37,
                transformationList
            )
        )

        //frame 38
        transformationList = mutableListOf()
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left ear
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right ear
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //head
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //neck
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //torso
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //stomach
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //waist
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left shoulder
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left arm
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left elbow
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left forearm
                        CubeTransformationByFrameModel(
                            8f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            8.0,
                            -2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left hand
                        CubeTransformationByFrameModel(
                            8f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            18.0,
                            -4.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right shoulder
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right arm
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right elbow
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right forearm
                        CubeTransformationByFrameModel(
                            8f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            8.0,
                            -2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right hand
                        CubeTransformationByFrameModel(
                            8f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            18.0,
                            -4.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left femur
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left knee
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left leg
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left foot
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right femur
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right knee
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right leg
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right foot
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        cubesByFrameList.add(
            CubesByFrameModel(
                38,
                transformationList
            )
        )

        //frame 39
        transformationList = mutableListOf()
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left ear
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right ear
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //head
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //neck
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //torso
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //stomach
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //waist
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left shoulder
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left arm
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left elbow
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left forearm
                        CubeTransformationByFrameModel(
                            8f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            8.0,
                            -2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left hand
                        CubeTransformationByFrameModel(
                            8f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            18.0,
                            -4.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right shoulder
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right arm
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right elbow
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right forearm
                        CubeTransformationByFrameModel(
                            8f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            8.0,
                            -2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right hand
                        CubeTransformationByFrameModel(
                            8f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            18.0,
                            -4.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left femur
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left knee
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left leg
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left foot
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right femur
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right knee
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right leg
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right foot
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        cubesByFrameList.add(
            CubesByFrameModel(
                39,
                transformationList
            )
        )

        //frame 40
        transformationList = mutableListOf()
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left ear
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right ear
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //head
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //neck
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //torso
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //stomach
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //waist
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left shoulder
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left arm
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left elbow
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left forearm
                        CubeTransformationByFrameModel(
                            8f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            8.0,
                            -2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left hand
                        CubeTransformationByFrameModel(
                            8f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            18.0,
                            -4.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right shoulder
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right arm
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right elbow
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right forearm
                        CubeTransformationByFrameModel(
                            8f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            8.0,
                            -2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right hand
                        CubeTransformationByFrameModel(
                            8f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            18.0,
                            -4.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left femur
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left knee
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left leg
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left foot
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right femur
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right knee
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right leg
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right foot
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        cubesByFrameList.add(
            CubesByFrameModel(
                40,
                transformationList
            )
        )

        //frame 41
        transformationList = mutableListOf()
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left ear
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right ear
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //head
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //neck
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //torso
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //stomach
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //waist
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left shoulder
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left arm
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left elbow
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left forearm
                        CubeTransformationByFrameModel(
                            8f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            8.0,
                            -2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left hand
                        CubeTransformationByFrameModel(
                            8f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            18.0,
                            -4.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right shoulder
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right arm
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right elbow
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right forearm
                        CubeTransformationByFrameModel(
                            8f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            8.0,
                            -2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right hand
                        CubeTransformationByFrameModel(
                            8f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            18.0,
                            -4.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left femur
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left knee
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left leg
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left foot
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right femur
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right knee
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right leg
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right foot
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        cubesByFrameList.add(
            CubesByFrameModel(
                41,
                transformationList
            )
        )

        //frame 42
        transformationList = mutableListOf()
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left ear
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right ear
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //head
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //neck
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //torso
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //stomach
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //waist
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left shoulder
                        CubeTransformationByFrameModel(
                            4f,
                            0.0,
                            0.0,
                            -1.0,
                            GraphicUtils.RotationType.REFERENCE
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            4.0,
                            -6.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left arm
                        CubeTransformationByFrameModel(
                            4f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            6.0,
                            3.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left elbow
                        CubeTransformationByFrameModel(
                            4f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            11.0,
                            4.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left forearm
                        CubeTransformationByFrameModel(
                            8f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            12.0,
                            10.0,
                            -8.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left hand
                        CubeTransformationByFrameModel(
                            8f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            12.0,
                            16.0,
                            -14.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right shoulder
                        CubeTransformationByFrameModel(
                            4f,
                            0.0,
                            0.0,
                            1.0,
                            GraphicUtils.RotationType.REFERENCE
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -4.0,
                            -6.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right arm
                        CubeTransformationByFrameModel(
                            4f,
                            0.0,
                            0.0,
                            1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -6.0,
                            3.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right elbow
                        CubeTransformationByFrameModel(
                            4f,
                            0.0,
                            0.0,
                            1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -11.0,
                            4.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right forearm
                        CubeTransformationByFrameModel(
                            8f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -12.0,
                            10.0,
                            -8.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right hand
                        CubeTransformationByFrameModel(
                            8f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -12.0,
                            16.0,
                            -14.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left femur
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left knee
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left leg
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left foot
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right femur
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right knee
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right leg
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right foot
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        cubesByFrameList.add(
            CubesByFrameModel(
                42,
                transformationList
            )
        )

        //frame 43
        transformationList = mutableListOf()
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left ear
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right ear
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //head
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //neck
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //torso
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //stomach
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //waist
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left shoulder
                        CubeTransformationByFrameModel(
                            4f,
                            0.0,
                            0.0,
                            -1.0,
                            GraphicUtils.RotationType.REFERENCE
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            4.0,
                            -6.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left arm
                        CubeTransformationByFrameModel(
                            4f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            6.0,
                            3.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left elbow
                        CubeTransformationByFrameModel(
                            4f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            11.0,
                            4.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left forearm
                        CubeTransformationByFrameModel(
                            8f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            12.0,
                            10.0,
                            -8.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left hand
                        CubeTransformationByFrameModel(
                            8f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            12.0,
                            16.0,
                            -14.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right shoulder
                        CubeTransformationByFrameModel(
                            4f,
                            0.0,
                            0.0,
                            1.0,
                            GraphicUtils.RotationType.REFERENCE
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -4.0,
                            -6.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right arm
                        CubeTransformationByFrameModel(
                            4f,
                            0.0,
                            0.0,
                            1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -6.0,
                            3.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right elbow
                        CubeTransformationByFrameModel(
                            4f,
                            0.0,
                            0.0,
                            1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -11.0,
                            4.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right forearm
                        CubeTransformationByFrameModel(
                            8f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -12.0,
                            10.0,
                            -8.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right hand
                        CubeTransformationByFrameModel(
                            8f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -12.0,
                            16.0,
                            -14.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left femur
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left knee
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left leg
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left foot
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right femur
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right knee
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right leg
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right foot
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        cubesByFrameList.add(
            CubesByFrameModel(
                43,
                transformationList
            )
        )

        //frame 44
        transformationList = mutableListOf()
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left ear
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right ear
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //head
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //neck
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //torso
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //stomach
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //waist
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left shoulder
                        CubeTransformationByFrameModel(
                            4f,
                            0.0,
                            0.0,
                            -1.0,
                            GraphicUtils.RotationType.REFERENCE
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            4.0,
                            -6.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left arm
                        CubeTransformationByFrameModel(
                            4f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            6.0,
                            3.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left elbow
                        CubeTransformationByFrameModel(
                            4f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            11.0,
                            4.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left forearm
                        CubeTransformationByFrameModel(
                            8f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            12.0,
                            10.0,
                            -8.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left hand
                        CubeTransformationByFrameModel(
                            8f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            12.0,
                            16.0,
                            -14.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right shoulder
                        CubeTransformationByFrameModel(
                            4f,
                            0.0,
                            0.0,
                            1.0,
                            GraphicUtils.RotationType.REFERENCE
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -4.0,
                            -6.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right arm
                        CubeTransformationByFrameModel(
                            4f,
                            0.0,
                            0.0,
                            1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -6.0,
                            3.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right elbow
                        CubeTransformationByFrameModel(
                            4f,
                            0.0,
                            0.0,
                            1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -11.0,
                            4.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right forearm
                        CubeTransformationByFrameModel(
                            8f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -12.0,
                            10.0,
                            -8.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right hand
                        CubeTransformationByFrameModel(
                            8f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -12.0,
                            16.0,
                            -14.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left femur
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left knee
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left leg
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left foot
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right femur
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right knee
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right leg
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right foot
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        cubesByFrameList.add(
            CubesByFrameModel(
                44,
                transformationList
            )
        )

        //frame 45
        transformationList = mutableListOf()
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left ear
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right ear
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //head
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //neck
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //torso
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //stomach
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //waist
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left shoulder
                        CubeTransformationByFrameModel(
                            4f,
                            0.0,
                            0.0,
                            -1.0,
                            GraphicUtils.RotationType.REFERENCE
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            4.0,
                            -6.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left arm
                        CubeTransformationByFrameModel(
                            4f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            6.0,
                            3.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left elbow
                        CubeTransformationByFrameModel(
                            4f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            11.0,
                            4.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left forearm
                        CubeTransformationByFrameModel(
                            8f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            12.0,
                            10.0,
                            -8.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left hand
                        CubeTransformationByFrameModel(
                            8f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            12.0,
                            16.0,
                            -14.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right shoulder
                        CubeTransformationByFrameModel(
                            4f,
                            0.0,
                            0.0,
                            1.0,
                            GraphicUtils.RotationType.REFERENCE
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -4.0,
                            -6.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right arm
                        CubeTransformationByFrameModel(
                            4f,
                            0.0,
                            0.0,
                            1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -6.0,
                            3.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right elbow
                        CubeTransformationByFrameModel(
                            4f,
                            0.0,
                            0.0,
                            1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -11.0,
                            4.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right forearm
                        CubeTransformationByFrameModel(
                            8f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -12.0,
                            10.0,
                            -8.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right hand
                        CubeTransformationByFrameModel(
                            8f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -12.0,
                            16.0,
                            -14.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left femur
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left knee
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left leg
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left foot
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right femur
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right knee
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right leg
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right foot
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        cubesByFrameList.add(
            CubesByFrameModel(
                45,
                transformationList
            )
        )

        //frame 46
        transformationList = mutableListOf()
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left ear
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right ear
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //head
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //neck
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //torso
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //stomach
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //waist
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left shoulder
                        CubeTransformationByFrameModel(
                            4f,
                            0.0,
                            0.0,
                            -1.0,
                            GraphicUtils.RotationType.REFERENCE
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            4.0,
                            -6.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left arm
                        CubeTransformationByFrameModel(
                            4f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            6.0,
                            3.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left elbow
                        CubeTransformationByFrameModel(
                            4f,
                            0.0,
                            0.0,
                            -1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            11.0,
                            4.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left forearm
                        CubeTransformationByFrameModel(
                            8f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            12.0,
                            10.0,
                            -8.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left hand
                        CubeTransformationByFrameModel(
                            8f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            12.0,
                            16.0,
                            -14.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right shoulder
                        CubeTransformationByFrameModel(
                            4f,
                            0.0,
                            0.0,
                            1.0,
                            GraphicUtils.RotationType.REFERENCE
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -4.0,
                            -6.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right arm
                        CubeTransformationByFrameModel(
                            4f,
                            0.0,
                            0.0,
                            1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -6.0,
                            3.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right elbow
                        CubeTransformationByFrameModel(
                            4f,
                            0.0,
                            0.0,
                            1.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -11.0,
                            4.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right forearm
                        CubeTransformationByFrameModel(
                            8f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -12.0,
                            10.0,
                            -8.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right hand
                        CubeTransformationByFrameModel(
                            8f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            -12.0,
                            16.0,
                            -14.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left femur
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left knee
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left leg
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left foot
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right femur
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right knee
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right leg
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right foot
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        cubesByFrameList.add(
            CubesByFrameModel(
                46,
                transformationList
            )
        )

        //frame 47
        transformationList = mutableListOf()
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left ear
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            -1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right ear
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            -1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //head
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            -1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //neck
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            -1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //torso
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            -1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //stomach
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            -1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //waist
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            -1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left shoulder
                        CubeTransformationByFrameModel(
                            2f,
                            -1.0,
                            0.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left arm
                        CubeTransformationByFrameModel(
                            2f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left elbow
                        CubeTransformationByFrameModel(
                            2f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -4.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left forearm
                        CubeTransformationByFrameModel(
                            4f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -9.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left hand
                        CubeTransformationByFrameModel(
                            4f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            1.0,
                            -16.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right shoulder
                        CubeTransformationByFrameModel(
                            2f,
                            -1.0,
                            0.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right arm
                        CubeTransformationByFrameModel(
                            2f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right elbow
                        CubeTransformationByFrameModel(
                            2f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -4.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right forearm
                        CubeTransformationByFrameModel(
                            4f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -9.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right hand
                        CubeTransformationByFrameModel(
                            4f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            1.0,
                            -16.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left femur
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left knee
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left leg
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left foot
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right femur
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right knee
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right leg
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right foot
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        cubesByFrameList.add(
            CubesByFrameModel(
                47,
                transformationList
            )
        )

        //frame 48
        transformationList = mutableListOf()
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left ear
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            -1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right ear
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            -1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //head
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            -1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //neck
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            -1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //torso
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            -1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //stomach
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            -1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //waist
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            -1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left shoulder
                        CubeTransformationByFrameModel(
                            2f,
                            -1.0,
                            0.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left arm
                        CubeTransformationByFrameModel(
                            2f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left elbow
                        CubeTransformationByFrameModel(
                            2f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -4.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left forearm
                        CubeTransformationByFrameModel(
                            4f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -9.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left hand
                        CubeTransformationByFrameModel(
                            4f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            1.0,
                            -16.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right shoulder
                        CubeTransformationByFrameModel(
                            2f,
                            -1.0,
                            0.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right arm
                        CubeTransformationByFrameModel(
                            2f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right elbow
                        CubeTransformationByFrameModel(
                            2f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -4.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right forearm
                        CubeTransformationByFrameModel(
                            4f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -9.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right hand
                        CubeTransformationByFrameModel(
                            4f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            1.0,
                            -16.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left femur
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left knee
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left leg
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left foot
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right femur
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right knee
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right leg
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right foot
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        cubesByFrameList.add(
            CubesByFrameModel(
                48,
                transformationList
            )
        )

        //frame 49
        transformationList = mutableListOf()
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left ear
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            -1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right ear
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            -1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //head
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            -1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //neck
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            -1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //torso
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            -1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //stomach
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            -1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //waist
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            -1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left shoulder
                        CubeTransformationByFrameModel(
                            2f,
                            -1.0,
                            0.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left arm
                        CubeTransformationByFrameModel(
                            2f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left elbow
                        CubeTransformationByFrameModel(
                            2f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -4.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left forearm
                        CubeTransformationByFrameModel(
                            4f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -9.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left hand
                        CubeTransformationByFrameModel(
                            4f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            1.0,
                            -16.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right shoulder
                        CubeTransformationByFrameModel(
                            2f,
                            -1.0,
                            0.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right arm
                        CubeTransformationByFrameModel(
                            2f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right elbow
                        CubeTransformationByFrameModel(
                            2f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -4.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right forearm
                        CubeTransformationByFrameModel(
                            4f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -9.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right hand
                        CubeTransformationByFrameModel(
                            4f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            1.0,
                            -16.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left femur
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left knee
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left leg
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left foot
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right femur
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right knee
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right leg
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right foot
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        cubesByFrameList.add(
            CubesByFrameModel(
                49,
                transformationList
            )
        )

        //frame 50
        transformationList = mutableListOf()
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left ear
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            -1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right ear
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            -1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //head
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            -1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //neck
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            -1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //torso
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            -1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //stomach
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            -1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //waist
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            -1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left shoulder
                        CubeTransformationByFrameModel(
                            2f,
                            -1.0,
                            0.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left arm
                        CubeTransformationByFrameModel(
                            2f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left elbow
                        CubeTransformationByFrameModel(
                            2f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -4.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left forearm
                        CubeTransformationByFrameModel(
                            4f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -9.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left hand
                        CubeTransformationByFrameModel(
                            4f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            1.0,
                            -16.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right shoulder
                        CubeTransformationByFrameModel(
                            2f,
                            -1.0,
                            0.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right arm
                        CubeTransformationByFrameModel(
                            2f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right elbow
                        CubeTransformationByFrameModel(
                            2f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -4.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right forearm
                        CubeTransformationByFrameModel(
                            4f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -9.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right hand
                        CubeTransformationByFrameModel(
                            4f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            1.0,
                            -16.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left femur
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left knee
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left leg
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left foot
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right femur
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right knee
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right leg
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right foot
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        cubesByFrameList.add(
            CubesByFrameModel(
                50,
                transformationList
            )
        )

        //frame 51
        transformationList = mutableListOf()
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left ear
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            -1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right ear
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            -1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //head
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            -1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //neck
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            -1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //torso
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            -1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //stomach
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            -1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //waist
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            -1.0,
                            0.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left shoulder
                        CubeTransformationByFrameModel(
                            2f,
                            -1.0,
                            0.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left arm
                        CubeTransformationByFrameModel(
                            2f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left elbow
                        CubeTransformationByFrameModel(
                            2f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -4.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left forearm
                        CubeTransformationByFrameModel(
                            4f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -9.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left hand
                        CubeTransformationByFrameModel(
                            4f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            1.0,
                            -16.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right shoulder
                        CubeTransformationByFrameModel(
                            2f,
                            -1.0,
                            0.0,
                            0.0,
                            GraphicUtils.RotationType.REFERENCE
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right arm
                        CubeTransformationByFrameModel(
                            2f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right elbow
                        CubeTransformationByFrameModel(
                            2f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -4.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right forearm
                        CubeTransformationByFrameModel(
                            4f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -9.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right hand
                        CubeTransformationByFrameModel(
                            4f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            1.0,
                            -16.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left femur
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left knee
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left leg
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //left foot
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right femur
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right knee
                        CubeTransformationByFrameModel(
                            1f,
                            -1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right leg
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        ),
                        CubeTransformationByFrameModel(
                            0f,
                            0.0,
                            0.0,
                            -2.0,
                            translation = true
                        )
                    )
                )
            }
        )
        transformationList.add(
            CubeTransformationListByFrameModel().apply {
                setDataList(
                    listOf(
                        //right foot
                        CubeTransformationByFrameModel(
                            1f,
                            1.0,
                            0.0,
                            0.0
                        )
                    )
                )
            }
        )
        cubesByFrameList.add(
            CubesByFrameModel(
                51,
                transformationList
            )
        )

        timeWarning(System.currentTimeMillis() - time, "loadAllFrameTransformations2")
    }

    private fun setTimerTask() {
        val timerTask = object : TimerTask() {
            override fun run() {
                if (!paused) {
                    try {
                        val time = System.currentTimeMillis()
                        frame++
                        val endFrame = 613
                        if (frame > endFrame) {
                            frame = 0
                        }

                        //letters
                        if (frame in 361..endFrame) {
                            val lettersByFrame = lettersByFrameList[0]
                            lettersByFrame.getCubeListForFrame(lettersCubeListModel.getData())
                            if (!viewDestroyed) {
                                binding.danceDanceView.setLettersThreeDCubesModel(lettersCubeListModel)
                            }
                        }

                        //character
                        val characterFrame = when (frame) {
                            0 -> {
                                0
                            }
                            in 1..360 -> {
                                1
                            }
                            in 361..401 -> {
                                0
                            }
                            in 402..406 -> {
                                frame - 400
                            }
                            in 407..446 -> {
                                0
                            }
                            in 447..451 -> {
                                frame - 440
                            }
                            in 452..492 -> {
                                0
                            }
                            in 493..497 -> {
                                frame - 481
                            }
                            in 498..502 -> {
                                frame - 481
                            }
                            in 503..507 -> {
                                frame - 481
                            }
                            in 508..512 -> {
                                frame - 481
                            }
                            in 513..517 -> {
                                frame - 481
                            }
                            in 518..522 -> {
                                frame - 481
                            }
                            in 523..527 -> {
                                frame - 481
                            }
                            in 528..532 -> {
                                frame - 481
                            }
                            in 533..endFrame -> {
                                0
                            }
                            else -> {
                                0
                            }
                        }

                        val cubesByFrame = cubesByFrameList[characterFrame]
                        val torsoCenter = GraphicUtils.centerOfThreeDCoordinates(
                            threeDCubeListModel.getData()[4].getCoordinateList()
                        )
                        cubesByFrame.getCubeListForFrame(threeDCubeListModel.getData(), torsoCenter)
                        if (!viewDestroyed) {
                            binding.danceDanceView.setThreeDCubesModel(threeDCubeListModel)
                        }

                        if (!viewDestroyed) {
                            binding.danceDanceView.invalidate()
                        }
                        timeWarning(System.currentTimeMillis() - time, "TimerTask.run")
                    } catch (e: Exception) {
                        Timber.e(e)
                    }
                }
            }
        }
        timer.scheduleAtFixedRate(timerTask, 100, 30)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        timer.cancel()
        viewDestroyed = true
    }

}