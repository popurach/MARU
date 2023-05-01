package com.shoebill.maru.model.data

data class Spot(
    val id: Long,
    val landmarkId: Long?,
    val imageUrl: String,
    val isScrap: Boolean,
    val hashTags: List<String>
) {
    constructor(id: Long, imageUrl: String, isScrap: Boolean, hashTags: List<String>) : this(
        id = id,
        landmarkId = null,
        imageUrl = imageUrl,
        isScrap = isScrap,
        hashTags = hashTags,
    )
}
