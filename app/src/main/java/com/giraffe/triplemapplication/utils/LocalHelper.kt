package com.giraffe.triplemapplication.utils

import android.annotation.TargetApi
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import com.giraffe.triplemapplication.database.SharedHelper
import java.util.Locale


object LocalHelper {

    fun getLanguage(context: Context):String? {
        val shared: SharedHelper = SharedHelper.getInstance(context)
        return shared.read(Constants.LANGUAGE)
    }
    private fun persist(context: Context, language: String?) {
        val shared: SharedHelper = SharedHelper.getInstance(context)
        shared.store(Constants.LANGUAGE, language)
    }

    private const val SELECTED_LANGUAGE = "Locale.Helper.Selected.Language"
    fun onAttach(context: Context): Context? {
        val lang = getLanguage(context)
        return setLocale(context, lang)
    }

    fun onAttach(context: Context, defaultLanguage: String): Context? {
        val lang = getLanguage(context)
        return setLocale(context, lang)
    }

    fun setLocale(context: Context, language: String?): Context? {
        persist(context, language)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            updateResources(context, language?:"en")
        } else updateResourcesLegacy(context, language?:"en")
    }



    @TargetApi(Build.VERSION_CODES.N)
    private fun updateResources(context: Context, language: String?): Context? {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val configuration: Configuration = context.resources.configuration
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale)
        return context.createConfigurationContext(configuration)
    }

    @Suppress("deprecation")
    private fun updateResourcesLegacy(context: Context, language: String?): Context? {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val resources: Resources = context.resources
        val configuration: Configuration = resources.getConfiguration()
        configuration.locale = locale
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLayoutDirection(locale)
        }
        resources.updateConfiguration(configuration, resources.getDisplayMetrics())
        return context
    }
}