package com.shoebill.maru.model.data

import com.google.gson.annotations.SerializedName

data class LoginGoogleResponseModel(
    @SerializedName("access_token")
    val accessToken: String
)