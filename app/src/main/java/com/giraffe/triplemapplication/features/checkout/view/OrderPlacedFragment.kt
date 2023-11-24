package com.giraffe.triplemapplication.features.checkout.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.giraffe.triplemapplication.bases.BaseFragment
import com.giraffe.triplemapplication.databinding.FragmentOrderPlacedBinding
import com.giraffe.triplemapplication.features.checkout.viewmodel.CheckoutVM

class OrderPlacedFragment : BaseFragment<CheckoutVM, FragmentOrderPlacedBinding>() {
    override fun getViewModel(): Class<CheckoutVM> = CheckoutVM::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        b: Boolean
    ): FragmentOrderPlacedBinding = FragmentOrderPlacedBinding.inflate(inflater, container, false)

    override fun handleView() { }

    override fun handleClicks() {
        binding.closeButton.setOnClickListener { navigateUp() }
        binding.myOrdersButton.setOnClickListener { navigateToOrdersFragment() }
    }

    private fun navigateUp() { findNavController().navigateUp() }

    private fun navigateToOrdersFragment() {
        val action: NavDirections = OrderPlacedFragmentDirections.actionOrderPlacedFragmentToOrdersFragment()
        Navigation.findNavController(requireView()).navigate(action)
    }
}