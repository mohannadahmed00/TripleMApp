package com.giraffe.triplemapplication.features.home.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.giraffe.triplemapplication.bases.BaseFragment
import com.giraffe.triplemapplication.databinding.FragmentHomeBinding
import com.giraffe.triplemapplication.features.home.viewmodel.HomeVM
import com.giraffe.triplemapplication.utils.Resource
import kotlinx.coroutines.launch

class HomeFragment : BaseFragment<HomeVM, FragmentHomeBinding>() {
    override fun getViewModel(): Class<HomeVM> = HomeVM::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        b: Boolean
    ): FragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false)

    override fun handleView() {
        mViewModel.getAllProducts()
        observeGetAllProducts()
    }

    private fun observeGetAllProducts() {
        lifecycleScope.launch {
            mViewModel.allProductsFlow.collect{
                when(it){
                    is Resource.Failure -> {
                        binding.textView.text = it.errorBody.toString()
                    }
                    Resource.Loading -> {
                        binding.textView.text = "loading"
                    }
                    is Resource.Success -> {
                        binding.textView.text = it.value.products.toString()
                    }
                }
            }
        }
    }
}