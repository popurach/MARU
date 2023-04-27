package com.shoebill.maru.model.data

data class Landmark(
    val name: String,
    val occupant: Occupant,
    val spotList: List<SpotSimple>
)