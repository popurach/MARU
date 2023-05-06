package com.shoebill.maru.model.data.spot

data class SpotMarker(
    val type: String,
    val geometry: Geometry,
    val properties: Property
)
