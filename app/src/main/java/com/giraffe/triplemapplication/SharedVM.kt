package com.giraffe.triplemapplication
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.giraffe.triplemapplication.model.repo.RepoInterface
import com.giraffe.triplemapplication.utils.Resource
import com.giraffe.triplemapplication.utils.safeCall
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SharedVM(val repo: RepoInterface) : ViewModel() {
    private val _languageFlow: MutableStateFlow<Resource<String>> =
        MutableStateFlow(Resource.Loading)
    val languageFlow: StateFlow<Resource<String>> = _languageFlow.asStateFlow()
    fun getLanguage() {
        viewModelScope.launch {
            _languageFlow.emit(safeCall { repo.getLanguage() })
        }
    }
}