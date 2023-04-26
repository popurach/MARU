package com.shoebill.maru.model.`interface`

import retrofit2.http.GET
import retrofit2.http.Header

interface MemberApi {

    @GET("login/oauth2/token")
    suspend fun login(
        @Header("Access-Token") token: String,
    ): retrofit2.Response<Unit>
}