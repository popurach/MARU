package com.shoebill.maru.model.interfaces

import com.shoebill.maru.model.data.Member
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface MemberApi {

    @GET("login/oauth2/token")
    suspend fun login(
        @Header("Access-Token") token: String,
    ): retrofit2.Response<Unit>

    @GET("api/members/my")
    suspend fun getMemberInfo(): retrofit2.Response<Member>

    @POST("api/members/notice-token")
    suspend fun updateNoticeToken(
        @Body noticeToken: RequestBody,
    ): retrofit2.Response<Unit>
}