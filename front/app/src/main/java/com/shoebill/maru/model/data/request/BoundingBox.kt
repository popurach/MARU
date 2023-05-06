package com.shoebill.maru.model.data.request

data class BoundingBox(
    val west: Double,
    val south: Double,
    val east: Double,
    val north: Double,
    val zoom: Int
)
