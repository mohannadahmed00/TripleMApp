package com.giraffe.triplemapplication.features.splash.view

import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.giraffe.triplemapplication.R
import com.giraffe.triplemapplication.bases.BaseFragment
import com.giraffe.triplemapplication.databinding.FragmentSplashBinding
import com.giraffe.triplemapplication.features.splash.viewmodel.SplashVM
import com.giraffe.triplemapplication.utils.Resource
import kotlinx.coroutines.launch


class SplashFragment : BaseFragment<SplashVM, FragmentSplashBinding>() {
    companion object{
        private const val TAG = "SplashFragment"
    }
    override fun getViewModel(): Class<SplashVM> = SplashVM::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        b: Boolean
    ): FragmentSplashBinding = FragmentSplashBinding.inflate(inflater, container, false)

    override fun handleView() {
        val fadeIn: Animation = AnimationUtils.loadAnimation(context, R.anim.fade_in)
        binding.ivLogo.startAnimation(fadeIn)
        val handler = Handler()
        handler.postDelayed({
            mViewModel.getFirstTimeFlag()
            observeGetFirstTimeFlag()
            mViewModel.getCurrencies()
            observeGetCurrencies()
        }, 4000)
    }

    private fun observeGetCurrencies() {
        lifecycleScope.launch { 
            mViewModel.currenciesFlow.collect{
                when (it){
                    is Resource.Failure -> {
                        Log.e(TAG, "observeGetCurrencies: ${it.errorCode}: ${it.errorBody}")
                    }
                    Resource.Loading -> {
                        Log.i(TAG, "observeGetCurrencies: ")
                    }
                    is Resource.Success -> {
                        Log.d(TAG, "observeGetCurrencies: ${it.value}")
                        mViewModel.setExchangeRates(it.value)
                    }
                }
            }
        }
    }
    private fun observeGetFirstTimeFlag() {
        lifecycleScope.launch {
            mViewModel.firstFlagFlow.collect {
                when (it) {
                    is Resource.Failure -> {}
                    Resource.Loading -> {}
                    is Resource.Success -> {
                        if (it.value){
                            mViewModel.setFirstTimeFlag(false)
                            //start currency worker here

                            //must go to onboard graph
                        }else{


                            //should check here if authorized (go to main graph) or not (go to auth graph)
                        }
                        val action = SplashFragmentDirections.actionSplashFragmentToAuthGraph()
//                        val action = SplashFragmentDirections.actionSplashFragmentToMainGraph()
                        findNavController().navigate(action)
                    }
                }
            }
        }
    }
    override fun handleClicks() {}
}