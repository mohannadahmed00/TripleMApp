package com.giraffe.triplemapplication.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.os.LocaleList
import com.giraffe.triplemapplication.database.SharedHelper
import java.util.Locale

class LocaleManager{
    fun setLocale(c: Context): Context {
        return updateResources(c, getLanguage(c))
    }

    fun setNewLocale(c: Context, language: String): Context {
        persistLanguage(c, language)
        return updateResources(c, language)
    }

    fun getLanguage(context: Context): String {
        val shared: SharedHelper = SharedHelper.getInstance(context)
        return shared.read(Constants.LANGUAGE)?:"en"
    }

    @SuppressLint("ApplySharedPref")
    private fun persistLanguage(context: Context, language: String) {
        val shared: SharedHelper = SharedHelper.getInstance(context)
        shared.store(Constants.LANGUAGE, language)
    }

    private fun updateResources(context: Context, language: String): Context {
        val mContext: Context
        val locale = Locale(language)
        Locale.setDefault(locale)
        val res = context.resources
        val config = Configuration(res.configuration)
        setLocaleForApi24(config, locale)
        mContext = context.createConfigurationContext(config)
        return mContext
    }

    private fun setLocaleForApi24(config: Configuration, target: Locale) {
        val set: MutableSet<Locale> = LinkedHashSet()
        set.add(target)
        val all = LocaleList.getDefault()
        for (i in 0 until all.size()) {
            set.add(all[i])
        }
        val locales = set.toTypedArray()
        config.setLocales(LocaleList(*locales))
    }
}