package com.giraffe.triplemapplication.features.register.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.giraffe.triplemapplication.R
import com.giraffe.triplemapplication.bases.BaseFragment
import com.giraffe.triplemapplication.databinding.FragmentSignUpBinding
import com.giraffe.triplemapplication.features.register.viewmodel.RegisterVM
import com.giraffe.triplemapplication.utils.Resource
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class SignUpFragment : BaseFragment<RegisterVM, FragmentSignUpBinding>() {
    override fun getViewModel(): Class<RegisterVM> = RegisterVM::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        b: Boolean,
    ): FragmentSignUpBinding = FragmentSignUpBinding.inflate(inflater, container, false)

    override fun handleView() {

        setTextListeners()
        observeData()
        onClickListeners()
    }

    private fun onClickListeners() {
        binding.signUpBtn.setOnClickListener {
            register(binding.emailEditText.text.toString() , binding.passwordEditText.text.toString(), binding.confirmPasswordText.text.toString())
        }
        binding.loginBtn.setOnClickListener {
            navigateToLogin()
        }
    }

    private fun navigateToLogin() {
        findNavController().navigate(R.id.loginFragment)
    }

    private fun register(email: String, password: String, confirmPassword: String) {
        mViewModel.signUp(email, password, confirmPassword)
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
                        showSuccess(it)
                        dismissLoading()
                    }

                }
            }
        }

    }


    private fun setTextListeners() {
        binding.emailEditText.addTextChangedListener { editable ->
            mViewModel.email.value = editable.toString()
        }

        binding.passwordEditText.addTextChangedListener { editable ->
            mViewModel.password.value = editable.toString()
        }

        binding.confirmPasswordText.addTextChangedListener { editable ->
            mViewModel.confirmPassword.value = editable.toString()
        }
    }

    private fun observeData() {
        binding.emailEditText.doAfterTextChanged {
            mViewModel.isEmailValid.observe(this) { isValid ->
                if (!isValid) {
                    binding.emailTextInputLayout.error = "Email is not valid"
                } else {
                    binding.emailTextInputLayout.error = null
                }
            }
        }

        binding.passwordEditText.doAfterTextChanged {

            mViewModel.isPasswordValid.observe(this) { isValid ->
                if (!isValid) {
                    binding.passwordTextInputLayout.error = "Password is not valid"
                } else {
                    binding.passwordTextInputLayout.error = null
                }
            }
        }

        binding.confirmPasswordText.doAfterTextChanged {

            mViewModel.doPasswordsMatch.observe(this) { doMatch ->
                if (!doMatch) {
                    binding.confirmPasswordTextInput.error = "Passwords don't match"
                } else {
                    binding.confirmPasswordTextInput.error = null
                }
            }
        }

    }

    private fun showSuccess(it: Resource.Success<FirebaseUser>) {
        findNavController().navigate(R.id.homeFragment)
    }

    private fun showFailure(it: Resource.Failure) {
       Snackbar.make(requireView() , it.errorBody.toString() , Snackbar.LENGTH_SHORT).show()
    }


}