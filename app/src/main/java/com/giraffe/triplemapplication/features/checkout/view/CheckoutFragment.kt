package com.giraffe.triplemapplication.features.checkout.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.giraffe.triplemapplication.bases.BaseFragment
import com.giraffe.triplemapplication.databinding.FragmentCheckoutBinding
import com.giraffe.triplemapplication.features.checkout.viewmodel.CheckoutVM

class CheckoutFragment : BaseFragment<CheckoutVM, FragmentCheckoutBinding>() {
    override fun getViewModel(): Class<CheckoutVM> = CheckoutVM::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        b: Boolean
    ): FragmentCheckoutBinding = FragmentCheckoutBinding.inflate(inflater, container, false)

    override fun handleView() {}

    override fun handleClicks() {
        binding.btnClose.setOnClickListener { navigateUp() }
        binding.btnCheckout.setOnClickListener { checkout() }
    }

    private fun navigateUp() { findNavController().navigateUp() }

    private fun checkout() {
        mViewModel.checkout()
    }
}
