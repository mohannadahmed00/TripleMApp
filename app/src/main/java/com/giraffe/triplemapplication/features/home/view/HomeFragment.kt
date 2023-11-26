package com.giraffe.triplemapplication.features.home.view

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.giraffe.triplemapplication.bases.BaseFragment
import com.giraffe.triplemapplication.databinding.FragmentHomeBinding
import com.giraffe.triplemapplication.features.home.adapters.BrandsAdapter
import com.giraffe.triplemapplication.features.home.adapters.CategoriesAdapter
import com.giraffe.triplemapplication.features.home.adapters.ProductAdapter
import com.giraffe.triplemapplication.features.home.adapters.SliderAdapter
import com.giraffe.triplemapplication.features.home.viewmodel.HomeVM
import com.giraffe.triplemapplication.model.products.Product
import com.giraffe.triplemapplication.utils.Constants
import com.giraffe.triplemapplication.utils.Resource
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeFragment : BaseFragment<HomeVM, FragmentHomeBinding>(), SliderAdapter.OnCodeClick {
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
        productsAdapter = ProductAdapter(requireContext()) { navigateToProductInfoScreen(it) }
        binding.productsRecyclerView.apply {
            adapter = productsAdapter
            layoutManager = LinearLayoutManager(context).apply {
                orientation = RecyclerView.HORIZONTAL
            }
        }
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

        /*images = arrayListOf()
        images.add(R.drawable.banner)
        images.add(R.drawable.banner)
        images.add(R.drawable.banner)*/


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
                        sliderAdapter = SliderAdapter(requireContext(), coupons,this@HomeFragment)
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
        lifecycleScope.launch {
            mViewModel.allProductsFlow.collect {
                when(it) {
                    is Resource.Failure -> { dismissLoading() }
                    Resource.Loading -> { showLoading() }
                    is Resource.Success -> {
                        //sharedViewModel.allProducts.emit(it.value.products)
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
}