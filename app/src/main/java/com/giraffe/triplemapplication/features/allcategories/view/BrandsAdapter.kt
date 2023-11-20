package com.giraffe.triplemapplication.features.allcategories.view

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.giraffe.triplemapplication.R
import com.giraffe.triplemapplication.databinding.ItemBrandBinding
import com.giraffe.triplemapplication.model.brands.SmartCollection

class BrandsAdapter(
    private val context: Context,
    private val onItemClick: (SmartCollection) -> Unit
): ListAdapter<SmartCollection, BrandsAdapter.ViewHolder>(BrandsDataDiffUtil()) {

    private lateinit var binding: ItemBrandBinding
    private var selectedItem = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = ItemBrandBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = getItem(position)

        Glide.with(context)
            .load(current.image.src)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.loading_img)
                    .error(R.drawable.ic_broken_image)
            )
            .into(holder.binding.brandImage)
        holder.binding.brandName.text = current.handle
        if (position == selectedItem) {
            holder.binding.brandName.setTextColor(Color.RED)
        } else {
            holder.binding.brandName.setTextColor(Color.BLACK)
        }
        holder.binding.item.setOnClickListener {
            onItemClick(current)
            if (selectedItem != position) {
                val previousItem = selectedItem
                selectedItem = position
                notifyItemChanged(previousItem)
                notifyItemChanged(selectedItem)
            }
        }
    }

    inner class ViewHolder(var binding: ItemBrandBinding): RecyclerView.ViewHolder(binding.root)

    class BrandsDataDiffUtil: DiffUtil.ItemCallback<SmartCollection>() {
        override fun areItemsTheSame(oldItem: SmartCollection, newItem: SmartCollection): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: SmartCollection, newItem: SmartCollection): Boolean {
            return oldItem == newItem
        }

    }
}