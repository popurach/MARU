package com.shoebill.maru.model.repository

import com.shoebill.maru.model.interfaces.MemberApi
import javax.inject.Inject

class MemberRepository @Inject constructor(private val memberApi: MemberApi) {
    suspend fun login(accessToken: String) = memberApi.login(accessToken)

    fun refresh(refreshToken: String) = memberApi.refresh(refreshToken)

    suspend fun getNotices(page: Int = 0) = memberApi.getNotices(page)
}