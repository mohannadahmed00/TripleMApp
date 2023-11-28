package com.giraffe.triplemapplication.features.allcategories.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.giraffe.triplemapplication.R
import com.giraffe.triplemapplication.databinding.ItemCategoryBinding
import com.giraffe.triplemapplication.model.products.Product
import com.giraffe.triplemapplication.utils.convert

class ProductsAdapter(
    private val context: Context,
    private val exchangeRate: Pair<Double,Double>?,
    private val currency: Int,
    private val onItemClick: (Product) -> Unit
): ListAdapter<Product, ProductsAdapter.ViewHolder>(CategoriesDataDiffUtil()) {

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
        holder.binding.price.text = "${ current.variants?.get(0)?.price?.toDouble()?.convert(exchangeRate).toString()} ${holder.binding.root.context.getString(currency)}"

        Glide.with(context)
            .load(current.image?.src)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.loading_img)
                    .error(R.drawable.ic_broken_image)
            )
            .into(holder.binding.brandImage)
    }

    inner class ViewHolder(var binding: ItemCategoryBinding): RecyclerView.ViewHolder(binding.root)

    class CategoriesDataDiffUtil: DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }

    }
}