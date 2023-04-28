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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.shoebill.maru.R
import com.shoebill.maru.service.LoginViewModel
import com.shoebill.maru.ui.component.LottieOwl
import com.shoebill.maru.viewmodel.LoginViewModel
import com.shoebill.maru.viewmodel.NavigateViewModel


@Composable
fun LoginPage(
    loginViewModel: LoginViewModel = hiltViewModel(),
    navigateViewModel: NavigateViewModel = viewModel()
) {
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
            contentDescription = "로고"
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
                Box(
                    modifier = Modifier.padding(
                        start = 40.dp,
                        end = 40.dp,
                        top = 4.dp,
                        bottom = 4.dp
                    )
                ) {
                    Image(
                        modifier = Modifier
                            .clickable {
                                loginViewModel.kakaoLogin(
                                    context,
                                    navigateViewModel.navigator
                                )
                            },
                        painter = painterResource(id = R.drawable.kakao_login),
                        contentDescription = "카카오 로그인"
                    )
                }
                Box(
                    modifier = Modifier.padding(
                        start = 40.dp,
                        end = 40.dp,
                        top = 4.dp,
                        bottom = 4.dp
                    )
                ) {
                    Image(
                        modifier = Modifier
                            .clickable {
                                loginViewModel.naverLogin(
                                    context,
                                    navigateViewModel.navigator!!
                                )
                            },
                        painter = painterResource(id = R.drawable.naver_login),
                        contentDescription = "네이버 로그인"
                    )
                }
                Box(
                    modifier = Modifier.padding(
                        start = 40.dp,
                        end = 40.dp,
                        top = 4.dp,
                        bottom = 4.dp
                    )
                ) {
                    Image(
                        modifier = Modifier
                            .clickable { /*TODO*/ }
                            .background(color = Color.White)
                            .border(width = 0.5.dp, color = Color(0xFFEAEDEF)),
                        painter = painterResource(id = R.drawable.google_login),
                        contentDescription = "구글 로그인"
                    )
                }
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