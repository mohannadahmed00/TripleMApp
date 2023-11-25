package com.giraffe.triplemapplication.features.register.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.giraffe.triplemapplication.model.repo.RepoInterface
import com.giraffe.triplemapplication.utils.Resource
import com.giraffe.triplemapplication.utils.safeCall
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.HttpException

class RegisterVM(private val repo: RepoInterface) : ViewModel() {
    private val _firebaseUser: MutableStateFlow<Resource<AuthResult>> =
        MutableStateFlow(Resource.Loading)
    val currentUser: StateFlow<Resource<AuthResult>> = _firebaseUser.asStateFlow()

    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val confirmPassword = MutableLiveData<String>()
    val isEmailValid = MutableLiveData<Boolean>()
    val isPasswordValid = MutableLiveData<Boolean>()
    val doPasswordsMatch = MutableLiveData<Boolean>()

    init {
        isEmailValid.value = false
        isPasswordValid.value = false
        doPasswordsMatch.value = false
        email.observeForever {
            isEmailValid.value = repo.isEmailValid(it)
        }
        password.observeForever {
            isPasswordValid.value = repo.isPasswordValid(it)
            checkPasswordMatching()
        }
        confirmPassword.observeForever {
            checkPasswordMatching()
        }
    }

    private fun checkPasswordMatching() {
        val passwordValue = password.value ?: ""
        val confirmPasswordValue = confirmPassword.value ?: ""
        doPasswordsMatch.value = repo.doPasswordsMatch(passwordValue, confirmPasswordValue)
    }

    fun signUp(email: String, password: String, confirmPassword: String) {


        viewModelScope.launch(Dispatchers.IO) {

            _firebaseUser.emit(safeCall {
                repo.signUpFirebase(
                    email,
                    password,
                    confirmPassword
                )
            })


        }

    }

    fun isDataValid(email: String, password: String, confirmPassword: String): Boolean =
        repo.isDataValid(email, password, confirmPassword)

}