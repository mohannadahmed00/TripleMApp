package com.giraffe.triplemapplication.features.orders.view

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.giraffe.triplemapplication.databinding.ItemOrderBinding
import com.giraffe.triplemapplication.model.orders.Order

class OrdersAdapter(
) : ListAdapter<Order, OrdersAdapter.ViewHolder>(OrdersDataDiffUtil()) {

    private lateinit var binding: ItemOrderBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = ItemOrderBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = getItem(position)
        holder.binding.orderNumber.text = current.order_number.toString()
    }

    inner class ViewHolder(var binding: ItemOrderBinding) : RecyclerView.ViewHolder(binding.root)

    class OrdersDataDiffUtil : DiffUtil.ItemCallback<Order>() {
        override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem == newItem
        }

    }
}