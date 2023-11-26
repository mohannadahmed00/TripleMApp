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
import com.giraffe.triplemapplication.utils.Resource
import com.giraffe.triplemapplication.utils.convert
import kotlinx.coroutines.launch

class CartFragment : BaseFragment<CartVM, FragmentCartBinding>() {
    private lateinit var adapter: CartAdapter
    companion object{
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
        val action: NavDirections = CartFragmentDirections.actionCartFragmentToCheckoutFragment()
        Navigation.findNavController(requireView()).navigate(action)
    }
    override fun handleView() {
        adapter = CartAdapter(mutableListOf(),sharedViewModel.exchangeRateFlow.value,sharedViewModel.currencySymFlow.value)
        binding.rvProducts.adapter = adapter
        mViewModel.getCartItems()
        observeGetCartItems()
    }

    private fun observeGetCartItems() {
        lifecycleScope.launch {
            mViewModel.cartFlow.collect{
                when(it){
                    is Resource.Failure -> {
                        Log.e(
                            TAG,
                            "observeGetCartItems: (Failure ${it.errorCode}) ${it.errorBody}"
                        )
                    }
                    Resource.Loading -> {
                        Log.i(TAG, "observeGetCartItems: (Loading)")
                    }
                    is Resource.Success -> {
                        Log.d(TAG, "observeGetCartItems: (Success)")
                        adapter.updateList(it.value)
                        val total = it.value.sumOf { cartItem ->
                            cartItem.product.variants?.get(0)?.price?.toDouble() ?: 0.0
                        }
                        binding.tvTotal.text = total.convert(sharedViewModel.exchangeRateFlow.value).toString()
                            .plus(" ${requireContext().getString(sharedViewModel.currencySymFlow.value)}")
                    }
                }
            }
        }
    }

}