package com.example.myapplication.ui.graphics.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ItemGraphicsExampleBinding
import com.example.myapplication.model.GraphicExampleModel

/**
 * Created by Athenriel on 10/7/2022
 */
class GraphicExamplesAdapter(
    private val graphicExampleModelList: List<GraphicExampleModel>,
    private val graphicExampleListener: GraphicExampleListener?
) : RecyclerView.Adapter<GraphicExamplesAdapter.GraphicExampleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GraphicExampleViewHolder {
        return GraphicExampleViewHolder(parent)
    }

    override fun onBindViewHolder(holder: GraphicExampleViewHolder, position: Int) {
        holder.bind(graphicExampleModelList[position], graphicExampleListener)
    }

    override fun getItemCount(): Int {
        return graphicExampleModelList.size
    }

    inner class GraphicExampleViewHolder(
        private val parent: ViewGroup,
        private val binding: ItemGraphicsExampleBinding = ItemGraphicsExampleBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            graphicExampleModel: GraphicExampleModel,
            graphicExampleListener: GraphicExampleListener?
        ) {
            binding.itemGraphicsExampleTitleTextView.text = graphicExampleModel.title
            binding.itemGraphicsExampleTitleTextView.setOnClickListener {
                graphicExampleListener?.onGraphicExampleClick(graphicExampleModel.id)
            }
        }
    }

    interface GraphicExampleListener {
        fun onGraphicExampleClick(id: Int)
    }

}