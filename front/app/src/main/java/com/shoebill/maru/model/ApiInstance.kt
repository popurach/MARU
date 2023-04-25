package com.shoebill.maru.model

import com.shoebill.maru.model.`interface`.MemberApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiInstance {
    companion object {
        private const val BASE_URL = "http://localhost:8080/"

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("http://localhost:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val authApi: MemberApi = retrofit.create(MemberApi::class.java)
    }
}