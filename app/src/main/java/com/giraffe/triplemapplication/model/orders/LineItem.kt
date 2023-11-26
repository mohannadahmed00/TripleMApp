package com.giraffe.triplemapplication.model.orders

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LineItem(
    val admin_graphql_api_id: String,
//    val attributed_staffs: List<Any>,
//    val discount_allocations: List<Any>,
//    val duties: List<Any>,
    val fulfillable_quantity: Int,
    val fulfillment_service: String,
//    val fulfillment_status: Any,
    val gift_card: Boolean,
    val grams: Int,
    val id: Long,
    val name: String,
    val price: String,
    val price_set: PriceSet,
    val product_exists: Boolean,
    val product_id: Long,
//    val properties: List<Any>,
    val quantity: Int,
    val requires_shipping: Boolean,
    val sku: String,
    val tax_lines: List<TaxLine>,
    val taxable: Boolean,
    val title: String,
    val total_discount: String,
    val total_discount_set: TotalDiscountSet,
    val variant_id: Long,
    val variant_inventory_management: String,
    val variant_title: String,
    val vendor: String
): Parcelable