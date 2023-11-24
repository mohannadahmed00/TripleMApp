package com.giraffe.triplemapplication.features.checkout.view

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.giraffe.triplemapplication.bases.BaseFragment
import com.giraffe.triplemapplication.databinding.FragmentCheckoutBinding
import com.giraffe.triplemapplication.features.checkout.viewmodel.CheckoutVM
import com.giraffe.triplemapplication.model.orders.createorder.Order
import com.giraffe.triplemapplication.model.orders.createorder.OrderCreate
import com.giraffe.triplemapplication.utils.Resource
import kotlinx.coroutines.launch

class CheckoutFragment : BaseFragment<CheckoutVM, FragmentCheckoutBinding>() {
    override fun getViewModel(): Class<CheckoutVM> = CheckoutVM::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        b: Boolean
    ): FragmentCheckoutBinding = FragmentCheckoutBinding.inflate(inflater, container, false)

    override fun handleView() { }

    override fun handleClicks() {
        binding.btnClose.setOnClickListener { navigateUp() }
        binding.btnCheckout.setOnClickListener { checkout() }
    }

    private fun navigateUp() { findNavController().navigateUp() }

    private fun checkout() {
        val orderCreate = OrderCreate(
            Order(
                currency = "",
                line_items = listOf(),
                total_tax = 0.0,
                transactions = listOf()
            )
        )
        mViewModel.checkout(orderCreate)
        observeCreateOrder()
    }

    private fun observeCreateOrder() {
        lifecycleScope.launch {
            mViewModel.ordersFlow.collect {
                when (it) {
                    is Resource.Failure -> {
                        dismissLoading()
                    }
                    Resource.Loading -> { showLoading() }
                    is Resource.Success -> {
                        dismissLoading()
                        if (it.value.orders.isNotEmpty()) { navigateToOrderPlacedFragment() }
                    }
                }
            }
        }
    }

    private fun navigateToOrderPlacedFragment() {
        val action: NavDirections = CheckoutFragmentDirections.actionCheckoutFragmentToOrderPlacedFragment()
        Navigation.findNavController(requireView()).navigate(action)
    }
}
