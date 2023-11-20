package com.giraffe.triplemapplication.features.splash.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.giraffe.triplemapplication.model.repo.RepoInterface
import com.giraffe.triplemapplication.utils.Resource
import com.giraffe.triplemapplication.utils.safeCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SplashVM(private val repo:RepoInterface):ViewModel() {
    private val _firstFlagFlow: MutableStateFlow<Resource<Boolean>> = MutableStateFlow(
        Resource.Loading)
    val firstFlagFlow: StateFlow<Resource<Boolean>> = _firstFlagFlow.asStateFlow()

    fun getFirstTimeFlag(){
        viewModelScope.launch(Dispatchers.IO) {
            _firstFlagFlow.emit(safeCall { repo.getFirstTimeFlag() })
        }
    }
    fun setFirstTimeFlag(flag:Boolean){
        viewModelScope.launch(Dispatchers.IO) {
            repo.setFirstTimeFlag(flag)
        }
    }
}