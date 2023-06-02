package com.shoebill.maru.ui.page

import android.app.Activity
import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
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
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.shoebill.maru.BuildConfig
import com.shoebill.maru.R
import com.shoebill.maru.ui.component.LottieOwl
import com.shoebill.maru.ui.feature.common.CustomCircularProgressBar
import com.shoebill.maru.ui.feature.login.LoginViewModel
import com.shoebill.maru.ui.main.NavigateViewModel

@Composable
fun LoginPage(
    loginViewModel: LoginViewModel = hiltViewModel(),
    navigateViewModel: NavigateViewModel = viewModel(),
) {
    val context = LocalContext.current
    val isLoading = loginViewModel.isLoading.observeAsState()

    val startForResult =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                if (result.data != null) {
                    val task: Task<GoogleSignInAccount> =
                        GoogleSignIn.getSignedInAccountFromIntent(intent)
                    loginViewModel.handleSignInResult(task, navigateViewModel.navigator!!)
                }
            }
        }

    fun getGoogleLoginAuth(context: Context): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestServerAuthCode(BuildConfig.GOOGLE_CLIENT_ID)
            .requestEmail()
            .build()

        return GoogleSignIn.getClient(context, gso)
    }

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
        Box {
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
                    modifier = Modifier
                        .padding(
                            start = 40.dp,
                            end = 40.dp,
                            top = 4.dp,
                            bottom = 4.dp
                        )
                        .clickable {
                            startForResult.launch(getGoogleLoginAuth(context = context).signInIntent)
                        }
                ) {
                    Image(
                        modifier = Modifier
                            .background(Color.White)
                            .border(width = 0.5.dp, color = Color(0xFFEAEDEF)),
                        painter = painterResource(id = R.drawable.google_login),
                        contentDescription = "구글 로그인"
                    )
                    Image(
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(start = 23.dp)
                            .size(22.dp),
                        painter = painterResource(id = R.drawable.google_logo),
                        contentDescription = "구글 로고"
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
    if (isLoading.value == true) {
        CustomCircularProgressBar()
    }
}