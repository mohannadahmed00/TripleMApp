package com.giraffe.triplemapplication.features.search.view

import android.view.LayoutInflater
import android.view.ViewGroup
import com.giraffe.triplemapplication.bases.BaseFragment
import com.giraffe.triplemapplication.databinding.FragmentSearchResultBinding
import com.giraffe.triplemapplication.features.search.viewmodel.SearchVM

class SearchResultFragment :  BaseFragment<SearchVM, FragmentSearchResultBinding>() {



    override fun getViewModel(): Class<SearchVM> = SearchVM::class.java


    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        b: Boolean,

    ): FragmentSearchResultBinding = FragmentSearchResultBinding.inflate(inflater,container,false)

    override fun handleView() {
   }

    override fun handleClicks() {

    }
}