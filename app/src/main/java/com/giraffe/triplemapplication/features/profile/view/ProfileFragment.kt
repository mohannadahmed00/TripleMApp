package com.giraffe.triplemapplication.features.profile.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.giraffe.triplemapplication.R
import com.giraffe.triplemapplication.bases.BaseFragment
import com.giraffe.triplemapplication.databinding.FragmentProfileBinding
import com.giraffe.triplemapplication.features.profile.viewmodel.ProfileVM
import com.giraffe.triplemapplication.utils.Constants
import com.giraffe.triplemapplication.utils.Resource
import kotlinx.coroutines.launch

class ProfileFragment : BaseFragment<ProfileVM, FragmentProfileBinding>() {
    override fun getViewModel(): Class<ProfileVM> = ProfileVM::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        b: Boolean
    ): FragmentProfileBinding = FragmentProfileBinding.inflate(inflater, container, false)

    override fun handleView() {
        sharedViewModel.getLanguage()
        observeGetLanguage()
    }
    override fun handleClicks() {
        binding.ivEnterLanguages.setOnClickListener {
            val action = ProfileFragmentDirections.actionProfileFragmentToLanguageFragment()
            findNavController().navigate(action)
        }
    }

    private fun observeGetLanguage() {
        lifecycleScope.launch {
            sharedViewModel.languageFlow.collect{
                when(it){
                    is Resource.Failure -> {}
                    Resource.Loading -> {}
                    is Resource.Success -> {
                        if (it.value== Constants.Languages.ARABIC.value){
                            binding.tvLanguageCode.text = getString(R.string.arabic)
                        }else{
                            binding.tvLanguageCode.text = getString(R.string.english)
                        }
                    }
                }
            }
        }

    }
}