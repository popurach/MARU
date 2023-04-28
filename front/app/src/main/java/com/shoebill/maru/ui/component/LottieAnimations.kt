package com.shoebill.maru.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun LottieDiamond(
    onClick: () -> Unit
) {
    val composition by rememberLottieComposition(spec = LottieCompositionSpec.Url("https://assets2.lottiefiles.com/packages/lf20_RhZuehZviJ.json"))
    LottieAnimation(
        modifier = Modifier
            .fillMaxSize()
            .clickable { onClick() },
        composition = composition,
        iterations = LottieConstants.IterateForever,
    )
}

@Composable
fun LottieOwl() {
    val composition by rememberLottieComposition(spec = LottieCompositionSpec.Url("https://assets1.lottiefiles.com/packages/lf20_g9gACcXlja.json"))
    LottieAnimation(composition = composition, iterations = LottieConstants.IterateForever)
}