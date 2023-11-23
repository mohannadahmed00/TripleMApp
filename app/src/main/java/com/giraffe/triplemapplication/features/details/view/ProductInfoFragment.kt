package com.giraffe.triplemapplication.features.details.view


import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.lifecycleScope
import com.giraffe.triplemapplication.R
import com.giraffe.triplemapplication.bases.BaseFragment
import com.giraffe.triplemapplication.databinding.FragmentProductInfoBinding
import com.giraffe.triplemapplication.features.details.viewmodel.ProductInfoVM
import com.giraffe.triplemapplication.model.products.Product
import com.giraffe.triplemapplication.model.products.Review
import com.giraffe.triplemapplication.utils.Resource
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ProductInfoFragment : BaseFragment<ProductInfoVM, FragmentProductInfoBinding>(), OnColorClickListener {
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
                    is Product ->{
                        showSuccess(it)
                    }
                }
            }
        }

    }

    private fun showSuccess(it: Product) {
        showData(it)
    }

    private fun showData(product: Product) {
        binding.imageSlider.adapter = ImagePagerAdapter(requireContext() , product.images)
        binding.productName.text = product.title
        binding.productPrice.text = product.variants?.first()?.price.toString()
        showDetailsData(product)
        showProductData(product)
        showReviewsData()

    }
    private fun createReviewsList() : List<Review> {
        val egyptianNames = listOf(
            "Amir", "Yasmine", "Mohamed", "Nour", "Ahmed", "Laila", "Tarek", "Fatma"
        )

        val reviewMessages = listOf(
            "منتج رائع جدًا!",
            "يمكن أن يكون أفضل",
            "جودة ممتازة",
            "تجربة رائعة",
            "أوصي به بشدة"

        )

        val reviews = mutableListOf<Review>()

        val totalReviews = minOf(egyptianNames.size, reviewMessages.size)
        for (i in 0 until totalReviews) {
            val userName = egyptianNames[i]
            val reviewMessage = reviewMessages[i]
            val date = "7 Oct 2023"

            val review = Review(userName, reviewMessage, date)
            reviews.add(review)
        }
        return  reviews
    }
    private fun showReviewsData() {
        val review = ReviewAdapter()
        binding.reviewsRv.adapter =review
        review.submitList(createReviewsList())
    }

    private fun showProductData(product: Product) {
        val colorsAdapter =  ColorsAdapter(this)
        binding.colorRv.adapter = colorsAdapter
        colorsAdapter.submitList(product.options?.get(1)?.values)
        val sizeAdapter = SizeAdapter()
        sizeAdapter.submitList(product.options?.get(0)?.values)
        binding.sizeRv.adapter = sizeAdapter
    }

    private fun showDetailsData(product: Product) {
        binding.brandName.text = product.vendor
        binding.statusText.text = product.status
        binding.fittingText.text = product.options!!.first().values.toString()
        binding.publishText.text = product.published_scope
        binding.serialText.text = product.id.toString()
        binding.categoryText.text = product.product_type

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

    override fun onClick(imageView: ImageView) {
        if(imageView.visibility == View.GONE){
            imageView.visibility = View.VISIBLE
        }else{
            imageView.visibility = View.GONE
        }
    }

}