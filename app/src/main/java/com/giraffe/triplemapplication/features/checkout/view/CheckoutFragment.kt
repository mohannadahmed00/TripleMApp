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
import com.giraffe.triplemapplication.model.orders.createorder.LineItem
import com.giraffe.triplemapplication.model.orders.createorder.Order
import com.giraffe.triplemapplication.model.orders.createorder.OrderCreate
import com.giraffe.triplemapplication.model.orders.createorder.TaxLine
import com.giraffe.triplemapplication.model.orders.createorder.Transaction
import com.giraffe.triplemapplication.utils.Resource
import kotlinx.coroutines.launch

class CheckoutFragment : BaseFragment<CheckoutVM, FragmentCheckoutBinding>() {
    override fun getViewModel(): Class<CheckoutVM> = CheckoutVM::class.java

    companion object {
        private const val TAG = "CheckoutFragment"
    }

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
                currency = "EUR",
                line_items = listOf(
                    LineItem(
                        title = "Big Brown Bear Boots",
                        price = 74.99,
                        grams = "1300",
                        quantity = 3,
                        tax_lines = listOf(
                            TaxLine(
                                price = 13.5,
                                rate = 0.06,
                                title = "State tax"
                            )
                        )
                    )
                ),
                total_tax = 13.5,
                transactions = listOf(
                    Transaction(
                        amount = 238.47,
                        kind = "sale",
                        status = "success"
                    )
                )
            )
        )
//        mViewModel.checkout(orderCreate)
        mViewModel.completeOrder()
//        observeCreateOrder()
        observeDraftOrderOrder()
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
                        navigateToOrderPlacedFragment()
                    }
                }
            }
        }
    }

    private fun observeDraftOrderOrder() {
        lifecycleScope.launch {
            mViewModel.draftOrderFlow.collect {
                when (it) {
                    is Resource.Failure -> {
                        dismissLoading()
                        Log.i("hahahahaha", "observeDraftOrderOrder: ${it.errorBody}")
                    }
                    Resource.Loading -> { showLoading() }
                    is Resource.Success -> {
                        dismissLoading()
                        navigateToOrderPlacedFragment()
                        mViewModel.removeCart()
                        Log.i("hahahahaha", "observeDraftOrderOrder: ${it.value}")
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
