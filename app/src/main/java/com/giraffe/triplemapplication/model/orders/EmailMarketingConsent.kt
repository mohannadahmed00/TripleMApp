package com.giraffe.triplemapplication.model.orders

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class EmailMarketingConsent(
//    val consent_updated_at: Any,
    val opt_in_level: String,
    val state: String
): Parcelable