package com.giraffe.triplemapplication.features.orderdetails.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.giraffe.triplemapplication.R
import com.giraffe.triplemapplication.bases.BaseFragment
import com.giraffe.triplemapplication.databinding.FragmentOrderDetailsBinding
import com.giraffe.triplemapplication.features.orderdetails.viewmodel.OrderDetailsVM

class OrderDetailsFragment : BaseFragment<OrderDetailsVM, FragmentOrderDetailsBinding>() {
    override fun getViewModel() = OrderDetailsVM::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        b: Boolean
    ): FragmentOrderDetailsBinding = FragmentOrderDetailsBinding.inflate(inflater, container, false)

    override fun handleView() {
    }

    override fun handleClicks() {
    }

}