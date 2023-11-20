package com.giraffe.triplemapplication.features.allcategories.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.giraffe.triplemapplication.databinding.ItemCategoryBinding


class CategoryAdapter(
    private var list: List<String>,
    private val onItemClick: (String) -> Unit
): RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    private lateinit var binding: ItemCategoryBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = ItemCategoryBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = list.size

    fun setList(items: List<String>) {
        this.list = items
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = list[position]

        holder.binding.text.text = current
        holder.binding.item.setOnClickListener { onItemClick(current) }
    }

    inner class ViewHolder(var binding: ItemCategoryBinding): RecyclerView.ViewHolder(binding.root)
}