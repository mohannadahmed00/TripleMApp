package com.giraffe.triplemapplication.features.cart.view

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import com.giraffe.triplemapplication.bases.BaseFragment
import com.giraffe.triplemapplication.databinding.FragmentCartBinding
import com.giraffe.triplemapplication.features.cart.viewmodel.CartVM

class CartFragment : BaseFragment<CartVM, FragmentCartBinding>() {
    override fun getViewModel(): Class<CartVM> = CartVM::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        b: Boolean
    ): FragmentCartBinding = FragmentCartBinding.inflate(inflater, container, false)

    override fun handleView() {}
    override fun handleClicks() {
        binding.btnCheckout.setOnClickListener { navigateToCheckoutFragment() }
    }

    private fun navigateToCheckoutFragment() {
        val action: NavDirections = CartFragmentDirections.actionCartFragmentToCheckoutFragment()
        Navigation.findNavController(requireView()).navigate(action)
    }
}