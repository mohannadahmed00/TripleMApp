package com.giraffe.triplemapplication.features.home.view

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.giraffe.triplemapplication.R
import com.giraffe.triplemapplication.bases.BaseFragment
import com.giraffe.triplemapplication.databinding.FragmentHomeBinding
import com.giraffe.triplemapplication.features.home.viewmodel.HomeVM
import com.giraffe.triplemapplication.utils.Resource
import kotlinx.coroutines.launch

class HomeFragment : BaseFragment<HomeVM, FragmentHomeBinding>() {
    override fun getViewModel(): Class<HomeVM> = HomeVM::class.java

    private lateinit var recyclerAdapter: ProductAdapter
    private lateinit var images: ArrayList<Int>
    private lateinit var sliderAdapter: SliderAdapter

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        b: Boolean
    ): FragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false)

    override fun handleView() {
        // Recycler View
        recyclerAdapter = ProductAdapter(requireContext()) { Toast.makeText(context, "${it.title} clicked", Toast.LENGTH_SHORT).show() }
        binding.recyclerView.apply {
            adapter = recyclerAdapter
            layoutManager = LinearLayoutManager(context).apply {
                orientation = RecyclerView.HORIZONTAL
            }
        }

        handleClicks()

        images = arrayListOf()
        images.add(R.drawable.banner)
        images.add(R.drawable.banner)
        images.add(R.drawable.banner)
        sliderAdapter = SliderAdapter(requireContext(), images)
        binding.sliderViewPager.adapter = sliderAdapter

        observeGetAllProducts()
        observeGetAllCategories()
        observeGetAllBrands()
    }

    private fun handleClicks() {
        binding.seeAllImage.setOnClickListener { navigateToAllCategoriesScreen() }
    }

    private fun observeGetAllProducts() {
        lifecycleScope.launch {
            mViewModel.allProductsFlow.collect {
                when(it) {
                    is Resource.Failure -> { }
                    Resource.Loading -> { }
                    is Resource.Success -> {
                        recyclerAdapter.submitList(it.value.products)
                    }
                }
            }
        }
    }

    private fun observeGetAllCategories() {
        lifecycleScope.launch {
            mViewModel.allCategoriesFlow.collect {
                when(it) {
                    is Resource.Failure -> { }
                    Resource.Loading -> { }
                    is Resource.Success -> {
//                        recyclerAdapter.submitList(it.value.custom_collections)
                        Log.i("hhhhhhhhhhhhhh", "observeGetAllCategories: ${it.value}")
                    }
                }
            }
        }
    }

    private fun observeGetAllBrands() {
        lifecycleScope.launch {
            mViewModel.allBrandsFlow.collect {
                when(it) {
                    is Resource.Failure -> { }
                    Resource.Loading -> { }
                    is Resource.Success -> {
//                        recyclerAdapter.submitList(it.value.custom_collections)
                        Log.i("hhhhhhhhhhhhhh", "observeGetAllBrands: ${it.value}")
                    }
                }
            }
        }
    }

    private fun navigateToAllCategoriesScreen() {
        val action: NavDirections = HomeFragmentDirections.actionHomeFragmentToAllCategoriesFragment()
        findNavController(requireView()).navigate(action)
    }

}