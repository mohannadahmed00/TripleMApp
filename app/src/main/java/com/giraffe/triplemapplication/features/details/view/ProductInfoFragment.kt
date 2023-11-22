package com.giraffe.triplemapplication.features.details.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import com.giraffe.triplemapplication.R
import com.giraffe.triplemapplication.bases.BaseFragment
import com.giraffe.triplemapplication.databinding.FragmentProductInfoBinding
import com.giraffe.triplemapplication.features.details.viewmodel.ProductInfoVM
import com.giraffe.triplemapplication.model.products.Product
import com.giraffe.triplemapplication.utils.Resource
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ProductInfoFragment : BaseFragment<ProductInfoVM, FragmentProductInfoBinding>() {
    override fun getViewModel(): Class<ProductInfoVM> = ProductInfoVM::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        b: Boolean,
    ): FragmentProductInfoBinding = FragmentProductInfoBinding.inflate(inflater, container, false)

    override fun handleView() {
//        val product = ProductInfoFragmentArgs.fromBundle(requireArguments()).product

        observeData()
    }

    private fun observeData() {
        lifecycleScope.launch {
            sharedViewModel.currentProduct.collectLatest { it ->
                when (it) {
                    is Resource.Failure -> {
                        dismissLoading()
                        showFailure(it)
                    }

                    Resource.Loading -> {
                        showLoading()
                    }

                    is Resource.Success -> {
                        dismissLoading()
                        showSuccess(it)
                    }

                }
            }
        }

    }

    private fun showSuccess(it: Resource.Success<Product>) {
        showData(it.value)
    }

    private fun showData(product: Product) {
        binding.imageSlider.adapter = ImagePagerAdapter(requireContext() , product.images)
        binding.brandName.text = product.vendor
        binding.productName.text = product.title
        binding.productPrice.text = product.variants!!.first().price.toString()
        binding.skuText.text = product.variants!!.first().sku.toString()
        binding.fittingText.text = product.variants!!.first().option1.toString()
        binding.conditionName.text = product.status.toString()
        binding.categoryText.text = product.product_type


    }

    private fun showFailure(it: Resource.Failure) {
        Snackbar.make(requireView() , it.errorBody.toString() , Snackbar.LENGTH_SHORT).show()
    }


    override fun handleClicks() {
        binding.viewChangerRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            showSelectedLayout(checkedId)
        }
    }

    private fun showSelectedLayout(selectedLayoutId: Int) {


        when (selectedLayoutId) {
            R.id.product_radio -> {
                binding.productLayout.visibility = View.VISIBLE
                binding.detailsLayout.visibility = View.GONE
                binding.reviewsLayout.visibility = View.GONE
            }
            R.id.details_radio -> {
                binding.productLayout.visibility = View.GONE
                binding.detailsLayout.visibility = View.VISIBLE
                binding.reviewsLayout.visibility = View.GONE
            }
            R.id.reviews_radio -> {
                binding.productLayout.visibility = View.GONE
                binding.detailsLayout.visibility = View.GONE
                binding.reviewsLayout.visibility = View.VISIBLE
            }
        }


    }

}