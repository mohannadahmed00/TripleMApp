package com.giraffe.triplemapplication.features.fav.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.giraffe.triplemapplication.databinding.FavItemBinding
import com.giraffe.triplemapplication.model.wishlist.WishListItem
import com.giraffe.triplemapplication.utils.convert
import com.giraffe.triplemapplication.utils.load

class FavAdapter(
    private val onItemClick: (WishListItem) -> Unit,
    private val exchangeRate:Pair<Double,Double>?,
    private  val currency:Int
) :
    ListAdapter<WishListItem, FavAdapter.ViewHolder>(WishListItemDataDiffUtil() ) {

    private lateinit var binding: FavItemBinding
    class ViewHolder(var binding :FavItemBinding) : RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding= FavItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentProduct = getItem(position)
        holder.binding.ivProduct.load(currentProduct.product.image!!.src!!)
        holder.binding.tvProductTitle.text = currentProduct.product.title
        holder.binding.tvPrice.text = currentProduct.product.variants?.first()?.price?.toDouble()?.convert(exchangeRate).toString()
            .plus(" ${holder.binding.root.context.getString(currency)}")
        holder.binding.tvVariants.text = currentProduct.product.vendor
        holder.binding.root.setOnClickListener { onItemClick(currentProduct) }

    }
    class WishListItemDataDiffUtil: DiffUtil.ItemCallback<WishListItem>() {
        override fun areItemsTheSame(oldItem: WishListItem, newItem: WishListItem): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: WishListItem, newItem: WishListItem): Boolean {
            return oldItem == newItem
        }

    }


}
