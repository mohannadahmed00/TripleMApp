package com.giraffe.triplemapplication.features.allcategories.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.giraffe.triplemapplication.bases.BaseFragment
import com.giraffe.triplemapplication.databinding.FragmentAllCategoriesBinding
import com.giraffe.triplemapplication.features.allcategories.adapters.BrandsAdapter
import com.giraffe.triplemapplication.features.allcategories.adapters.ProductsAdapter
import com.giraffe.triplemapplication.features.allcategories.viewmodel.AllCategoriesVM
import com.giraffe.triplemapplication.model.brands.SmartCollection
import com.giraffe.triplemapplication.model.categories.CustomCollection
import com.giraffe.triplemapplication.utils.Resource
import kotlinx.coroutines.launch

class AllCategoriesFragment : BaseFragment<AllCategoriesVM, FragmentAllCategoriesBinding>() {
    override fun getViewModel(): Class<AllCategoriesVM> = AllCategoriesVM::class.java

    private lateinit var subCategoriesAdapter: ProductsAdapter
    private var isBrand = true
    private var selectedItemFromHome = 0

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        b: Boolean
    ): FragmentAllCategoriesBinding = FragmentAllCategoriesBinding.inflate(inflater, container, false)

    override fun handleView() {
        getNavArguments()

        // Recycler View
        subCategoriesAdapter = ProductsAdapter(requireContext()) { Toast.makeText(context, "$it clicked", Toast.LENGTH_SHORT).show() }
        binding.categoryRecyclerView.apply {
            adapter = subCategoriesAdapter
            layoutManager = LinearLayoutManager(context).apply {
                orientation = RecyclerView.VERTICAL
            }
        }

        if (isBrand) {
            observeGetAllBrands()
        } else {
            observeGetAllCategories()
        }
    }

    private fun getNavArguments() {
        isBrand = AllCategoriesFragmentArgs.fromBundle(requireArguments()).isBrand
        selectedItemFromHome = AllCategoriesFragmentArgs.fromBundle(requireArguments()).selectedItemFromHome
    }

    override fun handleClicks() {
        binding.closeButton.setOnClickListener { navigateUp() }
    }

    private fun observeGetAllBrands() {
        val brandsAdapter = BrandsAdapter<SmartCollection>(requireContext(), selectedItemFromHome) {
            binding.brandNameLabel.text = it.handle
        }
        binding.brandsRecyclerView.apply {
            adapter = brandsAdapter
            layoutManager = LinearLayoutManager(context).apply {
                orientation = RecyclerView.VERTICAL
            }
        }

        lifecycleScope.launch {
            mViewModel.allBrandsFlow.collect {
                when(it) {
                    is Resource.Failure -> { dismissLoading() }
                    Resource.Loading -> { showLoading() }
                    is Resource.Success -> {
                        brandsAdapter.submitList(it.value.smart_collections)
                        binding.brandNameLabel.text = it.value.smart_collections[selectedItemFromHome].handle
                        dismissLoading()
                        setVisibility()
                    }
                }
            }
        }
    }

    private fun observeGetAllCategories() {
        val categoriesAdapter = BrandsAdapter<CustomCollection>(requireContext(), selectedItemFromHome) {
            binding.brandNameLabel.text = it.handle
        }
        binding.brandsRecyclerView.apply {
            adapter = categoriesAdapter
            layoutManager = LinearLayoutManager(context).apply {
                orientation = RecyclerView.VERTICAL
            }
        }

        lifecycleScope.launch {
            mViewModel.allCategoriesFlow.collect {
                when(it) {
                    is Resource.Failure -> { dismissLoading() }
                    Resource.Loading -> { showLoading() }
                    is Resource.Success -> {
                        val categories = it.value.custom_collections.toMutableList()
                        categories.removeAt(0) // Remove front page
                        categoriesAdapter.submitList(categories)
                        binding.brandNameLabel.text = it.value.custom_collections[selectedItemFromHome + 1].handle
                        dismissLoading()
                        setVisibility()
                    }
                }
            }
        }
    }

    private fun setBrandsOrCategoriesRecyclerView() {

    }

    private fun setVisibility() {
        binding.categoryCard.visibility = View.VISIBLE
        binding.brandNameLabel.visibility = View.VISIBLE
    }

    private fun navigateUp() {
        findNavController().navigateUp()
    }
}