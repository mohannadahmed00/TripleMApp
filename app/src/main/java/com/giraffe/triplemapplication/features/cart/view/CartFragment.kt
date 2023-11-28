package com.giraffe.triplemapplication.features.cart.view

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import com.giraffe.triplemapplication.bases.BaseFragment
import com.giraffe.triplemapplication.databinding.FragmentCartBinding
import com.giraffe.triplemapplication.features.cart.view.adapters.CartAdapter
import com.giraffe.triplemapplication.features.cart.viewmodel.CartVM
import com.giraffe.triplemapplication.model.cart.CartItem
import com.giraffe.triplemapplication.model.cart.Carts
import com.giraffe.triplemapplication.model.cart.request.LineItem
import com.giraffe.triplemapplication.utils.Resource
import com.giraffe.triplemapplication.utils.convert
import com.google.gson.Gson
import kotlinx.coroutines.launch

class CartFragment : BaseFragment<CartVM, FragmentCartBinding>(), CartAdapter.OnCartItemClick {
    private lateinit var adapter: CartAdapter
    private var items: List<CartItem> = emptyList()

    companion object {
        private const val TAG = "CartFragment"
    }

    override fun getViewModel(): Class<CartVM> = CartVM::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        b: Boolean
    ): FragmentCartBinding = FragmentCartBinding.inflate(inflater, container, false)

    override fun handleClicks() {
        binding.btnCheckout.setOnClickListener { navigateToCheckoutFragment() }
    }

    private fun navigateToCheckoutFragment() {
        val action: NavDirections = CartFragmentDirections.actionCartFragmentToCheckoutFragment(Gson().toJson(Carts(items)))
        Navigation.findNavController(requireView()).navigate(action)
    }

    override fun handleView() {
        adapter = CartAdapter(
            mutableListOf(),
            sharedViewModel.exchangeRateFlow.value,
            sharedViewModel.currencySymFlow.value,
            this
        )
        binding.rvProducts.adapter = adapter
        mViewModel.getLocallyCartItems()
        observeGetLocallyCartItems(false)
    }

    private fun observeGetLocallyCartItems(forceUpdate: Boolean) {
        lifecycleScope.launch {
            mViewModel.cartItemsFlow.collect {
                when (it) {
                    is Resource.Failure -> {
                        Log.e(
                            TAG,
                            "observeGetLocallyCartItems: (Failure ${it.errorCode}) ${it.errorBody}"
                        )
                    }

                    Resource.Loading -> {
                        Log.i(TAG, "observeGetLocallyCartItems: (Loading)")
                    }

                    is Resource.Success -> {
                        Log.d(TAG, "observeGetLocallyCartItems: (Success)")
                        items = it.value
                        adapter.updateList(it.value)
                        val total = it.value.sumOf { cartItem ->
                            (cartItem.product.variants?.get(0)?.price?.toDouble()
                                ?: 0.0) * (cartItem.quantity)
                        }
                        binding.tvTotal.text =
                            total.convert(sharedViewModel.exchangeRateFlow.value).toString()
                                .plus(" ${requireContext().getString(sharedViewModel.currencySymFlow.value)}")
                        if (forceUpdate) {
                            val lineItems = it.value.map { cartItem ->
                                LineItem(cartItem.quantity, cartItem.variantId)
                            }
                            mViewModel.updateCartDraft(lineItems)
                            observeUpdateCartDraft()
                        }
                    }
                }
            }
        }
    }

    override fun onPlusClick(cartItem: CartItem) {
        cartItem.quantity++
        mViewModel.insertCartItem(cartItem)
        observeInsertCartItem()
    }

    override fun onMinusClick(cartItem: CartItem, position: Int) {
        cartItem.quantity--
        if (cartItem.quantity == 0) {
            mViewModel.deleteCartItemLocally(cartItem)
            observeDeleteCartItemLocally(position)
        } else {
            mViewModel.insertCartItem(cartItem)
            observeInsertCartItem()
        }
    }

    private fun observeDeleteCartItemLocally(position: Int) {
        lifecycleScope.launch {
            mViewModel.delCartItemFlow.collect {
                when (it) {
                    is Resource.Failure -> {
                        Log.e(
                            TAG,
                            "observeDeleteCartItemLocally: (Failure) ${it.errorCode}) ${it.errorBody}"
                        )
                    }

                    Resource.Loading -> {
                        Log.i(TAG, "observeDeleteCartItemLocally: (Loading)")
                    }

                    is Resource.Success -> {
                        Log.d(TAG, "observeDeleteCartItemLocally: (ROOM) insertion is successful")
                        adapter.deleteItem(position)
                        mViewModel.getLocallyCartItems()
                        observeGetLocallyCartItems(true)
                    }
                }
            }
        }
    }

    private fun observeInsertCartItem() {
        lifecycleScope.launch {
            mViewModel.cartFlow.collect {
                when (it) {
                    is Resource.Failure -> {
                        Log.e(
                            TAG,
                            "observeInsertCartItem: (Failure) ${it.errorCode}) ${it.errorBody}"
                        )
                    }

                    Resource.Loading -> {
                        Log.i(TAG, "observeInsertCartItem: (Loading)")
                    }

                    is Resource.Success -> {
                        Log.d(TAG, "observeInsertCartItem: (ROOM) insertion is successful")
                        mViewModel.getLocallyCartItems()
                        observeGetLocallyCartItems(true)
                    }
                }
            }
        }
    }

    private fun observeUpdateCartDraft() {
        lifecycleScope.launch {
            mViewModel.updateCartFlow.collect {
                when (it) {
                    is Resource.Failure -> {
                        Log.e(
                            TAG,
                            "observeUpdateCartDraft: (Failure ${it.errorCode}) ${it.errorBody}"
                        )
                    }

                    Resource.Loading -> {
                        Log.i(TAG, "observeUpdateCartDraft: (Loading)")
                    }

                    is Resource.Success -> {
                        Log.d(TAG, "observeUpdateCartDraft: (Success) ${Gson().toJson(it.value)}")
                        mViewModel.uploadCartId(it.value.draft_order.id)
                        mViewModel.insertCartIdLocally(it.value.draft_order.id)
                    }
                }
            }
        }
    }

}