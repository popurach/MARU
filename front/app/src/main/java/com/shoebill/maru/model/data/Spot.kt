package com.shoebill.maru.model.data

data class Spot(
    val id: Long,
    val landmarkId: Long?,
    val imageUrl: String,
    val likeCount: Int,
    val tags: List<Tag>?,
    val liked: Boolean,
    val scraped: Boolean,
) {

    constructor(id: Long, imageUrl: String, scraped: Boolean) : this(
        id = id,
        landmarkId = null,
        imageUrl = imageUrl,
        likeCount = 0,
        tags = null,
        liked = false,
        scraped = scraped,
    )
}

data class Tag(
    val id: Long,
    val name: String,
)