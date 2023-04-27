package com.shoebill.maru.model.interfaces

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface MemberApi {

    @GET("login/oauth2/token")
    suspend fun login(
        @Header("Access-Token") token: String,
    ): retrofit2.Response<Unit>

    @GET("api/auth/access-token")
    fun refresh(
        @Header("Authorization") token: String
    ): retrofit2.Response<Unit>

    @GET("api/notices")
    suspend fun getNotices(
        @Query("page") page: Int
    ): retrofit2.Response<String?>
}