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

    constructor() : this(
        id = 0L,
        landmarkId = null,
        imageUrl = "https://picsum.photos/id/1/1000/2000",
        likeCount = 0,
        tags = null,
        liked = false,
        scraped = false,
    )
}

data class Tag(
    val id: Long,
    val name: String,
)