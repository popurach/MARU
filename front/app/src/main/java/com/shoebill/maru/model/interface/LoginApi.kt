package com.shoebill.maru.model.`interface`

import com.shoebill.maru.model.data.UserInfo
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET

interface LoginApi {
    @GET("login/1")
    fun getUserInfo(): Flow<UserInfo>
}