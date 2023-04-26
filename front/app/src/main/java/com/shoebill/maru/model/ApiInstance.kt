package com.shoebill.maru.model

import android.content.Context
import android.util.Log
import com.shoebill.maru.model.`interface`.MemberApi
import com.shoebill.maru.util.PreferenceUtil
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject


object ApiInstance {

    private const val BASE_URL = "http://10.0.2.2:8080/" // android emulator 에서 pc 의 주소를 가리킴

    // emulator 가 아닌 실제 device 사용시, 실제 내부 ipv4 주소 작성
    @ApplicationContext
    lateinit var context: Context

    @Inject
    lateinit var prefUtil: PreferenceUtil

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(
            OkHttpClient().newBuilder()
                .addInterceptor(AppInterceptor()).build()
        ) // auth interceptor 추가
        .build()

    val authApi: MemberApi = retrofit.create(MemberApi::class.java)

    class AppInterceptor @Inject constructor() : Interceptor {
        @Inject
        lateinit var prefUtil: PreferenceUtil

        override fun intercept(chain: Interceptor.Chain): Response {
            val originRequest = chain.request()
            val accessToken = prefUtil.getString("accessToken", "")

            // 1. accessToken 존재한 다면 header 에 추가
            if (accessToken != "") {
                val requestWithAccessToken =
                    originRequest.newBuilder().header("Authorization", "Bearer $accessToken")
                        .build()
                val response = chain.proceed(requestWithAccessToken)

                // 2. 요청 결과가 401 이라면 refresh token 으로 access token을 재발급 받음
                if (response.code == 401) {
                    response.close()
                    val refreshToken = prefUtil.getString("refreshToken", "")
                    if (refreshToken != "") {
                        // 재발급 api 호출 -> 결과는 accessToken : String
                        val response = authApi.refresh(refreshToken)


                        // 3. 재발급 받은 accessToken 으로 동일 요청 시도
                        // 재발급 성공시
                        if (response.isSuccessful) {
                            val newAccessToken = response.headers().get("Access-Token")
                            prefUtil.setString("accessToken", newAccessToken!!)

                            val requestWithNewAccessToken = originRequest.newBuilder()
                                .header("Authorization", "Bearer $newAccessToken")
                                .build()
                            val response = chain.proceed(requestWithAccessToken)
                            // 4. 재발급 받은 accessToken 으로 시도했는데 401이면 재 로그인
                            if (response.code == 401) {
                                Log.d("AUTH", "재발급된 accessToken 으로도 실패")
                            } else {
                                // 401 이 아니면 그대로 결과 반환
                                return response
                            }
                        } else {
                            // accessToken 재발급에 실패하면 로그인 페이지로 이동
                            Log.d("AUTH", "accessToken 재발급 실패")
                        }
                    }
                } else {
                    // 401 이 아니면 그냥 요청 결과 반환
                    return response
                }
            }

            return chain.proceed(originRequest)
        }
    }
}