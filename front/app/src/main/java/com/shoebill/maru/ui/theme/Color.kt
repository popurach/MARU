package com.shoebill.maru.ui.theme

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val Purple200 = Color(0xFFBB86FC)
val Purple500 = Color(0xFF6200EE)
val Purple700 = Color(0xFF3700B3)
val Teal200 = Color(0xFF03DAC5)

val MaruBackground = Color(0xffF5F4FF)

val MaruBrush = Brush.linearGradient(
    listOf(
        Color(0xFF6039DF),
        Color(0xFFA14AB7)
    ),
    Offset.Zero,
    Offset.Infinite
)