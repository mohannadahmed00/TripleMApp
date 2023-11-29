package com.giraffe.triplemapplication.features.checkout.view

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
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
import com.giraffe.triplemapplication.model.address.Address
import com.giraffe.triplemapplication.model.cart.BillingAddress
import com.giraffe.triplemapplication.model.cart.Carts
import com.giraffe.triplemapplication.model.cart.ShippingAddress
import com.giraffe.triplemapplication.model.cart.request.DraftOrder
import com.giraffe.triplemapplication.model.customers.CustomerDetails
import com.giraffe.triplemapplication.model.discount.PriceRule
import com.giraffe.triplemapplication.model.orders.createorder.DiscountCodes
import com.giraffe.triplemapplication.model.orders.createorder.LineItems
import com.giraffe.triplemapplication.model.orders.createorder.Order
import com.giraffe.triplemapplication.model.orders.createorder.OrderCreate
import com.giraffe.triplemapplication.model.orders.createorder.Transaction
import com.giraffe.triplemapplication.utils.Constants
import com.giraffe.triplemapplication.utils.Resource
import com.giraffe.triplemapplication.utils.convert
import com.google.firebase.auth.FirebaseAuth
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
    private lateinit var lineItems: LineItems
    private var customer: CustomerDetails? = null
    private var financialStatus: String = "pending"
    private var selectedAddress: Address? = null

    private var totalPrice = 0.0
    private var currency = "usd"
    var copons: List<PriceRule> = listOf()

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
        val itemsString = CheckoutFragmentArgs.fromBundle(requireArguments()).items
        val items = Gson().fromJson(itemsString, Carts::class.java).carts
        lineItems = CheckoutFragmentArgs.fromBundle(requireArguments()).lineItems
        observeGetCustomerById()

        mViewModel.getExchangeRateOfDollar()
        observeGetExchangeRateOfDollar()
        totalPrice = lineItems.lineItems.sumOf { it.price * it.quantity }


        copons = sharedViewModel.couponsFlow.value
        Log.i(TAG, "handleView: $copons")

        binding.tvTotal.text = lineItems.lineItems.sumOf { it.price * it.quantity }.convert(sharedViewModel.exchangeRateFlow.value).toString()
            .plus(getString(sharedViewModel.currencySymFlow.value))

        when (sharedViewModel.currencySymFlow.value) {
            R.string.usd_sym -> currency = "usd"
            R.string.egp_sym -> currency = "egp"
            R.string.gbp_sym -> currency = "gbp"
            R.string.eur_sym -> currency = "eur"
        }
        adapter = ItemsAdapter(
            exchangeRate = sharedViewModel.exchangeRateFlow.value,
            currency = sharedViewModel.currencySymFlow.value,
            onItemClick = { })
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

    private fun observeGetExchangeRateOfDollar() {
        lifecycleScope.launch {
            mViewModel.exchangeRateFlow.collect{
                totalPrice = totalPrice.convert(it)
            }
        }
    }

    private fun onPaymentResult(paymentSheetResult: PaymentSheetResult) {
        if (paymentSheetResult is PaymentSheetResult.Completed) {
            Log.d(TAG, "onPaymentResult() called with: Success")
            financialStatus = "paid"
        } else {
            financialStatus = "pending"
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
            financialStatus = "pending"
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
            financialStatus = "pending"
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
                        mViewModel.createPaymentIntent(
                            customerId,
                            totalPrice.toString().split(".")[0] + totalPrice.toString()
                                .split(".")[1],
                            "usd"
                        )
                        observeCreatePaymentIntent()

                    }
                }
            }
        }
    }

    private fun observeGetCustomerById() {
        lifecycleScope.launch {
            mViewModel.customerFlow.collect {
                when (it) {
                    is Resource.Failure -> {
                        dismissLoading()
                        Log.e(
                            TAG,
                            "observeGetCustomerById: (Failure ${it.errorCode}) ${it.errorBody}"
                        )
                    }

                    Resource.Loading -> {
                        Log.i(TAG, "observeGetCustomerById: (Loading)")
                    }

                    is Resource.Success -> {
                        dismissLoading()
                        Log.d(TAG, "observeGetCustomerById: (Success) ${it.value}")
                        customer = it.value

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
//        mViewModel.completeOrder()
//        observeDraftOrderOrder()
        val discountCode = binding.edtPromoCode.text.toString()
        Log.i(TAG, "discount code $discountCode")
        if (discountCode == "" || copons.any { it.title == binding.edtPromoCode.text.toString() }) {
//            val copon = copons.first { it.title == discountCode }
            Log.i(TAG, "checkout: email ${FirebaseAuth.getInstance().currentUser?.email}")
            Log.i(TAG, "checkout: phone ${FirebaseAuth.getInstance().currentUser?.phoneNumber}")
            val orderCreate = OrderCreate(
                order = Order(
                    line_items = lineItems.lineItems,
                    email = FirebaseAuth.getInstance().currentUser?.email,
                    phone = FirebaseAuth.getInstance().currentUser?.phoneNumber ?: "",
                    billingAddress = BillingAddress(
                        address1 = selectedAddress?.address1.toString(),
                        city = selectedAddress?.city.toString(),
                        country = selectedAddress?.country.toString(),
                        first_name = selectedAddress?.first_name.toString(),
                        last_name = selectedAddress?.last_name.toString(),
                        phone = selectedAddress?.phone.toString(),
                        province = selectedAddress?.province.toString(),
                        zip = selectedAddress?.zip.toString(),
                    ),
                    shippingAddress = ShippingAddress(
                        address1 = selectedAddress?.address1.toString(),
                        city = selectedAddress?.city.toString(),
                        country = selectedAddress?.country.toString(),
                        first_name = selectedAddress?.first_name.toString(),
                        last_name = selectedAddress?.last_name.toString(),
                        phone = selectedAddress?.phone.toString(),
                        province = selectedAddress?.province.toString(),
                        zip = selectedAddress?.zip.toString()
                    ),
                    transactions = listOf(
                        Transaction(
                            amount = 50.0,
                            kind = "sale",
                            status = "success"
                        )
                    ),
                    financial_status = financialStatus,
                    discount_codes = if (discountCode == "") null else listOf(
                        DiscountCodes(
                            amount = "0.0",
                            code = discountCode,//copon.title,
                            type = "percentage"
                        )
                    )
                )
            )
            mViewModel.checkout(orderCreate)
            Log.i(TAG, "checkout: create order object $orderCreate")
            observeCreateOrder()
        } else {
            Toast.makeText(
                requireContext(),
                "Please, enter a valid discount code",
                Toast.LENGTH_SHORT
            ).show()
        }
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
                        Log.i(TAG, "observeCreateOrder: ERROR ${it.errorBody}")
                    }

                    Resource.Loading -> {
                        showLoading()
                        Log.i(TAG, "observeCreateOrder: LOADING")
                    }

                    is Resource.Success -> {
                        dismissLoading()
                        navigateToOrderPlacedFragment()
                        mViewModel.removeCart()
                        Log.i(TAG, "observeCreateOrder: SUCCESS ${it.value}")
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
                        if (it.value.addresses.isNotEmpty()) {
                            val address = it.value.addresses[0]
                            binding.tvName.text = address.first_name
                            binding.tvAddress.text = address.address1
                            selectedAddress = address
                        }
                        Log.i("hahahahahaha", "observeGetAddresses: now ${it.value.addresses}")
//                        addresses = it.value.addresses.toMutableList()
                        val dialogFragment =
                            AddressDialogFragment(it.value.addresses.toMutableList()) {
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
