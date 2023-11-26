package com.giraffe.triplemapplication.features.search.view

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.giraffe.triplemapplication.R
import com.giraffe.triplemapplication.bases.BaseFragment
import com.giraffe.triplemapplication.databinding.FragmentSearchBinding
import com.giraffe.triplemapplication.features.allcategories.adapters.ProductsAdapter
import com.giraffe.triplemapplication.features.home.adapters.ProductAdapter
import com.giraffe.triplemapplication.features.search.viewmodel.SearchVM
import com.giraffe.triplemapplication.model.products.AllProductsResponse
import com.giraffe.triplemapplication.model.products.Product
import com.giraffe.triplemapplication.utils.Resource
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SearchFragment : BaseFragment<SearchVM, FragmentSearchBinding>() {
    override fun getViewModel(): Class<SearchVM> = SearchVM::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        b: Boolean
    ): FragmentSearchBinding = FragmentSearchBinding.inflate(inflater, container, false)

    override fun handleView() {
        observeData()

    }

    private fun observeData() {
        lifecycleScope.launch {
            mViewModel.allProductsFlow.collectLatest {it ->
                when(it){
                    is Resource.Failure -> {
                        showFailure(it)
                        dismissLoading()
                    }
                    Resource.Loading -> {
                        showLoading()
                    }
                    is Resource.Success -> {
                        showSuccess(it.value.products)
                        dismissLoading()
                    }
                }
            }
        }
    }

    private fun showSuccess(products: List<Product>) {
        val adapter = ProductAdapter(requireContext(), sharedViewModel.exchangeRateFlow.value) {navigateToProductInfo(it)}
        binding.searchRv.adapter = adapter
        adapter.submitList(products)

        binding.searchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val userInput: String = s.toString()

                val filteredProducts = products.filter { product ->
                    product.title!!.contains(userInput ,ignoreCase = true)

                }
                adapter.submitList(filteredProducts)
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

    private fun showFailure(it: Resource.Failure) {
        Snackbar.make(requireView() ,it.errorBody.toString() , Snackbar.LENGTH_SHORT).show()
    }

    override fun handleClicks() {}
}