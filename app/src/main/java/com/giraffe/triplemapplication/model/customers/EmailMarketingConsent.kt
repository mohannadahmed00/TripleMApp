package com.giraffe.triplemapplication.model.customers

data class EmailMarketingConsent(
    val state: String,

    val opt_in_level: String,

    val consent_updated_at: String?,
)
