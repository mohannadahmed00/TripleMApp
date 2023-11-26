package com.giraffe.triplemapplication.features.home.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.giraffe.triplemapplication.R
import com.giraffe.triplemapplication.databinding.ItemProductBinding
import com.giraffe.triplemapplication.model.products.Product
import com.giraffe.triplemapplication.utils.Resource
import com.giraffe.triplemapplication.utils.convert

class ProductAdapter(
    private val context: Context,
    private val exchangeRate: Pair<Double,Double>?,
    private val currency: String,
    private val onItemClick: (Product) -> Unit
): ListAdapter<Product, ProductAdapter.ViewHolder>(ProductDataDiffUtil()) {

    private lateinit var binding: ItemProductBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = ItemProductBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = getItem(position)

        Glide.with(context)
            .load(current.image?.src)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.loading_img)
                    .error(R.drawable.ic_broken_image)
            )
            .into(holder.binding.productImage)
        holder.binding.productName.text = current.handle
        holder.binding.productPrice.text = "${ current.variants?.get(0)?.price?.toDouble()?.convert(exchangeRate).toString()} $currency"
        holder.binding.row.setOnClickListener { onItemClick(current) }
    }

    inner class ViewHolder(var binding: ItemProductBinding): RecyclerView.ViewHolder(binding.root)

    class ProductDataDiffUtil: DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }

    }
}