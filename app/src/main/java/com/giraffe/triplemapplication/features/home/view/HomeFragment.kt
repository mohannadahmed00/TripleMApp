package com.giraffe.triplemapplication.features.home.view

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

        //binding.categoriesScrollView.isHorizontalScrollBarEnabled = false
        binding.seeAllImage.setOnClickListener { navigateToAllCategoriesScreen() }

        images = arrayListOf()
        images.add(R.drawable.banner)
        images.add(R.drawable.banner)
        images.add(R.drawable.banner)
        sliderAdapter = SliderAdapter(requireContext(), images)
        binding.sliderViewPager.adapter = sliderAdapter

        mViewModel.getAllProducts()
        observeGetAllProducts()

        /*lifecycleScope.launch {
            mViewModel.uiState.collect {
                recyclerAdapter.submitList(it.products)
                if (it.categories != emptyList<String?>()) {
                    binding.apparelLabel.text = it.categories[0]?.lowercase()
                    binding.beautyLabel.text = it.categories[1]?.lowercase()
                    binding.shoesLabel.text = it.categories[2]?.lowercase()
                }
            }
        }*/
    }

    private fun observeGetAllProducts() {
        lifecycleScope.launch {

        }
    }

    private fun navigateToAllCategoriesScreen() {
        val action: NavDirections = HomeFragmentDirections.actionHomeFragmentToAllCategoriesFragment()
        findNavController(requireView()).navigate(action)
    }

}