package com.shoebill.maru.ui.component.mypage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shoebill.maru.viewmodel.CountdownViewModel

@Composable
fun CountdownTimer(viewModel: CountdownViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF5F4F4)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            String.format(
                "%d일 %02d:%02d:%02d",
                viewModel.daysLeft.value,
                viewModel.hoursLeft.value,
                viewModel.minutesLeft.value,
                viewModel.secondsLeft.value
            ),
            modifier = Modifier
                .padding(vertical = 18.dp)
                .graphicsLayer(alpha = 0.99f)
                .drawWithCache {
                    onDrawWithContent {
                        drawContent()
                        drawRect(
                            Brush.linearGradient(
                                listOf(
                                    Color(0xFF6039DF),
                                    Color(0xFFA14AB7)
                                )
                            ),
                            blendMode = BlendMode.SrcAtop
                        )
                    }
                },
            textAlign = TextAlign.Center,
            fontSize = 30.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}