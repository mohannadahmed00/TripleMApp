package com.giraffe.triplemapplication.model.customers

data class SmsMarketingConsent(
    val state: String,

    val opt_in_level: String,

    val consent_updated_at: String?,

    val consent_collected_from: String,
)
