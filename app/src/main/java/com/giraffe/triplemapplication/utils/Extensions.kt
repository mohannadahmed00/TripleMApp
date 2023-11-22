package com.giraffe.triplemapplication.utils

import android.util.Log
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response


fun View.hide(){
    this.visibility = View.INVISIBLE
}

fun View.show(){
    this.visibility = View.VISIBLE
}

fun View.gone(){
    this.visibility = View.GONE
}

suspend fun <T> safeCall(apiCall: suspend () -> Flow<T>): Resource<T>{
    return withContext(Dispatchers.IO) {
        var resource:Resource<T> = Resource.Loading
        try {
            apiCall.invoke()
                .catch {
                    resource = Resource.Failure(true,0,it)
                }.collect{
                    resource = Resource.Success(it)
                }
        }catch (throwable: Throwable){
            when (throwable) {
                is HttpException -> {
                    Log.e("BaseRepoHttpErrorBody->","${throwable.response()!!.errorBody()}")
                    resource = Resource.Failure(
                        false,
                        throwable.code(),
                        throwable.response()!!.errorBody() as ResponseBody
                    )
                }
                is JsonSyntaxException -> {
                    Log.e("BaseRepoJsonErrorBody->", throwable.localizedMessage)
                    resource = Resource.Failure(
                        false,
                        0,
                        throwable.localizedMessage
                    )
                }
                else -> {
                    resource = Resource.Failure(true, null, null)
                }
            }
        }
        resource
    }
}

suspend fun <T> safeApiCall(apiCall: suspend () -> Flow<Response<T>>): Resource<T>{
    return withContext(Dispatchers.IO) {
        var resource:Resource<T> = Resource.Loading
        try {
            apiCall.invoke()
                .catch {
                    resource = Resource.Failure(true,0,it)
                }.collect{
                    resource = if (it.isSuccessful && it.body()!=null){
                        Resource.Success(it.body()!!)
                    }else{
                        Resource.Failure(true,it.code(),it.errorBody())
                    }

                }
        }catch (throwable: Throwable){
            when (throwable) {
                is HttpException -> {
                    Log.e("BaseRepoHttpErrorBody->","${throwable.response()!!.errorBody()}")
                    resource = Resource.Failure(
                        false,
                        throwable.code(),
                        throwable.response()!!.errorBody() as ResponseBody
                    )
                }
                is JsonSyntaxException -> {
                    Log.e("BaseRepoJsonErrorBody->", throwable.localizedMessage)
                    resource = Resource.Failure(
                        false,
                        0,
                        throwable.localizedMessage
                    )
                }
                else -> {
                    resource = Resource.Failure(true, null, null)
                }
            }
        }
        resource
    }
}

fun ImageView.load(url:String){
    Glide.with(this.context).load(url).into(this)
}

fun convert(euro:Double,selectedCurrency:Double){

}