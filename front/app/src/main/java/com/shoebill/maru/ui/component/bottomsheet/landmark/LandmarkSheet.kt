package com.shoebill.maru.ui.component.bottomsheet.landmark

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.shoebill.maru.ui.component.bottomsheet.BottomSheetIndicator
import com.shoebill.maru.ui.component.common.FabCamera

@Composable
fun LandmarkSheet(
    content: @Composable() () -> Unit
) {
    Box(
        Modifier
            .background(Color(0xffF5F4FF))
            .fillMaxSize()
    ) {
        content()
        BottomSheetIndicator()
        FabCamera(
            Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 20.dp)
        )
    }
}