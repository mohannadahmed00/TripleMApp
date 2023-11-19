package com.giraffe.triplemapplication.utils

object Constants {
    private const val HOST_NAME = "android-mns-1.myshopify.com/"
    const val ACCESS_TOKEN = "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"
    private const val API_KEY = "93122f977e96731187a18ceaab5e7995"
    const val API_SECRET_KEY = "5d2f665f2512b98e159d900032075fe6"
    const val URL = "https://$API_KEY:$ACCESS_TOKEN@$HOST_NAME/admin/api/2023-10/"
    const val UNKNOWN_AREA = "UNKNOWN_AREA"
    const val LANGUAGE = "LANGUAGE"
    const val CURRENCY = "CURRENCY"



    enum class Languages(val value:String) {
        ARABIC("ar"),
        ENGLISH("en")
    }

}