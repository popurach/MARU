package com.shoebill.maru.ui.page

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.shoebill.maru.ui.component.notice.NoticeContents
import com.shoebill.maru.ui.component.notice.NoticeHeader

@Composable
fun NoticePage(
    navController: NavHostController
) {
    Column {
        NoticeHeader()
        NoticeContents()
    }

}