package com.shoebill.maru.model.data

data class Landmark(
    val id: Long,
    val name: String,
    val coordinate: Coordinate,
    val visited: Boolean
)