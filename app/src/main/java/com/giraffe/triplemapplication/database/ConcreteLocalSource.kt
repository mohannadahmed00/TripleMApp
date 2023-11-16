package com.giraffe.triplemapplication.database

import android.content.Context

class ConcreteLocalSource(context: Context) : LocalSource {
    private val shared: SharedHelper = SharedHelper.getInstance(context)
    private val dao: FavoritesDao = AppDataBase.getInstance(context).getFavoritesDao()









}