package com.giraffe.triplemapplication.features.home.view

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.giraffe.triplemapplication.R
import com.giraffe.triplemapplication.bases.BaseFragment
import com.giraffe.triplemapplication.databinding.FragmentAllCategoriesBinding
import com.giraffe.triplemapplication.features.home.viewmodel.HomeVM

class AllCategoriesFragment : BaseFragment<HomeVM, FragmentAllCategoriesBinding>() {
    override fun getViewModel(): Class<HomeVM> = HomeVM::class.java
    private lateinit var recyclerAdapterAll: CategoriesAdapter
    private lateinit var recyclerAdapter: CategoryAdapter

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        b: Boolean
    ): FragmentAllCategoriesBinding = FragmentAllCategoriesBinding.inflate(inflater, container, false)

    override fun handleView() {
        binding.closeButton.setOnClickListener { navigateUp() }

        // Recycler View
        recyclerAdapterAll = CategoriesAdapter(requireContext()) { Toast.makeText(context, "${it.text} clicked", Toast.LENGTH_SHORT).show() }
        binding.categoriesRecyclerView.apply {
            adapter = recyclerAdapterAll
            layoutManager = LinearLayoutManager(context).apply {
                orientation = RecyclerView.VERTICAL
            }
        }
        val categories = listOf(Category(R.drawable.apparel, "Apparel"), Category(R.drawable.apparel, "Apparel"), Category(R.drawable.apparel, "Apparel"))
        recyclerAdapterAll.submitList(categories)
        val items = listOf("T-shirts", "Shirts", "Pants & Jeans", "Socks & Ties", "Underwear", "Jackets", "Coats", "Sweaters")
        recyclerAdapter = CategoryAdapter(items) { Toast.makeText(context, "$it clicked", Toast.LENGTH_SHORT).show() }
        binding.categoryRecyclerView.apply {
            adapter = recyclerAdapter
            layoutManager = LinearLayoutManager(context).apply {
                orientation = RecyclerView.VERTICAL
            }
        }
    }

    override fun handleClicks() {}

    private fun navigateUp() {
        findNavController().navigateUp()
    }
}