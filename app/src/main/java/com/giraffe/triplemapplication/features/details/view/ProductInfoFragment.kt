package com.giraffe.triplemapplication.features.details.view

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import com.giraffe.triplemapplication.bases.BaseFragment
import com.giraffe.triplemapplication.databinding.FragmentProductInfoBinding
import com.giraffe.triplemapplication.features.details.viewmodel.ProductInfoVM

class ProductInfoFragment : BaseFragment<ProductInfoVM, FragmentProductInfoBinding>() {
    override fun getViewModel(): Class<ProductInfoVM> = ProductInfoVM::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        b: Boolean,
    ): FragmentProductInfoBinding = FragmentProductInfoBinding.inflate(inflater,container,false)

    override fun handleView() {
        val product = ProductInfoFragmentArgs.fromBundle(requireArguments()).product
    }

    override fun handleClicks() {}

}