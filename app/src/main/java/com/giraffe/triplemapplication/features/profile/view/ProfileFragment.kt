package com.giraffe.triplemapplication.features.profile.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.giraffe.triplemapplication.bases.BaseFragment
import com.giraffe.triplemapplication.databinding.FragmentProfileBinding
import com.giraffe.triplemapplication.features.profile.viewmodel.ProfileVM

class ProfileFragment : BaseFragment<ProfileVM, FragmentProfileBinding>() {
    override fun getViewModel(): Class<ProfileVM> = ProfileVM::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        b: Boolean
    ): FragmentProfileBinding = FragmentProfileBinding.inflate(inflater, container, false)

    override fun handleView() {
        binding.ivEnterLanguages.setOnClickListener {
            val action = ProfileFragmentDirections.actionProfileFragmentToLanguageFragment()
            findNavController().navigate(action)
        }
    }
}