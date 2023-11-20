package com.giraffe.triplemapplication.features.login.view

import android.view.LayoutInflater
import android.view.ViewGroup
import com.giraffe.triplemapplication.bases.BaseFragment
import com.giraffe.triplemapplication.databinding.FragmentLoginBinding
import com.giraffe.triplemapplication.features.login.viewmodel.LoginVM

class LoginFragment : BaseFragment<LoginVM ,FragmentLoginBinding >() {
    override fun getViewModel(): Class<LoginVM> = LoginVM::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        b: Boolean,
    ): FragmentLoginBinding = FragmentLoginBinding.inflate(inflater,container,false)

    override fun handleView() {}

    override fun handleClicks() {}

}