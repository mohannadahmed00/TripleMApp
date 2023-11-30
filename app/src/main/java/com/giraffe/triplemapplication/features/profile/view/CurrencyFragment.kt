package com.giraffe.triplemapplication.features.profile.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.giraffe.triplemapplication.bases.BaseFragment
import com.giraffe.triplemapplication.databinding.FragmentCurrencyBinding
import com.giraffe.triplemapplication.features.profile.viewmodel.ProfileVM
import com.giraffe.triplemapplication.utils.Constants
import com.giraffe.triplemapplication.utils.Resource
import com.giraffe.triplemapplication.utils.hide
import com.giraffe.triplemapplication.utils.load
import com.giraffe.triplemapplication.utils.show
import kotlinx.coroutines.launch

class CurrencyFragment : BaseFragment<ProfileVM, FragmentCurrencyBinding>() {
    override fun getViewModel(): Class<ProfileVM> = ProfileVM::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        b: Boolean
    ): FragmentCurrencyBinding = FragmentCurrencyBinding.inflate(inflater, container, false)

    override fun handleView() {
        mViewModel.getCurrency()
        observeGetCurrency()
        handleFlags()
    }

    private fun handleFlags() {
        binding.ivEgp.load("https://flagcdn.com/w320/eg.png")
        binding.ivUsd.load("https://flagcdn.com/w320/us.png")
        binding.ivEur.load("https://flagcdn.com/w320/eu.png")
        binding.ivGbp.load("https://flagcdn.com/w320/gb.png")
    }

    private fun observeGetCurrency() {
        lifecycleScope.launch {
            mViewModel.currencyFlow.collect {
                when (it) {
                    is Resource.Failure -> {}
                    Resource.Loading -> {}
                    is Resource.Success -> {
                        hideAll()
                        when (it.value) {
                            Constants.Currencies.EGP.value -> {
                                binding.ivEgpCorrect.show()
                            }

                            Constants.Currencies.USD.value -> {
                                binding.ivUsdCorrect.show()
                            }

                            Constants.Currencies.EUR.value -> {
                                binding.ivEurCorrect.show()
                            }

                            Constants.Currencies.GBP.value -> {
                                binding.ivGbpCorrect.show()
                            }
                        }
                    }
                }
            }
        }
    }

    override fun handleClicks() {
        binding.btnEgp.setOnClickListener {
            hideAll()
            binding.ivEgpCorrect.show()
            sharedViewModel.getExchangeRateOf(Constants.Currencies.EGP)
            sharedViewModel.setCurrencySymbol(Constants.Currencies.EGP.symbolRes)
            mViewModel.setCurrency(Constants.Currencies.EGP)
        }
        binding.btnUsd.setOnClickListener {
            hideAll()
            binding.ivUsdCorrect.show()
            sharedViewModel.getExchangeRateOf(Constants.Currencies.USD)
            sharedViewModel.setCurrencySymbol(Constants.Currencies.USD.symbolRes)
            mViewModel.setCurrency(Constants.Currencies.USD)
        }
        binding.btnEur.setOnClickListener {
            hideAll()
            binding.ivEurCorrect.show()
            sharedViewModel.getExchangeRateOf(Constants.Currencies.EUR)
            sharedViewModel.setCurrencySymbol(Constants.Currencies.EUR.symbolRes)
            mViewModel.setCurrency(Constants.Currencies.EUR)
        }
        binding.btnGbp.setOnClickListener {
            hideAll()
            binding.ivGbpCorrect.show()
            sharedViewModel.getExchangeRateOf(Constants.Currencies.GBP)
            sharedViewModel.setCurrencySymbol(Constants.Currencies.GBP.symbolRes)
            mViewModel.setCurrency(Constants.Currencies.GBP)
        }
    }

    private fun hideAll() {
        binding.ivEgpCorrect.hide()
        binding.ivUsdCorrect.hide()
        binding.ivEurCorrect.hide()
        binding.ivGbpCorrect.hide()
    }
}