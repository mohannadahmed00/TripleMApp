package com.giraffe.triplemapplication.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.giraffe.triplemapplication.SharedVM
import com.giraffe.triplemapplication.features.allcategories.viewmodel.AllCategoriesVM
import com.giraffe.triplemapplication.features.cart.viewmodel.CartVM
import com.giraffe.triplemapplication.features.checkout.viewmodel.CheckoutVM
import com.giraffe.triplemapplication.features.home.viewmodel.HomeVM
import com.giraffe.triplemapplication.features.profile.viewmodel.ProfileVM
import com.giraffe.triplemapplication.features.search.viewmodel.SearchVM
import com.giraffe.triplemapplication.features.userinfo.viewmodel.UserInfoVM
import com.giraffe.triplemapplication.model.repo.RepoInterface


class ViewModelFactory(private val repo: RepoInterface) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(HomeVM::class.java)) {
            HomeVM(repo) as T
        } else if (modelClass.isAssignableFrom(SearchVM::class.java)) {
            SearchVM(repo) as T
        } else if (modelClass.isAssignableFrom(CartVM::class.java)) {
            CartVM(repo) as T
        } else if (modelClass.isAssignableFrom(ProfileVM::class.java)) {
            ProfileVM(repo) as T
        } else if (modelClass.isAssignableFrom(SharedVM::class.java)) {
            SharedVM(repo) as T
        } else if (modelClass.isAssignableFrom(CheckoutVM::class.java)) {
            CheckoutVM(repo) as T
        } else if (modelClass.isAssignableFrom(UserInfoVM::class.java)) {
            UserInfoVM(repo) as T
        } else if (modelClass.isAssignableFrom(AllCategoriesVM::class.java)) {
            AllCategoriesVM(repo) as T
        } else {
            throw IllegalArgumentException("can't create ${modelClass.simpleName}")
        }
    }
}