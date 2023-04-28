package com.shoebill.maru.util

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

object BrushUtil {
    val defaultBrush = Brush.linearGradient(listOf(Color(0xFF6039DF), Color(0xFFA14AB7)))
    val whiteBrush = Brush.linearGradient(listOf(Color.White, Color.White))
}