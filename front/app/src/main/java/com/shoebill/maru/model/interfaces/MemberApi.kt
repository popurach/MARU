package com.shoebill.maru.model.interfaces

import com.shoebill.maru.model.data.LoginGoogleRequestModel
import com.shoebill.maru.model.data.LoginGoogleResponseModel
import com.shoebill.maru.model.data.Member
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Url

interface MemberApi {

    @GET("login/oauth2/token")
    suspend fun kakaoNaverLogin(
        @Header("Access-Token") token: String,
    ): Response<Unit>

    @POST
    suspend fun googleLogin(
        @Url url: String = "https://www.googleapis.com/oauth2/v4/token",
        @Body loginGoogleRequestModel: LoginGoogleRequestModel
    ): Response<LoginGoogleResponseModel>

    @GET("api/members/my")
    suspend fun getMemberInfo(): Response<Member>

    @POST("api/members/notice-token")
    suspend fun updateNoticeToken(
        @Body noticeToken: RequestBody,
    ): Response<Unit>

    @Multipart
    @POST("api/members/my")
    suspend fun updateMemberInfo(
        @Part image: MultipartBody.Part? = null,
        @Part nickname: MultipartBody.Part? = null
    ): Response<Member>
}