package com.giraffe.triplemapplication.features.fav.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.giraffe.triplemapplication.R
import com.giraffe.triplemapplication.bases.BaseFragment
import com.giraffe.triplemapplication.databinding.FragmentFavBinding
import com.giraffe.triplemapplication.features.details.view.FavAdapter
import com.giraffe.triplemapplication.features.fav.viewmodel.FavVM
import com.giraffe.triplemapplication.model.products.Product
import com.giraffe.triplemapplication.utils.Resource
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class FavFragment : BaseFragment<FavVM , FragmentFavBinding>() {
    override fun getViewModel(): Class<FavVM>  = FavVM::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        b: Boolean,
    ): FragmentFavBinding = FragmentFavBinding.inflate(inflater , container ,false)

    override fun handleView() {
        observeData()
    }

    private fun observeData() {
        lifecycleScope.launch {
            mViewModel.allFavProducts.collectLatest {it ->
                when(it){

                    is List<Product> -> {
                        showSuccess(it)
                    }
                }
            }
        }
    }

    private fun showSuccess(it: List<Product>) {
        val adapter  = FavAdapter()
        binding.rvProducts.adapter = adapter
        adapter.submitList(it)
    }



    override fun handleClicks() {

    }


}