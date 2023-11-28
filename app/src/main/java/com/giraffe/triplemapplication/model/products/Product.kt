package com.giraffe.triplemapplication.model.products

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Parcelize
data class Product(
    val id: Long,
    val admin_graphql_api_id: String?,
    val body_html: String?,
    val created_at: String?,
    val handle: String?,
    val image: Image?,
    val images: List<Image>?,
    val options: List<Option>?,
    val product_type: String?,
    val published_at: String?,
    val published_scope: String?,
    val status: String?,
    val tags: String?,
    //val template_suffix: Any?,
    val title: String?,
    val updated_at: String?,
    val variants: List<Variant>?,
    val vendor: String?
): Parcelable