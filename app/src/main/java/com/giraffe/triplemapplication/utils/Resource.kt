package com.giraffe.triplemapplication.utils

sealed class Resource<out T> {
    data class Success<out T>(val value:T): Resource<T>()
    data class Failure(val isNetworkError:Boolean,val errorCode:Int?,val errorBody:Any? ): Resource<Nothing>()
    object  Loading: Resource<Nothing>()
}