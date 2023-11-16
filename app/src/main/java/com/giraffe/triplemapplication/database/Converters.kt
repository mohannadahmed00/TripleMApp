package com.giraffe.triplemapplication.database

import androidx.room.TypeConverter
import com.giraffe.triplemapplication.model.products.Image
import com.giraffe.triplemapplication.model.products.Option
import com.giraffe.triplemapplication.model.products.Product
import com.giraffe.triplemapplication.model.products.Variant
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromProduct(product: Product): String {
        return Gson().toJson(product)
    }
    @TypeConverter
    fun toProduct(json: String): Product {
        return Gson().fromJson(json, Product::class.java)
    }

    @TypeConverter
    fun fromImage(image:Image): String {
        return Gson().toJson(image)
    }
    @TypeConverter
    fun toImage(json: String): Image {
        return Gson().fromJson(json, Image::class.java)
    }


    @TypeConverter
    fun fromImages(hourly: List<Image>?): String {
        return Gson().toJson(hourly)
    }
    @TypeConverter
    fun toImages(json: String): List<Image>? {
        return try {
            Gson().fromJson<List<Image>>(json)
        } catch (e: Exception) {
            arrayListOf()
        }
    }

    @TypeConverter
    fun fromOptions(options: List<Option>?): String {
        return Gson().toJson(options)
    }
    @TypeConverter
    fun toOptions(json: String): List<Option>? {
        return try {
            Gson().fromJson<List<Option>>(json)
        } catch (e: Exception) {
            arrayListOf()
        }
    }

    @TypeConverter
    fun fromVariants(variants: List<Variant>?): String {
        return Gson().toJson(variants)
    }
    @TypeConverter
    fun toVariants(json: String): List<Variant>? {
        return try {
            Gson().fromJson<List<Variant>>(json)
        } catch (e: Exception) {
            arrayListOf()
        }
    }

    private inline fun <reified T> Gson.fromJson(json: String) = fromJson<T>(json, object : TypeToken<T>() {}.type)
}
