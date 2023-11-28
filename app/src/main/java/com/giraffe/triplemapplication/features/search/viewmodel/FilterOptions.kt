package com.giraffe.triplemapplication.features.search.viewmodel

object FilterOptions {
    val colorsList: MutableList<String> = mutableListOf("Blue", "Black", "White", "Red", "Gray", "Yellow")
    var currentCategory = ""
    var onSale = false
    var selectedColor = ""
    var selectedBrands : List<String>  = listOf()
    var isApplied : Boolean =false
}