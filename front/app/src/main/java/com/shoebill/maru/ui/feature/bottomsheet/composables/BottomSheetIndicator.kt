package com.shoebill.maru.ui.feature.bottomsheet

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.shoebill.maru.R

@Composable
fun BottomSheetIndicator() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Icon(
            painter = painterResource(id = R.drawable.stick_bar),
            contentDescription = "stick bar",
            modifier = Modifier
                .padding(vertical = 10.dp)
                .align(Alignment.Center),
            tint = Color.LightGray
        )
    }
}