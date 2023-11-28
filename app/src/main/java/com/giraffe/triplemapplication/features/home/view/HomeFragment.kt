package com.giraffe.triplemapplication.features.home.view

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.giraffe.triplemapplication.R
import com.giraffe.triplemapplication.bases.BaseFragment
import com.giraffe.triplemapplication.databinding.FragmentHomeBinding
import com.giraffe.triplemapplication.features.home.adapters.BrandsAdapter
import com.giraffe.triplemapplication.features.home.adapters.CategoriesAdapter
import com.giraffe.triplemapplication.features.home.adapters.OnProductClickListener
import com.giraffe.triplemapplication.features.home.adapters.ProductAdapter
import com.giraffe.triplemapplication.features.home.adapters.SliderAdapter
import com.giraffe.triplemapplication.features.home.viewmodel.HomeVM
import com.giraffe.triplemapplication.model.products.Product
import com.giraffe.triplemapplication.model.wishlist.WishListItem
import com.giraffe.triplemapplication.utils.Resource
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class HomeFragment : BaseFragment<HomeVM, FragmentHomeBinding>(), SliderAdapter.OnCodeClick , OnProductClickListener {
    companion object{
        private const val TAG = "HomeFragment"
    }
    override fun getViewModel(): Class<HomeVM> = HomeVM::class.java

    private lateinit var productsAdapter: ProductAdapter
    private lateinit var brandsAdapter: BrandsAdapter
    private lateinit var categoriesAdapter: CategoriesAdapter
    private lateinit var images: ArrayList<Int>
    private lateinit var sliderAdapter: SliderAdapter

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        b: Boolean
    ): FragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false)

    override fun handleView() {
        // Recycler View
        brandsAdapter = BrandsAdapter(requireContext()) { navigateToAllCategoriesScreen(true, it) }
        binding.brandsRecyclerView.apply {
            adapter = brandsAdapter
            layoutManager = LinearLayoutManager(context).apply {
                orientation = RecyclerView.HORIZONTAL
            }
        }
        categoriesAdapter = CategoriesAdapter(requireContext()) { navigateToAllCategoriesScreen(false, it) }
        binding.categoriesRecyclerView.apply {
            adapter = categoriesAdapter
            layoutManager = LinearLayoutManager(context).apply {
                orientation = RecyclerView.HORIZONTAL
            }
        }


        handleClicks()
        observeGetAllProducts()
        observeGetAllCategories()
        observeGetAllBrands()
        observeGetCoupons()
    }

    private fun observeGetCoupons() {
        lifecycleScope.launch {
            mViewModel.couponsFlow.collect{
                when(it){
                    is Resource.Failure -> {
                        Log.e(TAG, "observeGetCoupons: (Failure  ${it.errorCode}) ${it.errorBody}")
                    }
                    Resource.Loading -> {
                        Log.i(TAG, "observeGetCoupons: (Loading)")
                    }
                    is Resource.Success -> {
                        Log.d(TAG, "observeGetCoupons: ${it.value}")

                        val coupons = it.value.price_rules.map {priceRule ->
                            priceRule.title
                        }
                        sliderAdapter = SliderAdapter(requireContext(), it.value.price_rules,this@HomeFragment,sharedViewModel.exchangeRateFlow.value,sharedViewModel.currencySymFlow.value)
                        binding.sliderViewPager.adapter = sliderAdapter
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

    private fun observeGetAllCategories() {
        lifecycleScope.launch {
            mViewModel.allCategoriesFlow.collect {
                when(it) {
                    is Resource.Failure -> { dismissLoading() }
                    Resource.Loading -> { showLoading() }
                    is Resource.Success -> {
                        val categories = it.value.custom_collections.toMutableList()
                        categories.removeAt(0) // Remove front page
                        categoriesAdapter.submitList(categories)
                        dismissLoading()
                        binding.homeScreen.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun observeGetAllProducts() {

        productsAdapter = ProductAdapter(
            requireContext(),
            sharedViewModel.exchangeRateFlow.value,
            sharedViewModel.currencySymFlow.value,
            this

        ) { product -> navigateToProductInfoScreen(product) }
        lifecycleScope.launch {
            mViewModel.allProductsFlow.collect {
                when(it) {
                    is Resource.Failure -> { dismissLoading() }
                    Resource.Loading -> { showLoading() }
                    is Resource.Success -> {
                        binding.productsRecyclerView.apply {
                            adapter = productsAdapter
                            layoutManager = LinearLayoutManager(context).apply {
                                orientation = RecyclerView.HORIZONTAL
                            }
                        }
                        sharedViewModel.setAllProduct(it.value.products)
                        productsAdapter.submitList(it.value.products)
                        dismissLoading()
                        binding.homeScreen.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    override fun handleClicks() {
    }

    private fun navigateToAllCategoriesScreen(isBrand: Boolean, selectedItemFromHome: Int) {
        val action: NavDirections = HomeFragmentDirections.actionHomeFragmentToAllCategoriesFragment(isBrand, selectedItemFromHome)
        findNavController(requireView()).navigate(action)
    }

    private fun navigateToProductInfoScreen(product: Product) {
        sharedViewModel.setCurrentProduct(product)
        val action  = HomeFragmentDirections.actionHomeFragmentToProductInfoFragment()
        findNavController().navigate(action)
    }
    private fun copyToClipboard(text: String) {
        val clipboardManager = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("label", text)
        clipboardManager.setPrimaryClip(clipData)
    }

    override fun onClick(code: String) {
        copyToClipboard(code)
        Toast.makeText(requireContext(), "copied: $code", Toast.LENGTH_SHORT).show()
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