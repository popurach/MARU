package com.shoebill.maru.model.repository

import com.shoebill.maru.model.interfaces.MemberApi
import com.shoebill.maru.util.PreferenceUtil
import javax.inject.Inject

class MemberRepository @Inject constructor(
    private val memberApi: MemberApi,
    private val prefUtil: PreferenceUtil,
) {
    suspend fun kakaoNaverLogin(accessToken: String) = memberApi.kakaoNaverLogin(accessToken)

    suspend fun googleLogin(code: String) = memberApi.googleLogin(code)

    suspend fun getMemberInfo() = memberApi.getMemberInfo()

    fun logout() {
        prefUtil.clear()
    }
}