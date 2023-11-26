package com.giraffe.triplemapplication.features.details.view


import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.giraffe.triplemapplication.R
import com.giraffe.triplemapplication.bases.BaseFragment
import com.giraffe.triplemapplication.databinding.FragmentProductInfoBinding
import com.giraffe.triplemapplication.features.details.viewmodel.ProductInfoVM
import com.giraffe.triplemapplication.model.cart.CartItem
import com.giraffe.triplemapplication.model.cart.request.LineItem
import com.giraffe.triplemapplication.model.products.Product
import com.giraffe.triplemapplication.model.products.Review
import com.giraffe.triplemapplication.utils.Resource
import com.giraffe.triplemapplication.utils.convert
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ProductInfoFragment : BaseFragment<ProductInfoVM, FragmentProductInfoBinding>(),
    OnColorClickListener, OnSizeClickListener {
    companion object {
        private const val TAG = "ProductInfoFragment"
    }

    private var selectedColor: String? = null
    private var selectedSize: String? = null
    private lateinit var product: Product
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
                    is Product -> {
                        product = it
                        showData(it)
                    }
                }
            }
        }

    }

    private fun showSuccess(product: Product) {
        showData(product)
        binding.addToFav.setOnClickListener {
            sharedViewModel.insertFavorite(product)
            navigateToFav()
        }
        binding.addToCartButton.setOnClickListener {
            navigateToCart()
        }
    }

    private fun navigateToFav() {
        findNavController().navigate(R.id.favFragment)
    }

    private fun navigateToCart() {
        val action = ProductInfoFragmentDirections.actionProductInfoFragmentToCartFragment()
        findNavController().navigate(action)
    }

    private fun showData(product: Product) {
        binding.imageSlider.adapter = ImagePagerAdapter(requireContext(), product.images)
        binding.productName.text = product.title
        binding.productPrice.text = product.variants?.first()?.price?.toDouble()
            ?.convert(sharedViewModel.exchangeRateFlow.value).toString()
        showDetailsData(product)
        showProductData(product)
        showReviewsData()

    }

    private fun createReviewsList(): List<Review> {
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
        return reviews
    }

    private fun showReviewsData() {
        val review = ReviewAdapter()
        binding.reviewsRv.adapter = review
        review.submitList(createReviewsList())
    }

    private fun showProductData(product: Product) {
        val colorsAdapter = ColorsAdapter(this, product.options!![1].values!!)
        binding.colorRv.adapter = colorsAdapter
        val sizeAdapter = SizeAdapter(requireContext(), this, product.options[0].values!!)
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

        binding.addToCartButton.setOnClickListener {
            if (selectedColor != null && selectedSize != null) {
                val cartItem = CartItem(
                    variantId = product.variants?.first { it.option1 == selectedSize && it.option2 == selectedColor }!!.id!!.toLong(),//?????
                    product = product,
                    quantity = 1,
                    false
                )
                mViewModel.insertCartItem(cartItem)
                observeInsertCartItem()
                showSnackbar(true)
            } else {
                showSnackbar(false)
            }
        }
    }

    private fun showSnackbar(isSuccess: Boolean) {
        if (isSuccess) {

            Snackbar.make(
                requireView(),
                "Added to cart Successfully",
                Snackbar.LENGTH_SHORT
            ).show()

        } else {

            Snackbar.make(
                requireView(),
                "You cannot add to cart without choosing neither color nor size",
                Snackbar.LENGTH_SHORT
            ).show()

        }
    }

    private fun observeInsertCartItem() {
        lifecycleScope.launch {
            mViewModel.cartFlow.collect {
                when (it) {
                    is Resource.Failure -> {
                        Log.e(
                            TAG,
                            "observeInsertCartItem: (Failure) ${it.errorCode}) ${it.errorBody}"
                        )
                    }

                    Resource.Loading -> {
                        Log.i(TAG, "observeInsertCartItem: (Loading)")
                    }

                    is Resource.Success -> {
                        Log.d(TAG, "observeInsertCartItem: (ROOM) insertion is successful")
                        mViewModel.getLocallyCartItems()
                        observeGetLocallyCartItems()
                    }
                }
            }
        }
    }

    private fun observeGetLocallyCartItems() {
        lifecycleScope.launch {
            mViewModel.cartItemsFlow.collect {
                when (it) {
                    is Resource.Failure -> {
                        Log.e(TAG, "observeGetLocallyCartItems: (Failure ${it.errorCode}) ${it.errorBody}")
                    }
                    Resource.Loading -> {
                        Log.i(TAG, "observeGetLocallyCartItems: (Loading)")
                    }
                    is Resource.Success -> {
                        Log.d(TAG, "observeGetLocallyCartItems: (Success) ${it.value}")
                        val lineItems = it.value.map { cartItem ->
                            LineItem(cartItem.quantity, cartItem.variantId)
                        }
                        if (it.value.size==1){
                            mViewModel.createCartDraft(lineItems)
                            observeCreateCartDraft()
                        }else{
                            mViewModel.updateCartDraft(lineItems)
                            observeUpdateCartDraft()
                        }

                    }
                }
            }
        }
    }

    private fun observeCreateCartDraft() {
        lifecycleScope.launch {
            mViewModel.creationCartFlow.collect {
                when (it) {
                    is Resource.Failure -> {
                        Log.e(
                            TAG,
                            "observeCreateCartDraft: (Failure ${it.errorCode}) ${it.errorBody}"
                        )
                    }

                    Resource.Loading -> {
                        Log.i(TAG, "observeCreateCartDraft: (Loading)")
                    }

                    is Resource.Success -> {
                        Log.d(TAG, "observeCreateCartDraft: (Success) ${Gson().toJson(it.value)}")
                        //mViewModel.uploadCartId(it.value.draft_order.id)
                        mViewModel.insertCartIdLocally(it.value.draft_order.id)
                    }
                }
            }
        }
    }


    private fun observeUpdateCartDraft() {
        lifecycleScope.launch {
            mViewModel.updateCartFlow.collect {
                when (it) {
                    is Resource.Failure -> {
                        Log.e(
                            TAG,
                            "observeUpdateCartDraft: (Failure ${it.errorCode}) ${it.errorBody}"
                        )
                    }

                    Resource.Loading -> {
                        Log.i(TAG, "observeUpdateCartDraft: (Loading)")
                    }

                    is Resource.Success -> {
                        Log.d(TAG, "observeUpdateCartDraft: (Success) ${Gson().toJson(it.value)}")
                        //mViewModel.uploadCartId(it.value.draft_order.id)
                        mViewModel.insertCartIdLocally(it.value.draft_order.id)

                    }
                }
            }
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

    override fun onColorClickListener(color: String) {
        selectedColor = color

    }

    override fun onSizeClickListener(size: String) {
        selectedSize = size
    }

}