package com.giraffe.triplemapplication.features.search.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.giraffe.triplemapplication.R
import com.giraffe.triplemapplication.bases.BaseFragment
import com.giraffe.triplemapplication.databinding.FragmentFilterBinding
import com.giraffe.triplemapplication.features.search.viewmodel.SearchVM

class FilterFragment : BaseFragment<SearchVM, FragmentFilterBinding>() {
    override fun getViewModel(): Class<SearchVM> =SearchVM::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        b: Boolean,
    ): FragmentFilterBinding =FragmentFilterBinding.inflate(inflater ,container ,false)

    override fun handleView() {

    }

    override fun handleClicks() {

    }


}