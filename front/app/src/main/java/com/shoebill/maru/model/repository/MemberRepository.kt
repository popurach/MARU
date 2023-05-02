package com.shoebill.maru.model.repository

import com.shoebill.maru.model.data.LoginGoogleRequestModel
import com.shoebill.maru.model.interfaces.MemberApi
import com.shoebill.maru.util.PreferenceUtil
import javax.inject.Inject

class MemberRepository @Inject constructor(
    private val memberApi: MemberApi,
    private val prefUtil: PreferenceUtil,
) {
    suspend fun kakaoNaverLogin(accessToken: String) = memberApi.kakaoNaverLogin(accessToken)

    suspend fun googleLogin(loginGoogleRequestModel: LoginGoogleRequestModel) =
        memberApi.googleLogin(loginGoogleRequestModel = loginGoogleRequestModel)

    suspend fun getMemberInfo() = memberApi.getMemberInfo()

    fun logout() {
        prefUtil.clear()
    }
}