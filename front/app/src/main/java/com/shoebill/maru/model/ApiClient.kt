package com.shoebill.maru.model

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private val api = createRetrofit("") // 이곳에 우리 baseUrl입력

    private fun createRetrofit(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(baseUrl)
            .build()
    }

    fun <T> createClientByService(service: Class<T>): T = api.create(service)
}