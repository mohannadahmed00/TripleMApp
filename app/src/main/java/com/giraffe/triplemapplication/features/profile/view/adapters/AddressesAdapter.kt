package com.giraffe.triplemapplication.features.profile.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.giraffe.triplemapplication.databinding.AddressItemBinding
import com.giraffe.triplemapplication.model.address.Address

class AddressesAdapter(private val addresses: MutableList<Address>,private val onAddressClick: OnAddressClick): Adapter<AddressesAdapter.AddressVH>() {

    //private var selectedItemPosition: Int = -1

    class AddressVH(val binding: AddressItemBinding) :ViewHolder(binding.root)

    fun updateList(list: List<Address>) {
        this.addresses.clear()
        this.addresses.addAll(list)
        notifyDataSetChanged()
    }

    fun deleteItem(position: Int) {
        addresses.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, addresses.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressVH {
        val binding = AddressItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AddressVH(binding)
    }

    override fun getItemCount()= addresses.size

    override fun onBindViewHolder(holder: AddressVH, position: Int) {
        val item = addresses[position]
        /*if(position==addresses.size-1){
            holder.binding.line.hide()
        }*/
        holder.binding.tvAddress.text = "${item.name}\n${item.address1}, ${item.city}, ${item.country}"
        holder.binding.ivDelete.setOnClickListener {
            onAddressClick.onAddressClick(item,position)
        }


        /*holder.binding.root.setOnClickListener {
            if (selectedItemPosition != position) {
                val previousSelectedItemPosition = selectedItemPosition
                selectedItemPosition = position
                notifyItemChanged(previousSelectedItemPosition)
                notifyItemChanged(selectedItemPosition)
                onAddressClick.onClick(item)
            }
        }
        if (position == selectedItemPosition) {
            holder.binding.ivAddressCorrect.show()
        } else {
            holder.binding.ivAddressCorrect.hide()
        }*/
    }

    interface OnAddressClick{
        fun onAddressClick(address: Address,position:Int)
    }
}