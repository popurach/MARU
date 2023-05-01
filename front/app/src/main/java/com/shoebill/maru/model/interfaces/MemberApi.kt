package com.shoebill.maru.model.interfaces

import com.shoebill.maru.model.data.Member
import retrofit2.http.GET
import retrofit2.http.Header

interface MemberApi {

    @GET("login/oauth2/token")
    suspend fun kakaoNaverLogin(
        @Header("Access-Token") token: String,
    ): retrofit2.Response<Unit>

    @GET("login/oauth2/token")
    suspend fun googleLogin(
        @Header("Authorization-Code") code: String,
    ): retrofit2.Response<Unit>

    @GET("api/members/my")
    suspend fun getMemberInfo(): retrofit2.Response<Member>
}