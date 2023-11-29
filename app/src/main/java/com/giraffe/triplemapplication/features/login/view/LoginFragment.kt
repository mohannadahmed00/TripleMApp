package com.giraffe.triplemapplication.features.login.view

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.giraffe.triplemapplication.R
import com.giraffe.triplemapplication.bases.BaseFragment
import com.giraffe.triplemapplication.databinding.FragmentLoginBinding
import com.giraffe.triplemapplication.features.login.viewmodel.LoginVM
import com.giraffe.triplemapplication.model.cart.CartItem
import com.giraffe.triplemapplication.model.cart.response.LineItem
import com.giraffe.triplemapplication.utils.Resource
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class LoginFragment : BaseFragment<LoginVM, FragmentLoginBinding>() {
    companion object{
        private const val TAG = "LoginFragment"
    }

    override fun getViewModel(): Class<LoginVM> = LoginVM::class.java

    private var listOfLineItems: List<LineItem> = mutableListOf()
    private val listOfCartItems: MutableList<CartItem> = mutableListOf()
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
                        mViewModel.setData(it.value.customers.first().id)
                        mViewModel.setFullNameLocally(it.value.customers.first().first_name)
                        Log.i(TAG, "showSuccess: ${mViewModel.cartIdFlow.value}")
                        mViewModel.getSingleCart()
                        observeGetSingleCart()
                    }
                }
            }
        }

    }

    private fun observeGetSingleCart() {
        lifecycleScope.launch {
            mViewModel.singleCartFlow.collect{
                when(it){
                    is Resource.Failure -> {
                        Log.e(
                            TAG,
                            "observeGetSingleCart: (Failure ${it.errorCode}) ${it.errorBody}"
                        )
                    }

                    Resource.Loading -> {
                        Log.i(TAG, "observeGetSingleCart: (Loading)")
                    }

                    is Resource.Success -> {
                        Log.d(TAG, "observeGetSingleCart: (all products) ${sharedViewModel.allProducts.value}")
                        Log.d(TAG, "observeGetSingleCart: (Success) ${Gson().toJson(it.value)}")
                        if (it.value.draft_order.line_items.isEmpty()){
                            sharedViewModel.setIsLoggedFlag(true)
                            findNavController().setGraph(R.navigation.main_graph)
                            dismissLoading()
                        }else {
                            var ids = ""
                            listOfLineItems = it.value.draft_order.line_items
                            listOfLineItems.forEach { lineItem ->
                                ids += " ${lineItem.product_id},"
                            }
                            mViewModel.getListOfProducts(ids)
                            observeGetListOfProducts()
                        }
                    }
                }
            }
        }
    }

    private fun observeGetListOfProducts() {
        lifecycleScope.launch {
            mViewModel.productsFlow.collect {
                when(it){
                    is Resource.Failure -> {
                        Log.e(
                            TAG,
                            "observeGetListOfProducts: (Failure ${it.errorCode}) ${it.errorBody}"
                        )
                    }

                    Resource.Loading -> {
                        Log.i(TAG, "observeGetListOfProducts: (Loading)")
                    }

                    is Resource.Success -> {
                        Log.d(TAG, "observeGetListOfProducts: (Success) ${Gson().toJson(it.value)}")
                        Log.d(TAG, "observeGetListOfProducts: (listOfLineItems size) ${listOfLineItems.size}")
                        listOfLineItems.forEach {lineItem ->
                            val product = it.value.products.firstOrNull {p-> p.id == lineItem.product_id}
                            product?.let {pro->
                                mViewModel.insertCartItem(CartItem(lineItem.variant_id, pro,lineItem.quantity,true))
                                observeInsertCartItem()
                            }
                        }
                        sharedViewModel.setIsLoggedFlag(true)
                        findNavController().setGraph(R.navigation.main_graph)
                        dismissLoading()
                    }
                }
            }
        }
    }

    private fun observeInsertCartItem() {
        lifecycleScope.launch {
            mViewModel.cartFlow.collect {
                when (it) {
                    is Resource.Failure -> {
                        Log.e(
                            TAG,
                            "observeInsertCartItem: (Failure) ${it.errorCode}) ${it.errorBody}"
                        )
                    }

                    Resource.Loading -> {
                        Log.i(TAG, "observeInsertCartItem: (Loading)")
                    }

                    is Resource.Success -> {
                        Log.d(TAG, "observeInsertCartItem: (ROOM) insertion is successful")
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
        binding.btnGuest.setOnClickListener {
            findNavController().setGraph(R.navigation.main_graph)
        }
        binding.emailEditText.addTextChangedListener {
            binding.emailTextInputLayout.error = null
        }
        binding.passwordEditText.addTextChangedListener {

            binding.passwordTextInputLayout.error = null
        }
    }


}