package com.giraffe.triplemapplication.features.userinfo.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.giraffe.triplemapplication.R
import com.giraffe.triplemapplication.bases.BaseFragment
import com.giraffe.triplemapplication.databinding.FragmentUserInfoBinding
import com.giraffe.triplemapplication.features.userinfo.viewmodel.UserInfoVM

class UserInfoFragment : BaseFragment<UserInfoVM, FragmentUserInfoBinding>() {
    override fun getViewModel(): Class<UserInfoVM> = UserInfoVM::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        b: Boolean
    ): FragmentUserInfoBinding = FragmentUserInfoBinding.inflate(inflater, container, false)

    override fun handleView() {}
}