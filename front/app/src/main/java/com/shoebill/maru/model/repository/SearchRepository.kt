package com.shoebill.maru.model.repository

import com.shoebill.maru.model.data.search.SearchTag
import com.shoebill.maru.model.interfaces.SearchApi
import retrofit2.Response
import javax.inject.Inject

class SearchRepository @Inject constructor(
    private val searchApi: SearchApi,
) {
    suspend fun getElasticTagList(keyword: String): Response<List<SearchTag>> =
        searchApi.getElasticTagList(keyword = keyword)
}