package com.giraffe.triplemapplication.features.cart.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.giraffe.triplemapplication.databinding.CartItemBinding
import com.giraffe.triplemapplication.model.cart.CartItem
import com.giraffe.triplemapplication.utils.convert
import com.giraffe.triplemapplication.utils.load

class CartAdapter(
    private val cartItems: MutableList<CartItem>,
    private val exchangeRate: Pair<Double, Double>?,
    private val currencySymbol: Int,
    private val onCartItemClick: OnCartItemClick
) : Adapter<CartAdapter.CartItemVH>() {

    class CartItemVH(val binding: CartItemBinding) : ViewHolder(binding.root)

    fun updateList(list: List<CartItem>) {
        this.cartItems.clear()
        this.cartItems.addAll(list)
        notifyDataSetChanged()
    }

    fun deleteItem(position: Int) {
        cartItems.removeAt(position)
        if (position == cartItems.size - 1) {
            notifyDataSetChanged()
        } else {
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, cartItems.size)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartItemVH {
        val bind = CartItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartItemVH(bind)
    }

    override fun getItemCount() = cartItems.size

    override fun onBindViewHolder(holder: CartItemVH, position: Int) {
        val item = cartItems[position]
        holder.binding.tvProductTitle.text = item.product.title
        holder.binding.ivProduct.load(item.product.image?.src ?: "")
        val variantDetails = item.product.variants?.firstOrNull {
            it.id == item.variantId
        }
        holder.binding.tvPrice.text =
            variantDetails?.price?.toDouble()?.convert(exchangeRate).toString()
                .plus(" ${holder.binding.root.context.getString(currencySymbol)}")
        holder.binding.tvVariants.text = variantDetails?.title ?: ""
        holder.binding.tvCount.text = item.quantity.toString()
        holder.binding.ivPlus.setOnClickListener {
            onCartItemClick.onPlusClick(item)
        }
        holder.binding.ivMinus.setOnClickListener {
            onCartItemClick.onMinusClick(item,position)
        }
    }

    interface OnCartItemClick{
        fun onPlusClick(cartItem: CartItem)
        fun onMinusClick(cartItem: CartItem,position:Int)
    }
}