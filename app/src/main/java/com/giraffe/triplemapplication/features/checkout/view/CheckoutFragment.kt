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
import com.giraffe.triplemapplication.features.checkout.viewmodel.CheckoutVM
import com.giraffe.triplemapplication.model.orders.createorder.LineItem
import com.giraffe.triplemapplication.model.orders.createorder.Order
import com.giraffe.triplemapplication.model.orders.createorder.OrderCreate
import com.giraffe.triplemapplication.model.orders.createorder.TaxLine
import com.giraffe.triplemapplication.model.orders.createorder.Transaction
import com.giraffe.triplemapplication.utils.Constants
import com.giraffe.triplemapplication.utils.Resource
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


    override fun getViewModel(): Class<CheckoutVM> = CheckoutVM::class.java

    companion object {
        private const val TAG = "CheckoutFragment"
    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        b: Boolean
    ): FragmentCheckoutBinding = FragmentCheckoutBinding.inflate(inflater, container, false)

    override fun handleView() {
        PaymentConfiguration.init(requireContext(), Constants.STRIPE_PUBLISHED_KEY)
        paymentSheet = PaymentSheet(this) {
            onPaymentResult(it)
        }

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

    private fun observeDraftOrderOrder() {
        lifecycleScope.launch {
            mViewModel.draftOrderFlow.collect {
                when (it) {
                    is Resource.Failure -> {
                        dismissLoading()
                        Log.i("hahahahaha", "observeDraftOrderOrder: ${it.errorBody}")
                    }

                    Resource.Loading -> {
                        showLoading()
                    }

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
        val action: NavDirections =
            CheckoutFragmentDirections.actionCheckoutFragmentToOrderPlacedFragment()
        Navigation.findNavController(requireView()).navigate(action)
    }
}
