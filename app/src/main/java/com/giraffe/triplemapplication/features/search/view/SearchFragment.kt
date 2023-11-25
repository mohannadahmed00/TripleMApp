package com.giraffe.triplemapplication.features.search.view

import android.view.LayoutInflater
import android.view.ViewGroup
import com.giraffe.triplemapplication.bases.BaseFragment
import com.giraffe.triplemapplication.databinding.FragmentSearchBinding
import com.giraffe.triplemapplication.features.search.viewmodel.SearchVM

class SearchFragment : BaseFragment<SearchVM, FragmentSearchBinding>() {
    override fun getViewModel(): Class<SearchVM> = SearchVM::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        b: Boolean
    ): FragmentSearchBinding = FragmentSearchBinding.inflate(inflater, container, false)

    override fun handleView() {}
    override fun handleClicks() {}
}