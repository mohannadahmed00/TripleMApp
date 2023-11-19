package com.giraffe.triplemapplication.features.profile.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.giraffe.triplemapplication.R
import com.giraffe.triplemapplication.bases.BaseFragment
import com.giraffe.triplemapplication.databinding.FragmentLanguageBinding
import com.giraffe.triplemapplication.databinding.FragmentProfileBinding
import com.giraffe.triplemapplication.features.profile.viewmodel.ProfileVM

class LanguageFragment : BaseFragment<ProfileVM,FragmentLanguageBinding>() {
    override fun getViewModel(): Class<ProfileVM>  = ProfileVM::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        b: Boolean
    ): FragmentLanguageBinding = FragmentLanguageBinding.inflate(inflater,container,false)

    override fun handleView() {

    }

}