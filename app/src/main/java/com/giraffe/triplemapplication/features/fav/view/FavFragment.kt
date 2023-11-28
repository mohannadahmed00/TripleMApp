package com.giraffe.triplemapplication.features.fav.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.giraffe.triplemapplication.bases.BaseFragment
import com.giraffe.triplemapplication.databinding.FragmentFavBinding
import com.giraffe.triplemapplication.features.fav.swipe.OnSwipe
import com.giraffe.triplemapplication.features.fav.swipe.SwipeGesture
import com.giraffe.triplemapplication.features.fav.viewmodel.FavVM
import com.giraffe.triplemapplication.model.wishlist.WishListItem
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class FavFragment : BaseFragment<FavVM, FragmentFavBinding>()  , OnSwipe{
    override fun getViewModel(): Class<FavVM> = FavVM::class.java
    private lateinit var adapter: FavAdapter
    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        b: Boolean,
    ): FragmentFavBinding = FragmentFavBinding.inflate(inflater, container, false)

    override fun handleView() {
        setUpSwipeGesture()
        observeData()
    }

    private fun setUpSwipeGesture() {
        val swipeGesture = object : SwipeGesture(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//                showSnackBar(products[viewHolder.adapterPosition])
                onSwipe(viewHolder.adapterPosition)


            }
        }

        val touchHelper = ItemTouchHelper(swipeGesture)

        touchHelper.attachToRecyclerView(binding.rvProducts)

    }

    private fun observeData() {
        lifecycleScope.launch {
            mViewModel.wishListItemsFlow.collectLatest {

                showSuccess(it)
            }
        }
    }

    private fun showSuccess(products: List<WishListItem>) {

        adapter = FavAdapter(exchangeRate = sharedViewModel.exchangeRateFlow.value, currency = sharedViewModel.currencySymFlow.value, onItemClick = {
            navigateToProductInfo(it)
        })
        binding.rvProducts.adapter = adapter
        adapter.submitList(products)





    }

    private fun showSnackBar(product: WishListItem) {
        val snackbar =
            Snackbar.make(requireView(), "${product.product.handle} deleted", Snackbar.LENGTH_SHORT)
        snackbar.setAction("UNDO") {
            sharedViewModel.returnLastDeleted()
        }
            .show()
    }

    private fun navigateToProductInfo(product: WishListItem) {
        sharedViewModel.setCurrentProduct(product.product)
        val action = FavFragmentDirections.actionFavFragmentToProductInfoFragment()
        findNavController().navigate(action)
    }


    override fun handleClicks() {

    }

    override fun onSwipe(position: Int) {
        val item = adapter.currentList[position]
        showSnackBar(item)
        sharedViewModel.deleteWishListItemLocally(item)

    }


}