package com.giraffe.triplemapplication.features.orders.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.giraffe.triplemapplication.R
import com.giraffe.triplemapplication.bases.BaseFragment
import com.giraffe.triplemapplication.databinding.FragmentOrdersBinding
import com.giraffe.triplemapplication.features.orders.viewmodel.OrdersViewModel
import com.giraffe.triplemapplication.utils.Resource
import kotlinx.coroutines.launch

class OrdersFragment: BaseFragment<OrdersViewModel, FragmentOrdersBinding>() {
    override fun getViewModel(): Class<OrdersViewModel> = OrdersViewModel::class.java

    companion object{
        private const val TAG = "OrdersFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_orders, container, false)
    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        b: Boolean
    ): FragmentOrdersBinding = FragmentOrdersBinding.inflate(inflater, container, false)

    override fun handleClicks() {
    }

    override fun handleView() {
        observeGetOrders()
    }

    private fun observeGetOrders() {
        lifecycleScope.launch {
            mViewModel.ordersFlow.collect {
                when (it) {
                    is Resource.Failure -> { }
                    Resource.Loading -> { }
                    is Resource.Success -> {
                        Log.i(TAG, "observeGetOrders: ${it.value.orders}")
                    }
                }
            }
        }
    }
}