package com.giraffe.triplemapplication.features.search.view

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.giraffe.triplemapplication.R
import com.giraffe.triplemapplication.bases.BaseFragment
import com.giraffe.triplemapplication.databinding.FragmentSearchBinding
import com.giraffe.triplemapplication.features.home.adapters.OnProductClickListener
import com.giraffe.triplemapplication.features.home.adapters.ProductAdapter
import com.giraffe.triplemapplication.features.search.viewmodel.SearchVM
import com.giraffe.triplemapplication.model.products.Product
import com.giraffe.triplemapplication.model.wishlist.WishListItem
import com.giraffe.triplemapplication.utils.hide
import com.giraffe.triplemapplication.utils.show
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SearchFragment : BaseFragment<SearchVM, FragmentSearchBinding>() ,OnProductClickListener {
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
                        hideLottieAnimation()
                }else{
                    showLottieAnimation()
                }
            }
        }
    }

    private fun hideLottieAnimation() {
        binding.animationView.hide()
        binding.animationView.isActivated = false
        binding.searchRv.show()

    }

    private fun showLottieAnimation() {
        binding.animationView.show()
        binding.animationView.isActivated = true
        binding.searchRv.hide()
    }

    private fun showSuccess(products: List<Product>) {
        productsAdapter = ProductAdapter(
            sharedViewModel.isLoggedFlow.value,
            requireContext(),
            sharedViewModel.exchangeRateFlow.value,
            sharedViewModel.currencySymFlow.value,
            this
        ) { product -> navigateToProductInfo(product) }
        binding.searchRv.adapter = productsAdapter
        productsAdapter.submitList(products)


        binding.searchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val userInput: String = s.toString()

                val filteredProducts = products.filter { product ->
                    product.title!!.contains(userInput ,ignoreCase = true)

                }
                if(filteredProducts.isEmpty()){
                    showLottieAnimation()
                }else{
                    hideLottieAnimation()
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

    override fun onProductClickListener(isFav: ImageView, wishListItem: WishListItem) {
        if(isFav.id == R.drawable.fav){
            sharedViewModel.deleteWishListItemLocally(wishListItem)
            isFav.setImageResource(R.drawable.not_fav)
            showSnackBar(false , wishListItem.product)
        }else{
            sharedViewModel.insertFavorite(wishListItem)
            isFav.setImageResource(R.drawable.fav)

            showSnackBar(true , wishListItem.product)

        }
    }
    private fun showSnackBar(isAdding: Boolean, current: Product?) {
        if (isAdding) {

            Snackbar.make(
                binding.root.rootView,
                "${current!!.handle} added to wish list successfully",
                Snackbar.LENGTH_SHORT
            ).show()

        } else {
            Snackbar.make(
                binding.root.rootView,
                "${current!!.handle} deleted to wish list successfully",
                Snackbar.LENGTH_SHORT
            ).setAction("UNDO") {
                sharedViewModel.returnLastDeleted()
            }.show()

        }
    }

}