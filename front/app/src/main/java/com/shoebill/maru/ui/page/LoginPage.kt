package com.shoebill.maru.ui.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.shoebill.maru.R
import com.shoebill.maru.service.Login

@Composable
fun LottieOwl() {
    val composition by rememberLottieComposition(spec = LottieCompositionSpec.Url("https://assets1.lottiefiles.com/packages/lf20_g9gACcXlja.json"))
    LottieAnimation(composition = composition, iterations = LottieConstants.IterateForever)
}

@Composable
fun LoginPage() {
    val login = Login()
    val context = LocalContext.current // composable 이 실행되고 있는 Context 반환

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFF5F4FF)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier
                .padding(top = 60.dp),
            painter = painterResource(id = R.drawable.maru_logo),
            contentDescription = ""
        )
        Box() {
            Box(
                modifier = Modifier.height(300.dp)
            ) {
                LottieOwl()
            }
            Column(
                modifier = Modifier.padding(top = 259.dp, bottom = 50.dp)
            ) {
                Image(
                    modifier = Modifier
                        .clickable { login.kakaoLogin(context) }
                        .padding(start = 40.dp, end = 40.dp, top = 4.dp, bottom = 4.dp),
                    painter = painterResource(id = R.drawable.kakao_login),
                    contentDescription = "카카오 로그인"
                )
                Image(
                    modifier = Modifier
                        .clickable { /*TODO*/ }
                        .padding(start = 40.dp, end = 40.dp, top = 4.dp, bottom = 4.dp),
                    painter = painterResource(id = R.drawable.naver_login),
                    contentDescription = "네이버 로그인"
                )
                Image(
                    modifier = Modifier
                        .clickable { /*TODO*/ }
                        .padding(start = 40.dp, end = 40.dp, top = 4.dp, bottom = 4.dp)
                        .background(color = Color.White)
                        .border(width = 0.5.dp, color = Color(0xFFEAEDEF)),
                    painter = painterResource(id = R.drawable.google_login),
                    contentDescription = "구글 로그인"
                )
            }
        }
        Text(
            text = "Copyright ©2023 Shoebill.\n" + "All rights reserved.",
            fontSize = 13.sp,
            color = Color(0xFFC0C0C0),
            textAlign = TextAlign.Center
        )
    }
}