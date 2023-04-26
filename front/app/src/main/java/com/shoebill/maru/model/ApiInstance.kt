package com.shoebill.maru.model

import com.shoebill.maru.model.`interface`.MemberApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiInstance {
    companion object {
        private const val BASE_URL = "http://10.0.2.2:8080/" // android emulator 에서 pc 의 주소를 가리킴
        // emulator 가 아닌 실제 device 사용시, 실제 내부 ipv4 주소 작성

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val authApi: MemberApi = retrofit.create(MemberApi::class.java)
    }
}