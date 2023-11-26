package com.giraffe.triplemapplication.features.details.view

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.giraffe.triplemapplication.databinding.ColorPickerItemBinding

class ColorsAdapter( private val onClick: OnColorClickListener , private val colorsList : List<String>) :
    RecyclerView.Adapter< ColorsAdapter.ViewHolder>() {
    private var selectedItemPosition : Int = -1
    private lateinit var binding: ColorPickerItemBinding
    class ViewHolder(var binding :ColorPickerItemBinding) : RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorsAdapter.ViewHolder {
        val inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding= ColorPickerItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return colorsList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val selectedColor = colorsList[position]
        holder.binding.colorItem.setCardBackgroundColor(getColorByName(selectedColor))

        if(position == selectedItemPosition){
            //do changes while selected color
            holder.binding.selectedIv.visibility = View.VISIBLE
        }else{
            // color not selected
            holder.binding.selectedIv.visibility = View.GONE
        }

        holder.binding.colorItem.setOnClickListener{
            if(selectedItemPosition != position){
                val previousSelectedItemPosition = selectedItemPosition
                selectedItemPosition = position
                notifyItemChanged(previousSelectedItemPosition)
                notifyItemChanged(selectedItemPosition)
                onClick.onColorClickListener(selectedColor)
            }
        }
    }
    private fun getColorByName(colorName: String): Int {
        return try {
            Color.parseColor(colorName)
        } catch (e: IllegalArgumentException) {
            Color.parseColor("#FFFFFF") // Default color white if parsing fails
        }
    }

}
