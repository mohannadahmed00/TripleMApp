package com.giraffe.triplemapplication.features.checkout.view

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.giraffe.triplemapplication.R
import com.giraffe.triplemapplication.bases.BaseFragment
import com.giraffe.triplemapplication.databinding.FragmentCheckoutBinding
import com.giraffe.triplemapplication.features.checkout.adapters.ItemsAdapter
import com.giraffe.triplemapplication.features.checkout.viewmodel.CheckoutVM
import com.giraffe.triplemapplication.features.fav.view.FavAdapter
import com.giraffe.triplemapplication.features.splash.view.SplashFragment
import com.giraffe.triplemapplication.model.address.Address
import com.giraffe.triplemapplication.model.cart.Carts
import com.giraffe.triplemapplication.model.cart.ShippingAddress
import com.giraffe.triplemapplication.model.cart.request.Customer
import com.giraffe.triplemapplication.model.cart.request.DraftOrder
import com.giraffe.triplemapplication.model.orders.createorder.LineItem
import com.giraffe.triplemapplication.model.orders.createorder.Order
import com.giraffe.triplemapplication.model.orders.createorder.OrderCreate
import com.giraffe.triplemapplication.model.orders.createorder.TaxLine
import com.giraffe.triplemapplication.model.orders.createorder.Transaction
import com.giraffe.triplemapplication.utils.Constants
import com.giraffe.triplemapplication.utils.Resource
import com.google.gson.Gson
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import kotlinx.coroutines.launch

class CheckoutFragment : BaseFragment<CheckoutVM, FragmentCheckoutBinding>() {

    private lateinit var paymentSheet: PaymentSheet
    private var customerId: String = ""
    private var ephemeralKey: String = ""
    private var clientSecret: String = ""
    private var visaFlag = false
    private lateinit var adapter: ItemsAdapter


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
        val itemsString = CheckoutFragmentArgs.fromBundle(requireArguments()).items
        val items = Gson().fromJson(itemsString, Carts::class.java).carts

        adapter = ItemsAdapter(exchangeRate = sharedViewModel.exchangeRateFlow.value, currency = sharedViewModel.currencySymFlow.value, onItemClick = { })
        binding.rvItems.adapter = adapter
        adapter.submitList(items)

        mViewModel.getAddresses()
        observeGetAddresses()


        observeGetCartId()
//        observeGetCustomerDetails()

        PaymentConfiguration.init(requireContext(), Constants.STRIPE_PUBLISHED_KEY)
        paymentSheet = PaymentSheet(this) {
            onPaymentResult(it)
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
    }

    private fun onPaymentResult(paymentSheetResult: PaymentSheetResult) {
        if (paymentSheetResult is PaymentSheetResult.Completed) {
            Log.d(TAG, "onPaymentResult() called with: Success")
        } else {
            Log.e(TAG, "onPaymentResult() called with: Canceled")
        }
    }

    override fun handleClicks() {



        binding.btnClose.setOnClickListener { navigateUp() }
        binding.btnCheckout.setOnClickListener {
            if (visaFlag) {
                if (customerId != "" && ephemeralKey != "" && clientSecret != "") {
                    paymentFlow()
                } else {
                    mViewModel.createStripeCustomer()
                    observeCreateStripeCustomer()
                }
            } else {
                checkout()
            }
        }
        binding.btnCod.setOnClickListener {
            visaFlag = false
            binding.btnCod.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.red_button_stroked)
            binding.btnCod.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
            binding.btnPayVisa.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.white_button_stroked)
            binding.btnPayVisa.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.dark_gary
                )
            )
        }
        binding.btnPayVisa.setOnClickListener {
            visaFlag = true
            binding.btnPayVisa.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.red_button_stroked)
            binding.btnPayVisa.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
            binding.btnCod.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.white_button_stroked)
            binding.btnCod.setTextColor(ContextCompat.getColor(requireContext(), R.color.dark_gary))
        }
    }

    private fun observeCreateStripeCustomer() {
        showLoading()
        lifecycleScope.launch {
            mViewModel.stripeCustomerFlow.collect {
                when (it) {
                    is Resource.Failure -> {
                        dismissLoading()
                        Log.e(
                            TAG,
                            "observeCreateStripeCustomer: (Failure ${it.errorCode}) ${it.errorBody}"
                        )
                    }

                    Resource.Loading -> {
                        Log.i(TAG, "observeCreateStripeCustomer: (Loading)")
                    }

                    is Resource.Success -> {
                        Log.d(TAG, "observeCreateStripeCustomer: (Success) ${it.value}")
                        customerId = it.value.id
                        mViewModel.createEphemeralKey(customerId)
                        observeCreateEphemeralKey()
                    }
                }
            }
        }
    }

    private fun observeCreateEphemeralKey() {
        lifecycleScope.launch {
            mViewModel.ephemeralFlow.collect {
                when (it) {
                    is Resource.Failure -> {
                        dismissLoading()
                        Log.e(
                            TAG,
                            "observeCreateEphemeralKey: (Failure ${it.errorCode}) ${it.errorBody}"
                        )
                    }

                    Resource.Loading -> {
                        Log.i(TAG, "observeCreateEphemeralKey: (Loading)")
                    }

                    is Resource.Success -> {
                        Log.d(TAG, "observeCreateEphemeralKey: (Success) ${it.value}")
                        ephemeralKey = it.value.id
                        mViewModel.createPaymentIntent(customerId, "1550" + "00", "usd")
                        observeCreatePaymentIntent()

                    }
                }
            }
        }
    }

    private fun observeCreatePaymentIntent() {
        lifecycleScope.launch {
            mViewModel.paymentIntentFlow.collect {
                when (it) {
                    is Resource.Failure -> {
                        dismissLoading()
                        Log.e(
                            TAG,
                            "observeCreatePaymentIntent: (Failure ${it.errorCode}) ${it.errorBody}"
                        )
                    }

                    Resource.Loading -> {
                        Log.i(TAG, "observeCreatePaymentIntent: (Loading)")
                    }

                    is Resource.Success -> {
                        dismissLoading()
                        Log.d(TAG, "observeCreatePaymentIntent: (Success) ${it.value}")
                        clientSecret = it.value.client_secret
                        paymentFlow()

                    }
                }
            }
        }
    }

    private fun paymentFlow() {
        paymentSheet.presentWithPaymentIntent(
            clientSecret,
            PaymentSheet.Configuration(
                "Tripe M",
                PaymentSheet.CustomerConfiguration(customerId, ephemeralKey)
            )
        )
    }

    private fun navigateUp() {
        findNavController().navigateUp()
    }

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

                    Resource.Loading -> {
                        showLoading()
                    }

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
                        Log.i("hahahahahaha", "observeGetAddresses: now ${it.value.addresses}")
//                        addresses = it.value.addresses.toMutableList()
                        val dialogFragment = AddressDialogFragment(it.value.addresses.toMutableList()) {
                            Log.i("hahahahahaha", "handleClicks: $it")
                            binding.tvName.text = it.first_name
                            binding.tvAddress.text = it.address1
                            selectedAddress = it
                        }
                        binding.tvAddress.setOnClickListener {
                            dialogFragment.show(parentFragmentManager, "AddressDialogFragment")
                        }
                    }
                }
            }
        }
    }

    private fun navigateToOrderPlacedFragment() {
        val action: NavDirections =
            CheckoutFragmentDirections.actionCheckoutFragmentToOrderPlacedFragment()
        Navigation.findNavController(requireView()).navigate(action)
    }
}
