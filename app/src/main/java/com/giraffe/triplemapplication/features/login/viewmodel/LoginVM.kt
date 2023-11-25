package com.giraffe.triplemapplication.features.login.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.giraffe.triplemapplication.model.repo.RepoInterface
import com.giraffe.triplemapplication.utils.Resource
import com.giraffe.triplemapplication.utils.safeCall
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginVM(private val repo: RepoInterface) : ViewModel() {

    private val _firebaseUser: MutableStateFlow<Resource<AuthResult>> =
        MutableStateFlow(Resource.Loading)
    val currentUser: StateFlow<Resource<AuthResult>> = _firebaseUser.asStateFlow()


    fun login(email : String , password : String ){
        viewModelScope.launch(Dispatchers.IO) {
            _firebaseUser.emit(safeCall { repo.signInFirebase(email, password)})
        }
    }

}