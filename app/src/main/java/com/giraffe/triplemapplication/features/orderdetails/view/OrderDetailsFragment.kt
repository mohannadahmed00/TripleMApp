package com.giraffe.triplemapplication.features.orderdetails.view

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.giraffe.triplemapplication.bases.BaseFragment
import com.giraffe.triplemapplication.databinding.FragmentOrderDetailsBinding
import com.giraffe.triplemapplication.features.home.adapters.ProductAdapter
import com.giraffe.triplemapplication.features.orderdetails.viewmodel.OrderDetailsVM
import com.giraffe.triplemapplication.model.products.Product
import com.giraffe.triplemapplication.utils.Resource
import kotlinx.coroutines.launch

class OrderDetailsFragment : BaseFragment<OrderDetailsVM, FragmentOrderDetailsBinding>() {
    override fun getViewModel() = OrderDetailsVM::class.java

    private lateinit var productsAdapter: OrderProductsAdapter
    private var names: MutableList<String> = mutableListOf()

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

    @SuppressLint("SetTextI18n")
    private fun observeGetOrder() {
        lifecycleScope.launch {
            mViewModel.orderFlow.collect {
                when (it) {
                    is Resource.Failure -> { dismissLoading() }
                    Resource.Loading -> { showLoading() }
                    is Resource.Success -> {
                        val order = it.value.order
                        binding.price.text = "${order.current_total_price} ${order.currency}"
                        binding.discount.text = "${order.total_discounts} ${order.currency}"

                        it.value.order.line_items.forEach {
                            names.add(it.title)
                        }

//                        var productsIds = ""
//                        Log.i("hahahahahaha", "observeGetOrder: order order ${it.value.order}")
//                        val products = it.value.order.line_items
//                        products.forEachIndexed { index, product ->
//                            productsIds += if (index == products.size - 1) {
//                                product.product_id
//                            } else {
//                                "${product.product_id}, "
//                            }
//                        }
//                        Log.i("hahahahahaha", "observeGetOrder: $productsIds")
//                        mViewModel.getProductsInOrder(productsIds)
                        mViewModel.getAllProducts()
                        observeGetAllProducts()
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

    private fun observeGetAllProducts() {
        lifecycleScope.launch {
            mViewModel.allProductsFlow.collect {
                when(it) {
                    is Resource.Failure -> { dismissLoading() }
                    Resource.Loading -> { showLoading() }
                    is Resource.Success -> {
                       dismissLoading()
                        Log.i("hahahahahahaha", "observeGetAllProducts: ${it.value.products.map { it.title }}")
                        productsAdapter.submitList(it.value.products.filter { product ->
//                            Log.i("hahahahahahaha", "observeGetAllProducts: ${names} ----- ${product.title} === ${product.handle}")
                            names.contains(product.title)
                        })
                    }
                }
            }
        }
    }

}