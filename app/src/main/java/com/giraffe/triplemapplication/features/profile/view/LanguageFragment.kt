package com.giraffe.triplemapplication.features.profile.view

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.giraffe.triplemapplication.AppController
import com.giraffe.triplemapplication.MainActivity
import com.giraffe.triplemapplication.bases.BaseFragment
import com.giraffe.triplemapplication.databinding.FragmentLanguageBinding
import com.giraffe.triplemapplication.features.profile.viewmodel.ProfileVM
import com.giraffe.triplemapplication.utils.Constants
import com.giraffe.triplemapplication.utils.Resource
import com.giraffe.triplemapplication.utils.hide
import com.giraffe.triplemapplication.utils.show
import com.jakewharton.processphoenix.ProcessPhoenix
import kotlinx.coroutines.launch

class LanguageFragment : BaseFragment<ProfileVM,FragmentLanguageBinding>() {
    override fun getViewModel(): Class<ProfileVM>  = ProfileVM::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        b: Boolean
    ): FragmentLanguageBinding = FragmentLanguageBinding.inflate(inflater,container,false)

    override fun handleView() {
        sharedViewModel.getLanguage()
        observeGetLanguage()
        handleClicks()
    }

    override fun handleClicks() {

        binding.btnEn.setOnClickListener {
            changeApplicationLanguage(Constants.Languages.ENGLISH, requireActivity())
            binding.ivEnglishCorrect.show()
            binding.ivArabicCorrect.hide()
        }
        binding.btnAr.setOnClickListener {
            changeApplicationLanguage(Constants.Languages.ARABIC, requireActivity())
            binding.ivEnglishCorrect.hide()
            binding.ivArabicCorrect.show()
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

    private fun changeApplicationLanguage(lang: Constants.Languages,context: Context) {
        AppController.localeManager!!.setNewLocale(context, lang.value)
        mViewModel.setLanguage(lang)
        val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        ProcessPhoenix.triggerRebirth(context, intent)
    }
}