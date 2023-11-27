package com.giraffe.triplemapplication.features.profile.view

import android.util.Log
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
import com.giraffe.triplemapplication.utils.load
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ProfileFragment : BaseFragment<ProfileVM, FragmentProfileBinding>() {
    companion object {
        private const val TAG = "ProfileFragment"
    }

    override fun getViewModel(): Class<ProfileVM> = ProfileVM::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        b: Boolean,
    ): FragmentProfileBinding = FragmentProfileBinding.inflate(inflater, container, false)

    override fun handleView() {
        sharedViewModel.getLanguage()
        observeGetLanguage()
        mViewModel.getCurrency()
        observeGetCurrency()
    }

    override fun onResume() {
        super.onResume()
        mViewModel.getCurrency()
    }

    private fun handleFlags(currencyCode: String) {
        when (currencyCode) {
            Constants.Currencies.EGP.value -> {
                binding.ivCurrencyFlag.load("https://flagcdn.com/w320/eg.png")
            }

            Constants.Currencies.USD.value -> {
                binding.ivCurrencyFlag.load("https://flagcdn.com/w320/us.png")
            }

            Constants.Currencies.EUR.value -> {
                binding.ivCurrencyFlag.load("https://flagcdn.com/w320/eu.png")
            }

            Constants.Currencies.GBP.value -> {
                binding.ivCurrencyFlag.load("https://flagcdn.com/w320/gb.png")
            }
        }
    }

    override fun handleClicks() {
        binding.btnAddress.setOnClickListener {
            val action = ProfileFragmentDirections.actionProfileFragmentToAddressesFragment()
            findNavController().navigate(action)
        }

        binding.btnPayment.setOnClickListener {
            findNavController().navigate(R.id.favFragment)
        }
        binding.btnLanguage.setOnClickListener {
            val action = ProfileFragmentDirections.actionProfileFragmentToLanguageFragment()
            findNavController().navigate(action)
        }
        binding.btnCurrency.setOnClickListener {
            val action = ProfileFragmentDirections.actionProfileFragmentToCurrencyFragment()
            findNavController().navigate(action)
        }

        binding.ivEnterAllOrders.setOnClickListener {
            val action = ProfileFragmentDirections.actionProfileFragmentToOrdersFragment()
            findNavController().navigate(action)
        }
        binding.tvLogout.setOnClickListener {
            mViewModel.logout()
            observeLogout()
        }
    }

    private fun observeLogout() {
        lifecycleScope.launch {
            mViewModel.dataCleared
                .collectLatest { it ->
                    when (it) {
                        is Resource.Failure -> {
                            Log.e("TAG", "logout: $it")
                        }

                        Resource.Loading -> {
                            Log.e("TAG", "logout: $it")

                        }

                        is Resource.Success -> {
                            Log.d("TAG", "logout: $it")
                            mViewModel.signOut.collectLatest {it->
                                when(it){
                                    is Resource.Failure -> {
                                        Log.e("TAG", "logout: $it")

                                    }
                                    Resource.Loading -> {
                                        Log.e("TAG", "logout: $it")

                                    }
                                    is Resource.Success -> {
                                        Log.d("TAG", "logout: $it")
                                        findNavController().setGraph(R.navigation.auth_graph)

                                    }
                                }
                            }
                        }
                    }

                }
        }
    }

    private fun observeGetLanguage() {
        lifecycleScope.launch {
            sharedViewModel.languageFlow.collect {
                when (it) {
                    is Resource.Failure -> {}
                    Resource.Loading -> {}
                    is Resource.Success -> {
                        if (it.value == Constants.Languages.ARABIC.value) {
                            binding.tvLanguageCode.text = getString(R.string.arabic)
                        } else {
                            binding.tvLanguageCode.text = getString(R.string.english)
                        }
                    }
                }
            }
        }

    }

    private fun observeGetCurrency() {
        lifecycleScope.launch {
            mViewModel.currencyFlow.collect {
                when (it) {
                    is Resource.Failure -> {}
                    Resource.Loading -> {}
                    is Resource.Success -> {
                        binding.tvCurrencyCode.text = it.value
                        handleFlags(it.value)
                    }
                }
            }
        }
    }
}