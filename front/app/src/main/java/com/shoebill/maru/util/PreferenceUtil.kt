package com.shoebill.maru.util

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.shoebill.maru.model.data.Place
import com.shoebill.maru.model.data.SearchHistoryWrapper
import com.shoebill.maru.model.data.Tag
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

    fun saveSearchHistory(place: Place) {
        this.saveSearchHistory(SearchHistoryWrapper(type = "place", Gson().toJson(place)))
    }

    fun saveSearchHistory(tag: Tag) {
        this.saveSearchHistory(SearchHistoryWrapper(type = "place", Gson().toJson(tag)))
    }

    fun saveSearchHistory(searchHistoryWrapper: SearchHistoryWrapper) {
        val jsonString =
            searchPreferences.getString("search_history", "[]")

        val gson = Gson()
        val listType = object : TypeToken<MutableList<SearchHistoryWrapper>>() {}.type
        val jsonWrapperList = gson.fromJson<MutableList<SearchHistoryWrapper>>(jsonString, listType)

        // 동일한거 제거
        jsonWrapperList.removeIf { it.data == searchHistoryWrapper.data }

        // 추가
        jsonWrapperList.add(searchHistoryWrapper)

        // 갯수 확인
        if (jsonWrapperList.size > MAX_SEARCH_HISTORY_SIZE) {
            jsonWrapperList.removeFirst()
        }

        searchPreferences.edit().putString("search_history", gson.toJson(jsonWrapperList)).apply()
    }

    // 검색 이력을 불러오는 함수
    fun loadSearchHistory(): List<SearchHistoryWrapper> {
        val jsonString =
            searchPreferences.getString("search_history", "[]")

        val gson = Gson()
        val listType = object : TypeToken<List<SearchHistoryWrapper>>() {}.type
        val jsonWrapperList = gson.fromJson<List<SearchHistoryWrapper>>(jsonString, listType)

        return jsonWrapperList.reversed()
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