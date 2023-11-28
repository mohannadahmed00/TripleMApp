package com.giraffe.triplemapplication.features.orders.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.giraffe.triplemapplication.R
import com.giraffe.triplemapplication.bases.BaseFragment
import com.giraffe.triplemapplication.databinding.FragmentOrdersBinding
import com.giraffe.triplemapplication.features.orders.viewmodel.OrdersViewModel
import com.giraffe.triplemapplication.model.orders.Order
import com.giraffe.triplemapplication.utils.Resource
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch

class OrdersFragment: BaseFragment<OrdersViewModel, FragmentOrdersBinding>() {
    override fun getViewModel(): Class<OrdersViewModel> = OrdersViewModel::class.java

    private lateinit var ordersAdapter: OrdersAdapter

    companion object{
        private const val TAG = "OrdersFragment"
    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        b: Boolean
    ): FragmentOrdersBinding = FragmentOrdersBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleView()
    }

    override fun handleClicks() {
        binding.btnClose.setOnClickListener { navigateUp() }
    }

    private fun navigateUp() { findNavController().navigateUp() }

    override fun handleView() {
        // Recycler View
        lifecycleScope.launch {
            sharedViewModel.currencyFlow.collect {
                ordersAdapter = if (it is Resource.Success) {
                    OrdersAdapter(::showDelDialog, sharedViewModel.exchangeRateFlow.value, it.value) { product -> navigateToOrderDetailsFragment(product) }
                } else {
                    OrdersAdapter(::showDelDialog, sharedViewModel.exchangeRateFlow.value, "") { product -> navigateToOrderDetailsFragment(product) }
                }
            }
        }
        binding.ordersRecyclerView.apply {
            adapter = ordersAdapter
            layoutManager = LinearLayoutManager(context).apply {
                orientation = RecyclerView.VERTICAL
            }
        }

        observeGetOrders()
    }

    private fun observeGetOrders() {
        lifecycleScope.launch {
            mViewModel.ordersFlow.collect {
                when (it) {
                    is Resource.Failure -> { dismissLoading() }
                    Resource.Loading -> { showLoading() }
                    is Resource.Success -> {
                        dismissLoading()
                        Log.i("hahahaha", "observeGetOrders: customer id ${mViewModel.customerId}")
                        ordersAdapter.submitList(it.value.orders.filter { order -> order.customer?.id == mViewModel.customerId })
                    }
                }
            }
        }
    }

    private fun showDelDialog(orderId: Long) {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage(R.string.dialog_del)
            .setPositiveButton(R.string.dialog_del_ok) { dialog, _ ->
                delOrder(orderId)
                dialog.dismiss()
            }
            .setNegativeButton(R.string.dialog_del_cancel) { dialog, _ ->
                // User cancelled the dialog
                dialog.dismiss()
            }.show()
    }

    private fun delOrder(orderId: Long) {
        mViewModel.delOrder(orderId)
    }

    private fun navigateToOrderDetailsFragment(order: Order) {
        val action: NavDirections = OrdersFragmentDirections.actionOrdersFragmentToOrderDetailsFragment(order.id)
        findNavController().navigate(action)
    }
}