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

fun Double.convert(rates:Pair<Double,Double>?) :Double {
    return if (rates != null) {
        val formattedValue = String.format("%.2f", (this / rates.first) * rates.second)
        formattedValue.toDouble()
    } else {
        0.0
    }
}
fun <T> findCommonElements(vararg lists: List<T>): List<T> {
    if (lists.isEmpty()) {
        return emptyList()
    }

    // Create a map to count occurrences of each element
    val occurrences = mutableMapOf<T, Int>()
    for (list in lists) {
        val uniqueElements = list.toSet()
        for (element in uniqueElements) {
            occurrences[element] = occurrences.getOrDefault(element, 0) + 1
        }
    }

    // Filter elements that occur in all lists
    return occurrences.filter { it.value == lists.size }.keys.toList()
}
