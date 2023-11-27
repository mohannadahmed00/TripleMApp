package com.giraffe.triplemapplication.model.orders

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SmsMarketingConsent(
    val consent_collected_from: String,
//    val consent_updated_at: Any,
    val opt_in_level: String,
    val state: String
): Parcelable