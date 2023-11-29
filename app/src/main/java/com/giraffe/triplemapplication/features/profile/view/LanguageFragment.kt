package com.giraffe.triplemapplication.features.profile.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.giraffe.triplemapplication.OnActivityCallback
import com.giraffe.triplemapplication.bases.BaseFragment
import com.giraffe.triplemapplication.databinding.FragmentLanguageBinding
import com.giraffe.triplemapplication.features.profile.viewmodel.ProfileVM
import com.giraffe.triplemapplication.utils.Constants
import com.giraffe.triplemapplication.utils.Resource
import com.giraffe.triplemapplication.utils.hide
import com.giraffe.triplemapplication.utils.show
import kotlinx.coroutines.launch

class LanguageFragment : BaseFragment<ProfileVM,FragmentLanguageBinding>() {
    private lateinit var onActivityCallback: OnActivityCallback
    override fun getViewModel(): Class<ProfileVM>  = ProfileVM::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        b: Boolean
    ): FragmentLanguageBinding = FragmentLanguageBinding.inflate(inflater,container,false)

    override fun handleView() {
        onActivityCallback = activity as OnActivityCallback
        sharedViewModel.getLanguage()
        observeGetLanguage()
        handleClicks()
    }

    override fun handleClicks() {

        binding.tvEnglish.setOnClickListener {
            binding.ivEnglishCorrect.show()
            binding.ivArabicCorrect.hide()
            //mViewModel.setLanguage(Constants.Languages.ENGLISH)



            onActivityCallback.onLanguageSelected(Constants.Languages.ENGLISH.value)

        }
        binding.tvArabic.setOnClickListener {
            binding.ivEnglishCorrect.hide()
            binding.ivArabicCorrect.show()
            //mViewModel.setLanguage(Constants.Languages.ARABIC)



            onActivityCallback.onLanguageSelected(Constants.Languages.ARABIC.value)
        }
    }

    private fun observeGetLanguage() {
        lifecycleScope.launch {
            sharedViewModel.languageFlow.collect{
                when(it){
                    is Resource.Failure -> {}
                    Resource.Loading -> {}
                    is Resource.Success -> {
                        if (it.value==Constants.Languages.ARABIC.value){
                            binding.ivEnglishCorrect.hide()
                            binding.ivArabicCorrect.show()
                        }else{
                            binding.ivEnglishCorrect.show()
                            binding.ivArabicCorrect.hide()
                        }
                    }


                }
            }
        }

    }
}