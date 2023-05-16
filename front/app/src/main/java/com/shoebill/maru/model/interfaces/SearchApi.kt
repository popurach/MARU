package com.shoebill.maru.model.interfaces

import com.shoebill.maru.model.data.search.SearchTag
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApi {
    @GET("/api/tags")
    suspend fun getElasticTagList(
        @Query("keyword") keyword: String,
        @Query("size") size: Int = 5
    ): Response<List<SearchTag>>
}