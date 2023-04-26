package com.shoebill.maru.ui.component.bottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun LandmarkSheet() {
    Box(
        Modifier
            .background(Color(0xffF5F4FF))
            .fillMaxSize()
    ) {
        LandmarkFirstVisit()
        BottomSheetIndicator()
    }
}