package com.shoebill.maru.model

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = "http://10.0.2.2:8080/"
    private val api = createRetrofit() // 이곳에 우리 baseUrl입력

    private fun createRetrofit(): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
    }

    fun <T> createClientByService(service: Class<T>): T = api.create(service)
}