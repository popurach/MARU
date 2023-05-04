package com.shoebill.maru.model.data.landmark

import com.shoebill.maru.model.data.Coordinate

data class Landmark(
    val id: Long,
    val name: String,
    val coordinate: Coordinate,
    val visited: Boolean
)