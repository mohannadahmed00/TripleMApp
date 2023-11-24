package com.giraffe.triplemapplication.features.orders.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.giraffe.triplemapplication.R
import com.giraffe.triplemapplication.bases.BaseFragment
import com.giraffe.triplemapplication.databinding.FragmentOrdersBinding
import com.giraffe.triplemapplication.features.home.adapters.ProductAdapter
import com.giraffe.triplemapplication.features.orders.viewmodel.OrdersViewModel
import com.giraffe.triplemapplication.utils.Resource
import kotlinx.coroutines.launch

class OrdersFragment: BaseFragment<OrdersViewModel, FragmentOrdersBinding>() {
    override fun getViewModel(): Class<OrdersViewModel> = OrdersViewModel::class.java

    private lateinit var adapter: OrdersAdapter

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
    }

    override fun handleView() {
        // Recycler View
        adapter = OrdersAdapter()
        binding.ordersRecyclerView.apply {
            adapter = adapter
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
                    is Resource.Failure -> { showLoading() }
                    Resource.Loading -> { dismissLoading() }
                    is Resource.Success -> {
                        dismissLoading()
                        adapter.submitList(it.value.orders)
                    }
                }
            }
        }
    }
}