package com.giraffe.triplemapplication.features.home.view

import android.view.LayoutInflater
import android.view.View
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

    private lateinit var productsAdapter: ProductAdapter
    private lateinit var brandsAdapter: BrandsAdapter
    private lateinit var images: ArrayList<Int>
    private lateinit var sliderAdapter: SliderAdapter

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        b: Boolean
    ): FragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false)

    override fun handleView() {
        // Recycler View
        productsAdapter = ProductAdapter(requireContext()) { Toast.makeText(context, "${it.title} clicked", Toast.LENGTH_SHORT).show() }
        binding.productsRecyclerView.apply {
            adapter = productsAdapter
            layoutManager = LinearLayoutManager(context).apply {
                orientation = RecyclerView.HORIZONTAL
            }
        }
        brandsAdapter = BrandsAdapter(requireContext()) { Toast.makeText(context, "${it.title} clicked", Toast.LENGTH_SHORT).show() }
        binding.brandsRecyclerView.apply {
            adapter = brandsAdapter
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
        observeGetAllBrands()
    }

    override fun handleClicks() {}

    private fun observeGetAllProducts() {
        lifecycleScope.launch {
            mViewModel.allProductsFlow.collect {
                when(it) {
                    is Resource.Failure -> { dismissLoading() }
                    Resource.Loading -> { showLoading() }
                    is Resource.Success -> {
                        productsAdapter.submitList(it.value.products)
                        dismissLoading()
                        binding.homeScreen.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun observeGetAllBrands() {
        lifecycleScope.launch {
            mViewModel.allBrandsFlow.collect {
                when(it) {
                    is Resource.Failure -> { dismissLoading() }
                    Resource.Loading -> { showLoading() }
                    is Resource.Success -> {
                        brandsAdapter.submitList(it.value.smart_collections)
                        dismissLoading()
                        binding.homeScreen.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun handleClicks() {
        binding.brandsSeeAll.setOnClickListener { navigateToAllCategoriesScreen() }
    }

    private fun navigateToAllCategoriesScreen() {
        val action: NavDirections = HomeFragmentDirections.actionHomeFragmentToAllCategoriesFragment()
        findNavController(requireView()).navigate(action)
    }

}