package com.giraffe.triplemapplication.features.login.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.giraffe.triplemapplication.R
import com.giraffe.triplemapplication.bases.BaseFragment
import com.giraffe.triplemapplication.databinding.FragmentLoginBinding
import com.giraffe.triplemapplication.features.login.viewmodel.LoginVM

class LoginFragment : BaseFragment<LoginVM ,FragmentLoginBinding >() {
    override fun getViewModel(): Class<LoginVM>  = LoginVM::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        b: Boolean,
    ): FragmentLoginBinding =FragmentLoginBinding.inflate(inflater ,container , false)

    override fun handleView() {

    }

}