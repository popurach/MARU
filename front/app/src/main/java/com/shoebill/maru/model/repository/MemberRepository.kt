package com.shoebill.maru.model.repository

import com.shoebill.maru.model.interfaces.MemberApi
import com.shoebill.maru.util.PreferenceUtil
import javax.inject.Inject

class MemberRepository @Inject constructor(
    private val memberApi: MemberApi,
    private val prefUtil: PreferenceUtil,
) {
    suspend fun login(accessToken: String) = memberApi.login(accessToken)

    suspend fun getMemberInfo() = memberApi.getMemberInfo()

    fun logout() {
        prefUtil.clear()
    }
}