package com.shoebill.maru.model.data

import java.io.File

data class MemberUpdateRequest(
    val image: File?,
    val nickname: String?
)
