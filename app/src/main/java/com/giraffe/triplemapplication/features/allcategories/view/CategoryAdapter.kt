package com.giraffe.triplemapplication.features.allcategories.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.giraffe.triplemapplication.databinding.ItemCategoryBinding
import com.giraffe.triplemapplication.model.categories.CustomCollection

class CategoryAdapter(
    private val onItemClick: (CustomCollection) -> Unit
): ListAdapter<CustomCollection, CategoryAdapter.ViewHolder>(CategoriesDataDiffUtil()) {

    private lateinit var binding: ItemCategoryBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = ItemCategoryBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = getItem(position)
        holder.binding.text.text = current.handle
        holder.binding.item.setOnClickListener { onItemClick(current) }
    }

    inner class ViewHolder(var binding: ItemCategoryBinding): RecyclerView.ViewHolder(binding.root)

    class CategoriesDataDiffUtil: DiffUtil.ItemCallback<CustomCollection>() {
        override fun areItemsTheSame(oldItem: CustomCollection, newItem: CustomCollection): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: CustomCollection, newItem: CustomCollection): Boolean {
            return oldItem == newItem
        }

    }
}