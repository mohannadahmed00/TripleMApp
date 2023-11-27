package com.giraffe.triplemapplication.features.orderdetails.view

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.giraffe.triplemapplication.bases.BaseFragment
import com.giraffe.triplemapplication.databinding.FragmentOrderDetailsBinding
import com.giraffe.triplemapplication.features.allcategories.adapters.ProductsAdapter
import com.giraffe.triplemapplication.features.allcategories.view.AllCategoriesFragmentDirections
import com.giraffe.triplemapplication.features.orderdetails.viewmodel.OrderDetailsVM
import com.giraffe.triplemapplication.model.products.Product
import com.giraffe.triplemapplication.utils.Resource
import kotlinx.coroutines.launch

class OrderDetailsFragment : BaseFragment<OrderDetailsVM, FragmentOrderDetailsBinding>() {
    override fun getViewModel() = OrderDetailsVM::class.java

    private lateinit var productsAdapter: OrderProductsAdapter

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        b: Boolean
    ): FragmentOrderDetailsBinding = FragmentOrderDetailsBinding.inflate(inflater, container, false)

    override fun handleView() {
        val orderId = OrderDetailsFragmentArgs.fromBundle(requireArguments()).orderId

        // Recycler View
        productsAdapter = OrderProductsAdapter(requireContext(), sharedViewModel.exchangeRateFlow.value) { navigateToProductInfoScreen(it) }
        binding.categoryRecyclerView.apply {
            adapter = productsAdapter
            layoutManager = LinearLayoutManager(context).apply {
                orientation = RecyclerView.VERTICAL
            }
        }

        mViewModel.getOrder(orderId)
        observeGetOrder()
    }

    private fun navigateToProductInfoScreen(product: Product) {
        sharedViewModel.setCurrentProduct(product)
        val action =
            OrderDetailsFragmentDirections.actionOrderDetailsFragmentToProductInfoFragment()
        findNavController().navigate(action)
    }

    private fun observeGetOrder() {
        lifecycleScope.launch {
            mViewModel.orderFlow.collect {
                when (it) {
                    is Resource.Failure -> { dismissLoading() }
                    Resource.Loading -> { showLoading() }
                    is Resource.Success -> {
                        var productsIds = ""
                        val products = it.value.order.line_items
                        products.forEachIndexed { index, product ->
                            productsIds += if (index == products.size - 1) {
                                product.product_id
                            } else {
                                "${product.product_id}, "
                            }
                        }
                        Log.i("hahahahahaha", "observeGetOrder: $productsIds")
                        mViewModel.getProductsInOrder(productsIds)
                        observeGetProducts()
                    }
                }
            }
        }
    }

    private fun observeGetProducts() {
        lifecycleScope.launch {
            mViewModel.productsFlow.collect {
                when (it) {
                    is Resource.Failure -> { dismissLoading() }
                    Resource.Loading -> { showLoading() }
                    is Resource.Success -> {
                        dismissLoading()
                        productsAdapter.submitList(it.value.products)
                        Log.i("hahahahahahaha", "observeGetProducts: ${it.value.products}")
                    }
                }
            }
        }
    }

    override fun handleClicks() {
        binding.closeButton.setOnClickListener { findNavController().navigateUp() }
    }

}