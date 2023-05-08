package com.shoebill.maru.model.repository

import com.shoebill.maru.model.data.LoginGoogleRequestModel
import com.shoebill.maru.model.data.Member
import com.shoebill.maru.model.data.MemberUpdateRequest
import com.shoebill.maru.model.interfaces.MemberApi
import com.shoebill.maru.util.PreferenceUtil
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Response
import javax.inject.Inject

class MemberRepository @Inject constructor(
    private val memberApi: MemberApi,
    private val prefUtil: PreferenceUtil,
) {
    suspend fun kakaoNaverLogin(accessToken: String) = memberApi.kakaoNaverLogin(accessToken)

    suspend fun googleLogin(loginGoogleRequestModel: LoginGoogleRequestModel) =
        memberApi.googleLogin(loginGoogleRequestModel = loginGoogleRequestModel)

    suspend fun getMemberInfo() = memberApi.getMemberInfo()

    suspend fun updateNoticeToken(noticeToken: RequestBody) =
        memberApi.updateNoticeToken(noticeToken)

    suspend fun updateMemberInfo(memberUpdateRequest: MemberUpdateRequest): Response<Member> {
        val image: MultipartBody.Part? = if (memberUpdateRequest.image != null)
            MultipartBody.Part.createFormData(
                name = "image",
                filename = memberUpdateRequest.image.name,
                body = memberUpdateRequest.image.asRequestBody()
            ) else null


        val nickname: MultipartBody.Part? = if (memberUpdateRequest.nickname != null)
            MultipartBody.Part.createFormData(
                name = "nickname",
                value = memberUpdateRequest.nickname
            ) else null

        return memberApi.updateMemberInfo(image, nickname)
    }

    fun logout() {
        prefUtil.clear()
    }
}