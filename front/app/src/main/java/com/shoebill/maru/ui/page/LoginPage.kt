package com.shoebill.maru.ui.page

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.shoebill.maru.service.Login

@Composable
fun LoginPage() {
    val login = Login(LocalContext.current)

    Column() {
        Button(onClick = { login.kakaoLogin() }) {
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