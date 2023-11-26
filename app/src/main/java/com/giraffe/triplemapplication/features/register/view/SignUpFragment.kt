package com.giraffe.triplemapplication.features.register.view

import android.os.Bundle
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
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.AuthResult
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

    }


    private fun navigateToLogin() {
        findNavController().navigateUp()
    }

    private fun register(email: String, password: String, confirmPassword: String) {

        if(mViewModel.isDataValid(email, password, confirmPassword)){


            val bundle= Bundle()
            bundle.putString("email" , binding.emailEditText.text.toString())
            bundle.putString("password" , binding.passwordEditText.text.toString())
            findNavController().navigate(R.id.userInfoFragment , bundle)



        }else{
            Snackbar.make(requireView() , "Please check your inputs" , Snackbar.LENGTH_SHORT).show()

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







    override fun handleClicks() {
        binding.signUpBtn.setOnClickListener {
            register(binding.emailEditText.text.toString() , binding.passwordEditText.text.toString(), binding.confirmPasswordText.text.toString())
        }
        binding.loginBtn.setOnClickListener {
            navigateToLogin()
        }
    }

}