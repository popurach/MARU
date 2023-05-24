package com.shoebill.maru.ui.page

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.shoebill.maru.ui.feature.notice.NoticeHeader
import com.shoebill.maru.ui.feature.notice.NoticeList

@Composable
fun NoticePage(
    navController: NavHostController
) {
    Column {
        NoticeHeader(navController = navController)
        Divider(
            thickness = 1.dp,
            color = Color(0xFFE9E9E9),
            modifier = Modifier.padding(bottom = 0.dp)
        )
        NoticeList()
    }
}