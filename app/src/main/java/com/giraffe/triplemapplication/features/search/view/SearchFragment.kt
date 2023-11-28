package com.giraffe.triplemapplication.features.search.view

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.giraffe.triplemapplication.R
import com.giraffe.triplemapplication.bases.BaseFragment
import com.giraffe.triplemapplication.databinding.FragmentSearchBinding
import com.giraffe.triplemapplication.features.home.adapters.ProductAdapter
import com.giraffe.triplemapplication.features.search.viewmodel.SearchVM
import com.giraffe.triplemapplication.model.products.Product
import com.giraffe.triplemapplication.utils.Resource
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SearchFragment : BaseFragment<SearchVM, FragmentSearchBinding>() {
    override fun getViewModel(): Class<SearchVM> = SearchVM::class.java

    private lateinit var productsAdapter: ProductAdapter

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        b: Boolean
    ): FragmentSearchBinding = FragmentSearchBinding.inflate(inflater, container, false)

    override fun handleView() {
        findNavController().addOnDestinationChangedListener{_ , navDestination , _ ->
            when (navDestination.id) {
                R.id.searchResultFragment -> {

                    Log.i("TAG", "SearchView: ")
                }
                R.id.filterFragment -> {
                    Log.d("TAG", "FilterView: ")

                }
                else -> {
                    sharedViewModel.allProducts.value?.let { sharedViewModel.setFiltered(it) }

                    Log.e("TAG", "another: ")

                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        observeData()

    }


    private fun observeData() {
        lifecycleScope.launch {
            sharedViewModel.filteredProducts.collectLatest {
                    if(!it.isNullOrEmpty()){
                        showSuccess(it)

                }
            }
        }
    }

    private fun showSuccess(products: List<Product>) {
        lifecycleScope.launch {
            sharedViewModel.currencyFlow.collect {
                productsAdapter = if (it is Resource.Success) {
                    ProductAdapter(requireContext(), sharedViewModel.exchangeRateFlow.value, it.value) { product -> navigateToProductInfo(product) }
                } else {
                    ProductAdapter(requireContext(), sharedViewModel.exchangeRateFlow.value, "") { product -> navigateToProductInfo(product) }
                }
                binding.searchRv.adapter = productsAdapter
                productsAdapter.submitList(products)
            }
        }


        binding.searchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val userInput: String = s.toString()

                val filteredProducts = products.filter { product ->
                    product.title!!.contains(userInput ,ignoreCase = true)

                }
                productsAdapter.submitList(filteredProducts)
                // Update RecyclerView or UI with filteredProducts
                // For example, update RecyclerView adapter or UI list with the filtered data
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }


    private fun navigateToProductInfo(product: Product) {
        sharedViewModel.setCurrentProduct(product)
        findNavController().navigate(R.id.productInfoFragment)
    }



    override fun handleClicks() {
        binding.filterBtn.setOnClickListener {
            findNavController().navigate(R.id.filterFragment)
        }
    }
}