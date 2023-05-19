package com.shoebill.maru.model.data.spot

data class Property(
    val geoType: String,
    val id: Long?,
    val radius: Double,
    val count: Int,
    val abbrevCount: String
)
