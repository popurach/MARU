package com.shoebill.maru.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SuggestionList() {
    Column {
        Text(
            text = "---------- 최근 검색어 -----------------------------------------------------------------------",
            modifier = Modifier.padding(horizontal = 16.dp),
            color = Color.Gray,
            fontSize = 11.sp
        )
        for (i in 0..3) {
            SearchListItem()
        }
    }
}