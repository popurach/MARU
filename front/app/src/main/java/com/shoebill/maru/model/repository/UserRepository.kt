package com.shoebill.maru.model.repository

import com.shoebill.maru.model.ApiClient
import com.shoebill.maru.model.data.UserInfo
import com.shoebill.maru.model.`interface`.LoginApi
import kotlinx.coroutines.flow.Flow

class UserRepository private constructor() {
    companion object {
        fun getUserInfo(): Flow<UserInfo> {
            return ApiClient.createClientByService(LoginApi::class.java)
                .getUserInfo()
        }
    }
}