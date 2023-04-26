package com.shoebill.maru.ui.page

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.shoebill.maru.service.LoginViewModel

@Composable
fun LoginPage() {
    val context = LocalContext.current
    val loginViewModel: LoginViewModel = hiltViewModel()

    Column() {
        Button(onClick = { loginViewModel.kakaoLogin(context) }) {
            Text(text = "카카오 로그인")
        }
        Button(onClick = { /*TODO*/ }) {
            Text(text = "네이버 로그인")
        }
        Button(onClick = { /*TODO*/ }) {
            Text(text = "구글 로그인")
        }
    }
}