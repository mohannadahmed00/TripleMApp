package com.giraffe.triplemapplication.features.home.view

import android.view.LayoutInflater
import android.view.ViewGroup
import com.giraffe.triplemapplication.bases.BaseFragment
import com.giraffe.triplemapplication.databinding.FragmentHomeBinding
import com.giraffe.triplemapplication.features.home.viewmodel.HomeVM

class HomeFragment : BaseFragment<HomeVM, FragmentHomeBinding>() {
    override fun getViewModel(): Class<HomeVM> = HomeVM::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        b: Boolean
    ): FragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false)

    override fun handleView() {}
}