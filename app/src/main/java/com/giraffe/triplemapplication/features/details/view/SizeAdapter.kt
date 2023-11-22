package com.giraffe.triplemapplication.features.details.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.giraffe.triplemapplication.databinding.ChipsItemBinding

class SizeAdapter :
    ListAdapter<String, SizeAdapter.ViewHolder>(ColorsDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ChipsItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val selectedSize = getItem(position)
        holder.bind(selectedSize)
    }

    class ViewHolder(private val binding: ChipsItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(size: String) {
            binding.textview.text = size
        }
    }

    class ColorsDiffUtil : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }
}
