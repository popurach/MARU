package com.shoebill.maru.model.data.request

data class SpotClusterDTO(
    val boundingBox: BoundingBox,
    val filter: String = "all",
    val tagId: Long? = null,
    val size: Int
)