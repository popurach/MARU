package com.shoebill.maru.util

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton


class PreferenceUtil @Inject constructor(context: Context) {
    private val preference: SharedPreferences =
        context.getSharedPreferences("token", Context.MODE_PRIVATE)

    fun getString(key: String, defaultValue: String): String {
        return preference.getString(key, defaultValue).toString()
    }

    fun setString(key: String, value: String) {
        preference.edit().putString(key, value).apply()
    }
}

@Module
@InstallIn(SingletonComponent::class)
object PrefUtilModule {
    @Provides
    @Singleton
    fun providePrefUtil(@ApplicationContext context: Context): PreferenceUtil {
        return PreferenceUtil(context)
    }
}