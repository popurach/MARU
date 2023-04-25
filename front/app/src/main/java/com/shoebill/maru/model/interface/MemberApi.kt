package com.shoebill.maru.model.`interface`

import com.shoebill.maru.model.data.Member
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface MemberApi {

    @GET("login/oauth2/token")
    fun login(
        @Header("Access-Token") token: String,
    ): Call<Response<Member>>
}