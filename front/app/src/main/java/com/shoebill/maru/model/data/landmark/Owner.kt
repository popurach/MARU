package com.shoebill.maru.model.data.landmark

data class Owner(
    val id: Long = -1,
    val nickname: String = "마루",
    val profileImageUrl: String = "https://picsum.photos/200/300",
    val spotImageUrl: String? = null,
)
