package com.giraffe.triplemapplication.features.details.view

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.giraffe.triplemapplication.R
import com.giraffe.triplemapplication.databinding.ChipsItemBinding
import com.giraffe.triplemapplication.databinding.ColorPickerItemBinding

class SizeAdapter(private val context: Context, private val onClick: OnSizeClickListener , private val sizeList : List<String>) :
    RecyclerView.Adapter< SizeAdapter.ViewHolder>() {
    private var selectedItemPosition : Int = -1
    private lateinit var binding: ChipsItemBinding
    class ViewHolder(var binding :ChipsItemBinding) : RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SizeAdapter.ViewHolder {
        val inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding= ChipsItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return sizeList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val selectedSize = sizeList[position]
        holder.binding.textview.text = selectedSize
        if(position == selectedItemPosition){
            //do changes while selected size
            holder.binding.textview.setTextColor(context.getColor(R.color.red))
        }else{
            // size not selected
            holder.binding.textview.setTextColor(context.getColor(R.color.black))

        }

        holder.binding.root.setOnClickListener{
            if(selectedItemPosition != position){
                val previousSelectedItemPosition = selectedItemPosition
                selectedItemPosition = position
                notifyItemChanged(previousSelectedItemPosition)
                notifyItemChanged(selectedItemPosition)
                onClick.onSizeClickListener(selectedSize)
            }
        }
    }

}
