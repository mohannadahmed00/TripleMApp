package com.giraffe.triplemapplication.features.login.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.giraffe.triplemapplication.R
import com.giraffe.triplemapplication.bases.BaseFragment
import com.giraffe.triplemapplication.databinding.FragmentLoginBinding
import com.giraffe.triplemapplication.features.login.viewmodel.LoginVM
import com.giraffe.triplemapplication.utils.Resource
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class LoginFragment : BaseFragment<LoginVM, FragmentLoginBinding>() {

    override fun getViewModel(): Class<LoginVM> = LoginVM::class.java


    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        b: Boolean,

        ): FragmentLoginBinding = FragmentLoginBinding.inflate(inflater, container, false)

    override fun handleView() {

    }

    private fun observeData() {
        lifecycleScope.launch {
            mViewModel.currentUser.collectLatest { it ->
                when (it) {
                    is Resource.Failure -> {
                        showFailure(it)
                        dismissLoading()
                    }

                    Resource.Loading -> {
                        showLoading()
                    }

                    is Resource.Success -> {
                        showSuccess()
                        dismissLoading()
                    }
                }
            }
        }

    }

    private fun showSuccess() {
//        mViewModel.getData(binding.emailEditText.text.toString())
//        lifecycleScope.launch {
//            mViewModel.customerId.collectLatest {
//                if(it != null){
//                    mViewModel.setData()
//                    findNavController().setGraph(R.navigation.main_graph)
//                }else{
//                    Snackbar.make(requireView() , "Failed" , Snackbar.LENGTH_SHORT).show()
//                }
//
//            }
//        }
        mViewModel.getCustomerByEmail(binding.emailEditText.text.toString())
        lifecycleScope.launch {
            mViewModel.customer.collectLatest {
                when (it) {
                    is Resource.Failure -> {
                        Snackbar.make(requireView(), it.errorBody.toString(), Snackbar.LENGTH_SHORT)
                            .show()
                        dismissLoading()
                    }

                    Resource.Loading -> {
                        showLoading()
                    }

                    is Resource.Success -> {
//

                        mViewModel.setData(it.value.customers.first().id)
                        findNavController().setGraph(R.navigation.main_graph)
                        dismissLoading()

                    }
                }
            }
        }

    }

    private fun showFailure(it: Resource.Failure) {
        setError()
        Snackbar.make(requireView(), it.toString(), Snackbar.LENGTH_SHORT).show()

    }

    private fun setError() {
        binding.emailTextInputLayout.error = "Email may be not valid"
        binding.passwordTextInputLayout.error = "Password may be not valid"
    }


    override fun handleClicks() {
        binding.signInBtn.setOnClickListener {
            mViewModel.login(
                binding.emailEditText.text.toString(),
                binding.passwordEditText.text.toString()
            )
            observeData()
        }
        binding.signUpBtn.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToSignUpFragment()
            findNavController().navigate(action)
        }
        binding.forgetPasswordBtn.setOnClickListener {
        }
        binding.emailEditText.addTextChangedListener {
            binding.emailTextInputLayout.error = null
        }
        binding.passwordEditText.addTextChangedListener {

            binding.passwordTextInputLayout.error = null
        }
    }


}