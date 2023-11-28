package com.giraffe.triplemapplication.utils

import com.giraffe.triplemapplication.R

object Constants {
    private const val HOST_NAME = "android-mns-1.myshopify.com/"
    const val ACCESS_TOKEN = "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"
    private const val API_KEY = "93122f977e96731187a18ceaab5e7995"
    const val API_SECRET_KEY = "5d2f665f2512b98e159d900032075fe6"
    const val URL = "https://$API_KEY:$ACCESS_TOKEN@$HOST_NAME/admin/api/2023-10/"
    const val CURRENCY_URL = "http://api.exchangeratesapi.io/"
    const val UNKNOWN_AREA = "UNKNOWN_AREA"
    const val LANGUAGE = "LANGUAGE"
    const val CURRENCY = "CURRENCY"
    const val FIRST_TIME_FLAG = "FIRST_TIME_FLAG"
    const val SHOPIFY_HEADER = "X-Shopify-Access-Token"
    const val CURRENCY_HEADER = "access_key"
    const val CURRENCY_KEY = "4ee6d3381b90ee1d4e7a0c551205269f"
    const val CART_ID = "CART_ID"
    const val CUSTOMER_ID = "CUSTOMER_ID"
    const val WISH_LIST_ID = "WISH_LIST_ID"


    const val STRIPE_URL = "https://api.stripe.com/v1/"
    const val STRIPE_SECRET_KEY = "sk_test_51OHARDIuGQ52jwY9iqlD2D03RftUITPBkRTaUNUABkUk2qqqDheirvD34wVX47qKnX0k5PcaKZwuwG8FPPJfRQia007squq1bU"
    const val STRIPE_PUBLISHED_KEY = "pk_test_51OHARDIuGQ52jwY9A0DZCcXJ7Qt7bNpWj1TqUiiegESCR6VTv8rNN3vwSFLd5G17BEnjf5xNH75YxcKDh3sHYfPz00rW9NkkBc"

    enum class Languages(val value:String) {
        ARABIC("ar"),
        ENGLISH("en")
    }
    enum class Currencies(val value:String,val symbolRes:Int) {
        USD("USD", R.string.usd_sym),
        EUR("EUR",R.string.eur_sym),
        GBP("GBP",R.string.gbp_sym),
        EGP("EGP",R.string.egp_sym)
    }
}