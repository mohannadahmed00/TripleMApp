package com.giraffe.triplemapplication.features.home.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.giraffe.triplemapplication.R
import com.giraffe.triplemapplication.databinding.ItemAllCategoriesBinding
import com.giraffe.triplemapplication.databinding.ItemCategoryBinding

class CategoriesAdapter(
    private val context: Context,
    private val onItemClick: (Category) -> Unit
): ListAdapter<Category, CategoriesAdapter.ViewHolder>(CategoriesDataDiffUtil()) {

    private lateinit var binding: ItemAllCategoriesBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = ItemAllCategoriesBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = getItem(position)

        Glide.with(context)
            .load(current.image)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.loading_img)
                    .error(R.drawable.ic_broken_image)
            )
            .into(holder.binding.categoryImage)
        holder.binding.categoryName.text = current.text
        holder.binding.item.setOnClickListener { onItemClick(current) }
    }

    inner class ViewHolder(var binding: ItemAllCategoriesBinding): RecyclerView.ViewHolder(binding.root)

    class CategoriesDataDiffUtil: DiffUtil.ItemCallback<Category>() {
        override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem == newItem
        }

    }
}

data class Category(
    @DrawableRes val image: Int,
    val text: String
)