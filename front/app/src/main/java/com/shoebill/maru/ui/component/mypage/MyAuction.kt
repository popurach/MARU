package com.shoebill.maru.ui.component.mypage

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.shoebill.maru.ui.component.notice.NoticeList

@Composable
fun MyAuction() {
    Column {
        NoticeList()
        Divider(
            thickness = 1.dp,
            color = Color(0xFFE9E9E9),
            modifier = Modifier.padding(bottom = 0.dp)
        )
    }
}