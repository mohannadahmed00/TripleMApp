package com.giraffe.triplemapplication.model.products

data class Review(
    val userName: String,
    val reviewContent: String,
    val date: String // Date formatted as "10 Oct 2018"
)
