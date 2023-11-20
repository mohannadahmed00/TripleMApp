package com.giraffe.triplemapplication.features.profile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.giraffe.triplemapplication.model.repo.RepoInterface
import com.giraffe.triplemapplication.utils.Constants
import kotlinx.coroutines.launch

class ProfileVM(private val repo: RepoInterface) : ViewModel() {
    fun setLanguage(code: Constants.Languages) {
        viewModelScope.launch {
            repo.setLanguage(code)
        }
    }
}