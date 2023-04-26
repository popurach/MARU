package com.shoebill.maru.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun LottieDiamond() {
    val composition by rememberLottieComposition(spec = LottieCompositionSpec.Url("https://assets2.lottiefiles.com/packages/lf20_RhZuehZviJ.json"))
    LottieAnimation(
        composition = composition,
        iterations = LottieConstants.IterateForever,
    )
}

@Composable
fun LottieOwl() {
    val composition by rememberLottieComposition(spec = LottieCompositionSpec.Url("https://assets1.lottiefiles.com/packages/lf20_g9gACcXlja.json"))
    LottieAnimation(composition = composition, iterations = LottieConstants.IterateForever)
}