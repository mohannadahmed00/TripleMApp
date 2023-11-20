package com.giraffe.triplemapplication.features.register.view

import android.view.LayoutInflater
import android.view.ViewGroup
import com.giraffe.triplemapplication.bases.BaseFragment
import com.giraffe.triplemapplication.databinding.FragmentSignUpBinding
import com.giraffe.triplemapplication.features.register.viewmodel.RegisterVM


class SignUpFragment : BaseFragment<RegisterVM, FragmentSignUpBinding>() {
    override fun getViewModel(): Class<RegisterVM> = RegisterVM::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        b: Boolean,
    ): FragmentSignUpBinding = FragmentSignUpBinding.inflate(inflater,container,false)

    override fun handleView() {}
    override fun handleClicks() {}


}