package com.shoebill.maru.ui.component.bottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.shoebill.maru.ui.component.common.FabCamera

@Composable
fun BottomSheetFrame(
    hasFabCamera: Boolean = false,
    backgroundColor: Color = Color.White,
    content: @Composable() () -> Unit
) {
    Box(
        Modifier
            .background(backgroundColor)
            .fillMaxSize()
    ) {
        content()
        BottomSheetIndicator()
        if (hasFabCamera) {
            FabCamera(
                Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 20.dp)
            )
        }

    }
}