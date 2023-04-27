package com.shoebill.maru.model.data

data class Spot(
    val id: Long,
    val imageUrl: String,
    val isScrap: Boolean,
    val hashTags: List<String>
)
