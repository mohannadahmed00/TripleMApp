package com.giraffe.triplemapplication.features

import android.os.Handler
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
        }, 4000)
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
                        findNavController().navigate(action)
                    }
                }
            }
        }
    }

    override fun handleClicks() {}
}