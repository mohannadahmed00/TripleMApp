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
import com.giraffe.triplemapplication.features.splash.view.SplashFragment
import com.giraffe.triplemapplication.model.address.Address
import com.giraffe.triplemapplication.model.cart.ShippingAddress
import com.giraffe.triplemapplication.model.cart.request.Customer
import com.giraffe.triplemapplication.model.cart.request.DraftOrder
import com.giraffe.triplemapplication.model.orders.createorder.LineItem
import com.giraffe.triplemapplication.model.orders.createorder.Order
import com.giraffe.triplemapplication.model.orders.createorder.OrderCreate
import com.giraffe.triplemapplication.model.orders.createorder.TaxLine
import com.giraffe.triplemapplication.model.orders.createorder.Transaction
import com.giraffe.triplemapplication.utils.Resource
import kotlinx.coroutines.launch

class CheckoutFragment : BaseFragment<CheckoutVM, FragmentCheckoutBinding>() {
    override fun getViewModel(): Class<CheckoutVM> = CheckoutVM::class.java

    private var addresses: MutableList<Address> = mutableListOf()
    private lateinit var selectedAddress: Address

    companion object {
        private const val TAG = "CheckoutFragment"
    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        b: Boolean
    ): FragmentCheckoutBinding = FragmentCheckoutBinding.inflate(inflater, container, false)

    override fun handleView() {
        observeGetCartId()
//        observeGetCustomerDetails()
        mViewModel.getAddresses()
        observeGetAddresses()
    }

//    private fun observeGetCustomerDetails() {
//        lifecycleScope.launch {
//            mViewModel.customerName.collect {
//                when (it) {
//                    is Resource.Failure -> {
//                        dismissLoading()
//                        Log.i(TAG, "observeGetCustomerDetails: SUCCESS ${it.errorBody}")
//                    }
//                    Resource.Loading -> {
//                        Log.i(TAG, "observeGetCustomerDetails: LOADING")
//                        showLoading()
//                    }
//                    is Resource.Success -> {
//                        dismissLoading()
//                        Log.i(TAG, "observeGetCustomerDetails: SUCCESS ${it.value?.first_name}")
//                        binding.tvName.text = it.value?.first_name
//                    }
//                }
//            }
//        }
//    }

    override fun handleClicks() {
        binding.btnClose.setOnClickListener { navigateUp() }
        binding.btnCheckout.setOnClickListener { checkout() }
        binding.ivEnterAddress.setOnClickListener {
            // Show the dialog
            val dialogFragment = AddressDialogFragment(addresses) {
                Log.i("hahahahahaha", "handleClicks: $it")
                binding.tvName.text = it.first_name
                binding.tvAddress.text = it.address1
                selectedAddress = it
            }
            dialogFragment.show(parentFragmentManager, "AddressDialogFragment")
        }
    }

    private fun navigateUp() { findNavController().navigateUp() }

    private fun checkout() {
        mViewModel.completeOrder()
        observeDraftOrderOrder()

//        val orderCreate = OrderCreate(
//            Order(
//                currency = "EUR",
//                line_items = listOf(
//                    LineItem(
//                        title = "Big Brown Bear Boots",
//                        price = 74.99,
//                        grams = "1300",
//                        quantity = 3,
//                        tax_lines = listOf(
//                            TaxLine(
//                                price = 13.5,
//                                rate = 0.06,
//                                title = "State tax"
//                            )
//                        )
//                    )
//                ),
//                total_tax = 13.5,
//                transactions = listOf(
//                    Transaction(
//                        amount = 238.47,
//                        kind = "sale",
//                        status = "success"
//                    )
//                )
//            )
//        )
////        mViewModel.checkout(orderCreate)
//
////        observeCreateOrder()
//        observeOrder()
    }

