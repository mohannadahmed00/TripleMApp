package com.giraffe.triplemapplication

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import com.giraffe.triplemapplication.utils.LocaleManager

class AppController : Application() {
    companion object {
        var localeManager: LocaleManager? = null
    }


    override fun attachBaseContext(base: Context?) {
        localeManager = LocaleManager()
        super.attachBaseContext(localeManager!!.setLocale(base!!))
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        localeManager!!.setLocale(this)
    }

}