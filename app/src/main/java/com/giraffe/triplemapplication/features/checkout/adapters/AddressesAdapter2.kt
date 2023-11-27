package com.giraffe.triplemapplication.features.checkout.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.giraffe.triplemapplication.databinding.AddressItemBinding
import com.giraffe.triplemapplication.model.address.Address
import com.giraffe.triplemapplication.utils.hide
import com.giraffe.triplemapplication.utils.show

class AddressesAdapter2(
    val addresses: MutableList<Address>,
    private val onAddressClick: OnAddressClick,
) : Adapter<AddressesAdapter2.AddressVH>() {

    private var defaultPosition: Int = -1

    fun selectNewDefault(newPosition: Int,oldDefaultPosition:Int){
        addresses[newPosition].default = true
        addresses[oldDefaultPosition].default = false
        notifyItemChanged(newPosition)
        notifyItemChanged(oldDefaultPosition)
    }

    class AddressVH(val binding: AddressItemBinding) : ViewHolder(binding.root)

    fun updateList(list: List<Address>) {
        this.addresses.clear()
        this.addresses.addAll(list)
        notifyDataSetChanged()
    }

    fun deleteItem(position: Int) {
        addresses.removeAt(position)
        if (position == addresses.size - 1) {
            notifyDataSetChanged()
        } else {
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, addresses.size)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressVH {
        val binding = AddressItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AddressVH(binding)
    }

    override fun getItemCount() = addresses.size

    override fun onBindViewHolder(holder: AddressVH, position: Int) {
        val item = addresses[position]
        /*if(position==addresses.size-1){
            holder.binding.line.hide()
        }*/
        holder.binding.tvAddressTitle.text = item.name
        holder.binding.tvAddress.text = item.address1
        holder.binding.item.setOnClickListener {
            onAddressClick.onAddressClick(item, position)
        }
//        if (item.default == true){
//            defaultPosition = position
//            holder.binding.ivStar.show()
            holder.binding.ivDelete.hide()
//        }else{
            holder.binding.ivStar.hide()
//            holder.binding.ivDelete.show()
//        }

        holder.binding.root.setOnLongClickListener {
            onAddressClick.onAddressLongPress(item.id?.toLong()?:0,position,defaultPosition)
            true
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

    interface OnAddressClick {
        fun onAddressClick(address: Address, position: Int)
        fun onAddressLongPress(addressId: Long, position: Int,oldDefaultPosition:Int)

    }
}