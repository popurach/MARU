package com.shoebill.maru.ui.component.notice

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shoebill.maru.R

@Composable
fun NoticeContents() {
    var messages:ArrayList<String> = arrayListOf("11 경매에 낙찰되셨습니다.",
        "더현대 방문 포인트를 받았습니다.", "서울현대박물관 경매에 낙찰되셨습니다.", "더현대 방문 포인트를 받았습니다.")
    for( i in 1..50) {
        messages.add("서울현대박물관 경매에 낙찰되셨습니다.")
        messages.add("더현대 방문 포인트를 받았습니다.")
    }
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
    ) {
        items(messages) { message ->
            NoticeListItem(message = message)
            Divider(
                color = Color(0xFFE9E9E9),
                thickness= 1.dp,
                modifier = Modifier.padding(vertical = 15.dp))
        }
    }
    NoticeListItem(message = "서울현대박물관 경매에 낙찰되셨습니다.")
}

@Composable
fun NoticeListItem(message: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = painterResource(R.drawable.notice_point_icon),
            modifier = Modifier.padding(start = 20.dp, end = 10.dp).size(30.dp),
            contentDescription = null)
        Text(text = message)
    }
}