package com.shoebill.maru.ui.page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.shoebill.maru.ui.component.notice.NoticeContents
import com.shoebill.maru.ui.component.notice.NoticeHeader

@Composable
fun NoticePage(
    navController: NavHostController
) {
    Column {
        NoticeHeader(navController = navController)
        Divider(thickness = 1.dp, color = Color(0xFFE9E9E9), modifier = Modifier.padding(bottom = 0.dp))
        NoticeContents()
    }
}