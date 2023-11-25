package com.giraffe.triplemapplication.features.details.view

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.giraffe.triplemapplication.databinding.ColorPickerItemBinding

class ColorsAdapter(private val onClick : OnColorClickListener) :
    ListAdapter<String , ColorsAdapter.ViewHolder>(ColorsDiffUtil() ) {

    private lateinit var binding: ColorPickerItemBinding
    class ViewHolder(var binding :ColorPickerItemBinding) : RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorsAdapter.ViewHolder {
        val inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding= ColorPickerItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val selectedColor = getItem(position)
        holder.binding.colorItem.setCardBackgroundColor(getColorByName(selectedColor))

        holder.binding.colorItem.setOnClickListener{
            onClick.onClick(holder.binding.selectedIv)
        }
    }
    private fun getColorByName(colorName: String): Int {
        return try {
            Color.parseColor(colorName)
        } catch (e: IllegalArgumentException) {
            Color.parseColor("#000000") // Default color white if parsing fails
        }
    }
    class ColorsDiffUtil :DiffUtil.ItemCallback<String>(){
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }

}
