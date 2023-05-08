package com.shoebill.maru.model.data

data class Spot(
    val id: Long = 0L,
    val landmarkId: Long? = null,
    val imageUrl: String = "https://picsum.photos/id/1/1000/2000",
    val likeCount: Int = 0,
    val tags: List<Tag>? = null,
    val liked: Boolean = false,
    val scraped: Boolean = false,
)

data class Tag(
    val id: Long? = null,
    val name: String,
)