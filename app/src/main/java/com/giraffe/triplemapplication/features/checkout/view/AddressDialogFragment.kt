package com.giraffe.triplemapplication.features.checkout.view

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.giraffe.triplemapplication.R
import com.giraffe.triplemapplication.features.checkout.adapters.AddressesAdapter2
import com.giraffe.triplemapplication.model.address.Address

class AddressDialogFragment(
    private val addresses: MutableList<Address>,
    private val onSelectAddress: (Address) -> Unit
) : DialogFragment(),
    AddressesAdapter2.OnAddressClick {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.dialog_addresses, null)

        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view_addresses)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        Log.i("hahahahahaha", "onCreateDialog: addresses $addresses")
        val adapter = AddressesAdapter2(addresses, this)
        recyclerView.adapter = adapter

        return AlertDialog.Builder(requireActivity())
            .setTitle("Addresses")
            .setView(view)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
    }

    override fun onAddressClick(address: Address, position: Int) {
        onSelectAddress(address)
        dialog?.dismiss()
    }

    override fun onAddressLongPress(addressId: Long, position: Int, oldDefaultPosition: Int) {
        TODO("Not yet implemented")
    }
}