package com.giraffe.triplemapplication
import androidx.lifecycle.ViewModel
import com.giraffe.triplemapplication.model.repo.RepoInterface

class SharedVM(val repo: RepoInterface) : ViewModel() {
}