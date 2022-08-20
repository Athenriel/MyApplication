package com.example.myapplication.ui.graphics

import android.os.Bundle
import android.view.View
import com.example.myapplication.databinding.FragmentGraphsBinding
import com.example.myapplication.ui.BaseFragment

/**
 * Created by Athenriel on 8/18/2022
 */
class GraphsFragment : BaseFragment<FragmentGraphsBinding>(FragmentGraphsBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /*
        val lineDataList = mutableListOf<Int>().apply {
            for (i in 0..50) {
                add((10 * sin(i.toFloat())).toInt())
            }
        }
        */
        val lineDataList = listOf(11, 29, 10, 20, 12, 5, 31, 24, 21, 13)
        binding.graphsLinePlotView.setDataList(lineDataList)
        val pieDataList = listOf(
            1,
            1,
            2,
            3,
            5,
            8,
            13,
            21,
            34,
            55,
            89
        )
        binding.graphsPiePlotView.setDataList(pieDataList)
    }

}