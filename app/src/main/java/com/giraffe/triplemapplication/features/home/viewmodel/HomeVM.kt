package com.giraffe.triplemapplication.features.home.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.giraffe.triplemapplication.model.products.AllProductsResponse
import com.giraffe.triplemapplication.model.repo.RepoInterface
import com.giraffe.triplemapplication.utils.Resource
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.HttpException


class HomeVM(private val repo:RepoInterface):ViewModel() {
    private val _allProductsFlow:MutableStateFlow<Resource<AllProductsResponse>> = MutableStateFlow(Resource.Loading)
    val allProductsFlow:StateFlow<Resource<AllProductsResponse>> = _allProductsFlow.asStateFlow()

    fun getAllProducts(){
        viewModelScope.launch(Dispatchers.IO) {
            when(val call = safeApiCalls { repo.getAllProducts() }){
                is Resource.Failure -> {
                    _allProductsFlow.emit(Resource.Failure(false,0,call.errorBody))
                }
                Resource.Loading -> {
                    _allProductsFlow.emit(Resource.Loading)
                }
                is Resource.Success -> {
                    call.value
                        .catch {
                            _allProductsFlow.emit(Resource.Failure(false,0,it.message))
                        }
                        .collect{
                            _allProductsFlow.emit(Resource.Success(it))
                        }
                }
            }
        }
    }


    private suspend fun <T> safeApiCalls(apiCall: suspend () -> T): Resource<T> {
        return withContext(Dispatchers.IO) {
            try {
                Resource.Success(apiCall.invoke())
            } catch (throwable: Throwable) {
                Log.e("BaseRepo->","$throwable")
                when (throwable) {
                    is HttpException -> {
                        Log.e("BaseRepoHttpErrorBody->","${throwable.response()!!.errorBody()}")
                        Resource.Failure(
                            false,
                            throwable.code(),
                            throwable.response()!!.errorBody() as ResponseBody
                        )
                    }
                    is JsonSyntaxException -> {
                        Log.e("BaseRepoJsonErrorBody->", throwable.localizedMessage)
                        Resource.Failure(
                            false,
                            0,
                            throwable.localizedMessage
                        )
                    }
                    else -> {
                        Resource.Failure(true, null, null)
                    }
                }
            }
        }
    }
}