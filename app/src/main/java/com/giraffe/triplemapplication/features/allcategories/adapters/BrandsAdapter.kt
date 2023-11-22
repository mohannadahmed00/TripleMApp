package com.giraffe.triplemapplication.features.allcategories.adapters

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
import com.giraffe.triplemapplication.model.categories.CustomCollection

class BrandsAdapter<T>(
    private val context: Context,
    selectedItemFromHome: Int,
    private val onItemClick: (T) -> Unit
): ListAdapter<T, BrandsAdapter<T>.ViewHolder>(BrandsDataDiffUtil()) {

    private lateinit var binding: ItemBrandBinding
    private var selectedItem = selectedItemFromHome

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = ItemBrandBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = getItem(position)
        if (current is SmartCollection) {
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
        } else if (current is CustomCollection) {
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
    }

    inner class ViewHolder(var binding: ItemBrandBinding): RecyclerView.ViewHolder(binding.root)

    class BrandsDataDiffUtil<T>: DiffUtil.ItemCallback<T>() {
        override fun areItemsTheSame(oldItem: T & Any, newItem: T & Any): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: T & Any, newItem: T & Any): Boolean {
            return oldItem == newItem
        }

    }
}