    private fun observeGetCartId() {
        lifecycleScope.launch {
            sharedViewModel.cartIdFlow.collect {
                when (it) {
                    is Resource.Failure -> {
                        Log.e(
                            TAG,
                            "observeGetCartId: (Failure ${it.errorCode}) ${it.errorBody}"
                        )
                    }

                    Resource.Loading -> {
                        Log.i(TAG, "observeGetCartId: (Loading)")
                    }

                    is Resource.Success -> {
                        Log.d(TAG, "observeGetCartId: (Success) ${it.value}")
                        mViewModel.getOrderDetails(it.value)
                    }
                }
            }
        }
    }

    private fun observeOrder() {
        lifecycleScope.launch {
            mViewModel.orderFlow.collect {
                when (it) {
                    is Resource.Failure -> {
                        dismissLoading()
                        Log.i(TAG, "observeOrder: ERROR ${it.errorBody}")
                    }
                    Resource.Loading -> {
                        showLoading()
                        Log.i(TAG, "observeOrder: LOADING")
                    }
                    is Resource.Success -> {
                        Log.i(TAG, "observeOrder: SUCCESS ${it.value}")
                        dismissLoading()
                        val order = it.value.order
                        mViewModel.modifyOrder(
                            DraftOrder(
//                                id = order.id,
                                line_items = order.line_items.map {
                                                                  com.giraffe.triplemapplication.model.cart.request.LineItem(
                                                                      quantity = it.quantity,
                                                                      variant_id = it.variant_id,
                                                                      title = it.title,
                                                                      price = it.price.toDouble()
                                                                  )
                                },
//                                use_customer_default_address = true,
//                                appliedDiscount = null,
//                                customer = Customer(mViewModel.customerId),
//                                shipping_address = ShippingAddress(
//                                    address1 = selectedAddress.address1.toString(),
//                                    city = selectedAddress.city.toString(),
//                                    country = selectedAddress.country.toString(),
//                                    first_name = selectedAddress.first_name.toString(),
//                                    last_name = selectedAddress.last_name.toString(),
//                                    phone = selectedAddress.phone.toString(),
//                                    province = selectedAddress.province.toString(),
//                                    zip = selectedAddress.zip.toString()
//                                )
                            )
                        )
                        observeModifyOrder()
                    }
                }
            }
        }
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

    private fun observeModifyOrder() {
        lifecycleScope.launch {
            mViewModel.ordersFlow.collect {
                when (it) {
                    is Resource.Failure -> {
                        dismissLoading()
                        Log.i(TAG, "observeModifyOrder: ERROR")
                    }
                    Resource.Loading -> {
                        showLoading()
                        Log.i(TAG, "observeModifyOrder: LOADING")
                    }
                    is Resource.Success -> {
                        Log.i(TAG, "observeModifyOrder: SUCCESS")
//                        dismissLoading()
                        mViewModel.completeOrder()
                        observeDraftOrderOrder()
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
                        Log.i(TAG, "observeDraftOrderOrder: ${it.errorBody}")
                    }
                    Resource.Loading -> {
                        showLoading()
                        Log.i(TAG, "observeDraftOrderOrder: LOADING")
                    }
                    is Resource.Success -> {
                        dismissLoading()
                        navigateToOrderPlacedFragment()
                        mViewModel.removeCart()
                        Log.i(TAG, "observeDraftOrderOrder: ${it.value}")
                    }
                }
            }
        }
    }

    private fun observeGetAddresses() {
        lifecycleScope.launch {
            mViewModel.addressesFlow.collect {
                when (it) {
                    is Resource.Failure -> {
                        Log.e(TAG, "observeGetAddresses: ${it.errorCode}: ${it.errorBody}")
                    }

                    Resource.Loading -> {
                        Log.i(TAG, "observeGetAddresses: loading")
                    }

                    is Resource.Success -> {
                        Log.i(TAG, "hg add: ${it.value}")
                        if(it.value.addresses.isNotEmpty()) {
                            val address = it.value.addresses[0]
                            binding.tvName.text = address.first_name
                            binding.tvAddress.text = address.address1
                            selectedAddress = address
                        }
                        addresses = it.value.addresses.toMutableList()
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
