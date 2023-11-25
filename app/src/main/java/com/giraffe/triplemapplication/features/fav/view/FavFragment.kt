package com.giraffe.triplemapplication.features.fav.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.giraffe.triplemapplication.R
import com.giraffe.triplemapplication.bases.BaseFragment
import com.giraffe.triplemapplication.databinding.FragmentFavBinding
import com.giraffe.triplemapplication.features.details.view.FavAdapter
import com.giraffe.triplemapplication.features.fav.swipe.SwipeGesture
import com.giraffe.triplemapplication.features.fav.viewmodel.FavVM
import com.giraffe.triplemapplication.model.products.Product
import com.giraffe.triplemapplication.utils.Resource
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class FavFragment : BaseFragment<FavVM, FragmentFavBinding>() {
    override fun getViewModel(): Class<FavVM> = FavVM::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        b: Boolean,
    ): FragmentFavBinding = FragmentFavBinding.inflate(inflater, container, false)

    override fun handleView() {
        observeData()
    }

    private fun observeData() {
        lifecycleScope.launch {
            mViewModel.allFavProducts.collectLatest { it ->
                when (it) {

                    is List<Product> -> {
                        showSuccess(it)
                    }
                }
            }
        }
    }

    private fun showSuccess(products: List<Product>) {
        val adapter = FavAdapter() { navigateToProductInfo(it) }
        val swipeGesture = object : SwipeGesture(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                showSnackBar(products[viewHolder.adapterPosition])
                mViewModel.deleteFavourite(products[viewHolder.adapterPosition])
            }


        }
        val touchHelper = ItemTouchHelper(swipeGesture)
        touchHelper.attachToRecyclerView(binding.rvProducts)
        binding.rvProducts.adapter = adapter
        adapter.submitList(products)
    }

    private fun showSnackBar(product: Product) {
        val snackbar =
            Snackbar.make(requireView(), "${product.handle} deleted", Snackbar.LENGTH_SHORT)
        snackbar.setAction("UNDO") {
            mViewModel.returnLastDeleted()
        }
            .show()
    }

    private fun navigateToProductInfo(product: Product) {
        sharedViewModel.setCurrentProduct(product)
        val action = FavFragmentDirections.actionFavFragmentToProductInfoFragment()
        findNavController().navigate(action)
    }


    override fun handleClicks() {

    }


}