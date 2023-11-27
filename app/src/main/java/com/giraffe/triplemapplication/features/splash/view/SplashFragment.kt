package com.giraffe.triplemapplication.features.splash.view

import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.giraffe.triplemapplication.R
import com.giraffe.triplemapplication.bases.BaseFragment
import com.giraffe.triplemapplication.databinding.FragmentSplashBinding
import com.giraffe.triplemapplication.features.splash.viewmodel.SplashVM
import com.giraffe.triplemapplication.utils.Constants
import com.giraffe.triplemapplication.utils.ExchangeRatesWorker
import com.giraffe.triplemapplication.utils.Resource
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit


class SplashFragment : BaseFragment<SplashVM, FragmentSplashBinding>() {
    companion object {
        private const val TAG = "SplashFragment"
    }

    override fun getViewModel(): Class<SplashVM> = SplashVM::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        b: Boolean,
    ): FragmentSplashBinding = FragmentSplashBinding.inflate(inflater, container, false)

    override fun handleView() {
        val fadeIn: Animation = AnimationUtils.loadAnimation(context, R.anim.fade_in)
        binding.ivLogo.startAnimation(fadeIn)

        val handler = Handler()
        handler.postDelayed({
            sharedViewModel.getSelectedCurrency()
            observeGetSelectedCurrency()
            sharedViewModel.getCartId()
            observeGetCartId()
            mViewModel.getFirstTimeFlag()
            observeGetFirstTimeFlag()


        }, 4000)
    }

    private fun observeGetSelectedCurrency() {
        lifecycleScope.launch {
            sharedViewModel.currencyFlow.collect {
                when (it) {
                    is Resource.Failure -> {
                        Log.e(
                            TAG,
                            "observeGetSelectedCurrency: (Failure ${it.errorCode}) ${it.errorBody}"
                        )
                    }

                    Resource.Loading -> {
                        Log.i(TAG, "observeGetSelectedCurrency: (Loading)")
                    }

                    is Resource.Success -> {
                        Log.d(TAG, "observeGetSelectedCurrency: (Success) ${it.value}")
                        when (it.value) {
                            Constants.Currencies.EGP.value -> {
                                sharedViewModel.getExchangeRateOf(Constants.Currencies.EGP)
                                sharedViewModel.setCurrencySymbol(Constants.Currencies.EGP.symbolRes)

                            }

                            Constants.Currencies.USD.value -> {
                                sharedViewModel.getExchangeRateOf(Constants.Currencies.USD)
                                sharedViewModel.setCurrencySymbol(Constants.Currencies.USD.symbolRes)
                            }

                            Constants.Currencies.EUR.value -> {
                                sharedViewModel.getExchangeRateOf(Constants.Currencies.EUR)
                                sharedViewModel.setCurrencySymbol(Constants.Currencies.EUR.symbolRes)
                            }

                            Constants.Currencies.GBP.value -> {
                                sharedViewModel.getExchangeRateOf(Constants.Currencies.GBP)
                                sharedViewModel.setCurrencySymbol(Constants.Currencies.GBP.symbolRes)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun runExchangeRatesWorker() {

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val requestBuilder = PeriodicWorkRequestBuilder<ExchangeRatesWorker>(
            12, TimeUnit.HOURS,
            1, TimeUnit.HOURS
        )
            .setBackoffCriteria(BackoffPolicy.LINEAR, 10L, TimeUnit.MINUTES)
            .setConstraints(constraints)
        val request = requestBuilder.build()
        WorkManager.getInstance(requireContext()).enqueueUniquePeriodicWork(
            request.id.toString(),
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }

    private fun observeGetCartId() {
        lifecycleScope.launch {
            sharedViewModel.cartIdFlow.collect {
                when (it) {
                    is Resource.Failure -> {
                        Log.e(
                            TAG,
                            "observeGetCartId: (Failure ${it.errorCode}) ${it.errorBody}"
                        )
                    }

                    Resource.Loading -> {
                        Log.i(TAG, "observeGetCartId: (Loading)")
                    }

                    is Resource.Success -> {
                        Log.d(TAG, "observeGetCartId: (Success) ${it.value}")
                    }
                }
            }
        }
    }

    private fun observeGetCurrencies() {
        lifecycleScope.launch {
            mViewModel.currenciesFlow.collect {
                when (it) {
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
                    is Resource.Failure -> {
                        Log.e(TAG, "observeGetFirstTimeFlag: ")
                    }

                    Resource.Loading -> {
                        Log.i(TAG, "observeGetFirstTimeFlag: ")
                    }

                    is Resource.Success -> {
                        mViewModel.getCurrencies()
                        observeGetCurrencies()
                        Log.d(TAG, "observeGetFirstTimeFlag: ${it.value}")
                        if (it.value) {
                            mViewModel.setFirstTimeFlag(false)
                            runExchangeRatesWorker()
                            //start currency worker here

                            //must go to onboard graph
                        } else {
                            //should check here if authorized (go to main graph) or not (go to auth graph)

                            if (mViewModel.isLoggedIn()) {
                                val action =
                                    SplashFragmentDirections.actionSplashFragmentToMainGraph()
                                findNavController().navigate(action)


                            } else {
                                val action =
                                    SplashFragmentDirections.actionSplashFragmentToAuthGraph()
                                findNavController().navigate(action)


                            }

                        }
//                        findNavController().setGraph(R.navigation.auth_graph)

                        //val action = SplashFragmentDirections.actionSplashFragmentToAuthGraph()
                        val action = SplashFragmentDirections.actionSplashFragmentToMainGraph()
                        findNavController().navigate(action)
                    }
                }
            }
        }
    }


    override fun handleClicks() {}
}