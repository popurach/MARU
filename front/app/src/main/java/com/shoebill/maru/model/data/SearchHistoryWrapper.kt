package com.shoebill.maru.model.data

import com.google.gson.Gson

data class SearchHistoryWrapper(
    val type: String,
    val data: String
) {
    fun getData(): Any {
        lateinit var result: Any
        when (type) {
            "place" -> result = Gson().fromJson(data, Place::class.java)
            "tag" -> result = Gson().fromJson(data, Tag::class.java)
        }

        return result
    }
}