package com.shoebill.maru.model.interfaces

import com.shoebill.maru.model.data.notice.Notice
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NoticeApi {
    @GET("api/notices")
    suspend fun getNotices(
        @Query("page") page: Int,
        @Query("size") size: Int = 15,
    ): Response<List<Notice>>
}