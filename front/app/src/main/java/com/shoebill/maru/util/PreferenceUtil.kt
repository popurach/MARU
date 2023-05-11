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

    private val searchPreferences =
        context.getSharedPreferences("search_history", Context.MODE_PRIVATE)

    private val MAX_SEARCH_HISTORY_SIZE = 3

    fun saveSearchHistory(searchTerm: String) {
        val historyString =
            searchPreferences.getString("search_history", null)

        val separator = "|"
        val historyList = historyString?.split(separator)?.toMutableList() ?: mutableListOf()


        historyList.remove(searchTerm)
        historyList.add(searchTerm)
        if (historyList.size > MAX_SEARCH_HISTORY_SIZE) {
            historyList.removeFirst()
        }

        val nextString = historyList.joinToString(separator)
        searchPreferences.edit().putString("search_history", nextString).apply()
    }

    // 검색 이력을 불러오는 함수
    fun loadSearchHistory(): List<String> {

        val historyString =
            searchPreferences.getString("search_history", null)

        val separator = "|"
        val historyList = historyString?.split(separator)?.toMutableList() ?: mutableListOf()

        return historyList.reversed()
    }

    fun getString(key: String, defaultValue: String = ""): String {
        return preference.getString(key, defaultValue).toString()
    }

    fun setString(key: String, value: String) {
        preference.edit().putString(key, value).apply()
    }

    fun clear() {
        preference.edit().clear().apply()
    }

    fun isLogin(): Boolean {
        return getString("accessToken") != ""
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