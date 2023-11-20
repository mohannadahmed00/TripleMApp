package com.giraffe.triplemapplication.features.allcategories.view

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.giraffe.triplemapplication.bases.BaseFragment
import com.giraffe.triplemapplication.databinding.FragmentAllCategoriesBinding
import com.giraffe.triplemapplication.features.allcategories.viewmodel.AllCategoriesVM
import com.giraffe.triplemapplication.model.brands.SmartCollection
import com.giraffe.triplemapplication.utils.Resource
import kotlinx.coroutines.launch

class AllCategoriesFragment : BaseFragment<AllCategoriesVM, FragmentAllCategoriesBinding>() {
    override fun getViewModel(): Class<AllCategoriesVM> = AllCategoriesVM::class.java

    private lateinit var brandsAdapter: BrandsAdapter
    private lateinit var subCategoriesAdapter: CategoryAdapter

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        b: Boolean
    ): FragmentAllCategoriesBinding = FragmentAllCategoriesBinding.inflate(inflater, container, false)

    override fun handleView() {
        binding.closeButton.setOnClickListener { navigateUp() }

        // Recycler View
        brandsAdapter = BrandsAdapter(requireContext()) { Toast.makeText(context, "${it.handle} clicked", Toast.LENGTH_SHORT).show() }
        binding.brandsRecyclerView.apply {
            adapter = brandsAdapter
            layoutManager = LinearLayoutManager(context).apply {
                orientation = RecyclerView.VERTICAL
            }
        }

        observeGetAllBrands()
        observeGetAllCategories()

        subCategoriesAdapter = CategoryAdapter { Toast.makeText(context, "$it clicked", Toast.LENGTH_SHORT).show() }
        binding.categoryRecyclerView.apply {
            adapter = subCategoriesAdapter
            layoutManager = LinearLayoutManager(context).apply {
                orientation = RecyclerView.VERTICAL
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
                        brandsAdapter.submitList(it.value.smart_collections)
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
                        subCategoriesAdapter.submitList(it.value.custom_collections)
                    }
                }
            }
        }
    }

    private fun navigateUp() {
        findNavController().navigateUp()
    }
}