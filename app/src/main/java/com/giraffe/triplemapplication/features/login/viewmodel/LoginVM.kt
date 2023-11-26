package com.giraffe.triplemapplication.features.login.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.giraffe.triplemapplication.SharedVM
import com.giraffe.triplemapplication.model.repo.RepoInterface
import com.giraffe.triplemapplication.utils.Resource
import com.giraffe.triplemapplication.utils.safeCall
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch

class LoginVM(private val repo: RepoInterface) : ViewModel() {

    private val _firebaseUser: MutableStateFlow<Resource<AuthResult>> =
        MutableStateFlow(Resource.Loading)
    val currentUser: StateFlow<Resource<AuthResult>> = _firebaseUser.asStateFlow()



    fun login(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _firebaseUser.emit(safeCall { repo.signInFirebase(email, password) })
//            repo.uploadCustomerId()

        }
    }

    fun setData(email: String) {

        viewModelScope.launch(Dispatchers.IO) {

            repo.setWishListIdLocally(repo.getWishListId().first() )
            repo.setCartIdLocally( repo.getCartId().first() )
            repo.setCustomerIdLocally(repo.getCustomerByEmail(email).first().customer.id)
            Log.i("TAG", "setData: ${repo.getCustomer()}")

        }
    }

}