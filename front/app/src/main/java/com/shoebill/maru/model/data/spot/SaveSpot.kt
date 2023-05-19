package com.shoebill.maru.model.data.spot

data class SaveSpot(
    val tags: List<String>,
    val landmarkId: Long? = null
)
