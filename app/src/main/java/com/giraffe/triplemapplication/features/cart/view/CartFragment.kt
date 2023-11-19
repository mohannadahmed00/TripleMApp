package com.giraffe.triplemapplication.features.cart.view

import android.view.LayoutInflater
import android.view.ViewGroup
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
}