package com.giraffe.triplemapplication.features.home.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.giraffe.triplemapplication.model.products.AllProductsResponse
import com.giraffe.triplemapplication.model.repo.RepoInterface
import com.giraffe.triplemapplication.utils.Resource
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
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
            _allProductsFlow.emit(safeApiCalls {repo.getAllProducts()})
        }
    }
    private suspend fun <T> safeApiCalls(apiCall: suspend () -> Flow<T>): Resource<T> {
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
}