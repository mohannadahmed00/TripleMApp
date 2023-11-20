package com.giraffe.triplemapplication.model.repo

import com.giraffe.triplemapplication.database.LocalSource
import com.giraffe.triplemapplication.network.RemoteSource
import com.giraffe.triplemapplication.utils.Constants
import com.giraffe.triplemapplication.utils.Resource
import com.google.firebase.auth.FirebaseUser

class Repo private constructor(
    private val remoteSource: RemoteSource,
    private val localSource: LocalSource,
) : RepoInterface {
    companion object {
        @Volatile
        private var INSTANCE: Repo? = null
        fun getInstance(remoteSource: RemoteSource, localSource: LocalSource): Repo {
            return INSTANCE ?: synchronized(this) {
                val instance = Repo(remoteSource, localSource)
                INSTANCE = instance
                instance
            }
        }
    }

    override suspend fun getAllProducts() = remoteSource.getAllProducts()


    override suspend fun getLanguage() = localSource.getLanguage()

    override suspend fun setLanguage(code: Constants.Languages) = localSource.setLanguage(code)
    override suspend fun signUpFirebase(
        email: String,
        password: String,
        confirmPassword : String
    ): Resource<FirebaseUser> {
        return if(isDataValid(email , password , confirmPassword)){
            remoteSource.signUpFirebase(email, password )
        }else{
            Resource.Failure(true ,900 ,"Please check your input")
        }

    }

    private fun isDataValid(email: String, password: String, confirmPassword: String): Boolean {
        return isEmailValid(email) && isPasswordValid(password) && doPasswordsMatch(password, confirmPassword)

    }

    override suspend fun signInFirebase(email: String, password: String): Resource<FirebaseUser> =
        remoteSource.signInFirebase(email, password)

    override fun isEmailValid(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}".toRegex()
        return email.matches(emailPattern)
    }

    override fun isPasswordValid(password: String): Boolean {
        val minLength = 8 // Minimum length required for the password
        val uppercasePattern = Regex("[A-Z]") // At least one uppercase letter
        val lowercasePattern = Regex("[a-z]") // At least one lowercase letter
        val digitPattern = Regex("[0-9]") // At least one digit

        val containsUppercase = uppercasePattern.containsMatchIn(password)
        val containsLowercase = lowercasePattern.containsMatchIn(password)
        val containsDigit = digitPattern.containsMatchIn(password)
        val isLengthValid = password.length >= minLength

        return containsUppercase && containsLowercase && containsDigit && isLengthValid
    }

    override fun doPasswordsMatch(password: String , confirmPassword : String): Boolean {
        return password == confirmPassword
    }


}