package com.giraffe.triplemapplication.features.userinfo.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.giraffe.triplemapplication.model.customers.CustomerRequest
import com.giraffe.triplemapplication.model.customers.CustomerResponse
import com.giraffe.triplemapplication.model.customers.Request
import com.giraffe.triplemapplication.model.repo.RepoInterface
import com.giraffe.triplemapplication.utils.Resource
import com.giraffe.triplemapplication.utils.safeCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserInfoVM(private val repo: RepoInterface) : ViewModel() {
    private val _currentCustomer: MutableStateFlow<Resource<CustomerResponse>> =
        MutableStateFlow(Resource.Loading)
    val currentCustomer: StateFlow<Resource<CustomerResponse>> = _currentCustomer.asStateFlow()
    fun createCustomer(username: String, phone: String, email: String, password: String) {
        val customer = createRequest(username, phone , email , password)
        viewModelScope.launch(Dispatchers.IO) {
            _currentCustomer.emit(safeCall { repo.createCustomer(customer) })
        }
    }

    private fun createRequest(username: String, phone: String ,email: String, password: String): Request {
        return Request(

                CustomerRequest(
                    first_name = username,
                    email = email,
                    phone = "+20$phone",
                    verified_email = true,
                    password = password,
                    password_confirmation = password,
                    send_email_welcome = false
                )
        )
    }
}