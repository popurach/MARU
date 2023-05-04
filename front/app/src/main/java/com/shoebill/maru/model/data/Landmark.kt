package com.shoebill.maru.model.data

data class Landmark(
    val name: String,
    val isFirstVisit: Boolean,
    val occupantProfileImageUrl: String,
    val occupantNickname: String,
    val sentence: String,
)