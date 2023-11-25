package com.giraffe.triplemapplication.features.cart.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.giraffe.triplemapplication.databinding.CartItemBinding
import com.giraffe.triplemapplication.model.cart.CartItem
import com.giraffe.triplemapplication.utils.load

class CartAdapter(private val cartItems:MutableList<CartItem>) :Adapter<CartAdapter.CartItemVH>() {

    class CartItemVH(val binding: CartItemBinding):ViewHolder(binding.root)

    fun updateList(list: List<CartItem>) {
        this.cartItems.clear()
        this.cartItems.addAll(list)
        notifyDataSetChanged()
    }

    fun deleteItem(position: Int) {
        cartItems.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, cartItems.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartItemVH {
        val bind = CartItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CartItemVH(bind)
    }

    override fun getItemCount()=cartItems.size

    override fun onBindViewHolder(holder: CartItemVH, position: Int) {
        val item = cartItems[position]
        holder.binding.tvProductTitle.text = item.product.title
        holder.binding.ivProduct.load(item.product.image?.src?:"")
        val variantDetails = item.product.variants?.firstOrNull {
            it.id == item.variantId
        }
        holder.binding.tvPrice.text = variantDetails?.price?:"0.0"
        holder.binding.tvVariants.text = variantDetails?.title?:""
        holder.binding.tvCount.text = item.quantity.toString()
    }
}