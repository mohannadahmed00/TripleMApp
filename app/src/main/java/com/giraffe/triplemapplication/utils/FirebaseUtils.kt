package com.giraffe.triplemapplication.utils

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resumeWithException

suspend fun <T> Task<T>.await() :T {
    return suspendCancellableCoroutine { cont ->
        addOnCompleteListener {
            if(it.exception != null ){
                cont.resumeWithException(it.exception!!)
            }else{
                sendVerificationEmail()

                cont.resume(it.result, null)
            }

        }

    }
}

private fun sendVerificationEmail() {
    if(FirebaseAuth.getInstance().currentUser != null){
        FirebaseAuth.getInstance().currentUser!!.sendEmailVerification().addOnCompleteListener {
            if(it.isSuccessful){
                Log.d("TAG", "sendVerificationEmail: ")
            }
        }
    }
}
