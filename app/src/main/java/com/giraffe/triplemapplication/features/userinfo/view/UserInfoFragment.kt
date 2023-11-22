package com.giraffe.triplemapplication.features.userinfo.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.giraffe.triplemapplication.R
import com.giraffe.triplemapplication.bases.BaseFragment
import com.giraffe.triplemapplication.databinding.FragmentUserInfoBinding
import com.giraffe.triplemapplication.features.userinfo.viewmodel.UserInfoVM
import com.giraffe.triplemapplication.model.customers.AddressRequest
import com.giraffe.triplemapplication.model.customers.CustomerRequest
import com.giraffe.triplemapplication.model.customers.CustomerResponse
import com.giraffe.triplemapplication.model.customers.Request
import com.giraffe.triplemapplication.utils.Resource
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import androidx.navigation.fragment.navArgs as navArgs1

class UserInfoFragment : BaseFragment<UserInfoVM, FragmentUserInfoBinding>() {
    override fun getViewModel(): Class<UserInfoVM> = UserInfoVM::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        b: Boolean,
    ): FragmentUserInfoBinding = FragmentUserInfoBinding.inflate(inflater, container, false)

    override fun handleView() {

    }

    override fun handleClicks() {

        val email = arguments?.getString("email")
        val password = arguments?.getString("password")
        binding.confirmBtn.setOnClickListener {
            if (isDataValid()) {
                Log.i("TAG", "$email and $password")
                mViewModel.createCustomer(
                    binding.nameEditText.text.toString(),
                    binding.mobileEditText.text.toString(),
                    email!!,
                    password!!

                )
                observeData()
            } else {

            }

        }

        binding.backBtn.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun isDataValid(): Boolean =
        isPhoneNumberValid(binding.mobileEditText.text.toString()) && isUserNameEmpty(binding.nameEditText.text.toString())

    private fun isPhoneNumberValid(phoneNumber: String): Boolean {
        return phoneNumber.matches("^[0-9]{10}$".toRegex())
    }

    private fun isUserNameEmpty(text: String): Boolean {
        return text.isNotEmpty()
    }


    private fun observeData() {
        lifecycleScope.launch {

            mViewModel.currentCustomer.collectLatest { it ->
                when (it) {
                    is Resource.Failure -> {
                        dismissLoading()
                        showFailure(it)
                    }

                    Resource.Loading -> {
                        showLoading()
                    }

                    is Resource.Success -> {
                        dismissLoading()
                        showSuccess()
                    }
                }
            }
        }

    }

    private fun showSuccess() {
        findNavController().setGraph(R.navigation.main_graph)
    }

    private fun showFailure(it: Resource.Failure) {
        Snackbar.make(requireView(), it.errorBody.toString(), Snackbar.LENGTH_SHORT).show()
        Log.i("TAG", "showFailure: ${it.errorBody}")
    }
}