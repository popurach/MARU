package com.shoebill.maru.ui.feature.notice

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.shoebill.maru.R
import com.shoebill.maru.ui.main.NavigateViewModel

@Composable
fun NoticeList(
    noticeViewModel: NoticeViewModel = hiltViewModel(),
    navigateViewModel: NavigateViewModel = hiltViewModel(),
) {
    val notices = noticeViewModel.getNoticePagination(navigateViewModel.navigator!!)
        .collectAsLazyPagingItems()

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            if (notices.itemCount == 0) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .height(670.dp)

                    ) {
                        Text(
                            text = "새로운 알림이 없습니다.",
                            modifier = Modifier
                                .align(Alignment.Center)
                        )
                    }
                }
            }
            items(notices.itemCount) { idx ->
                if (notices[idx] != null) {
                    Box {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 20.dp)
                        ) {
                            Image(
                                painter = painterResource(
                                    if (notices[idx]?.category == "AUCTION") R.drawable.notice_point_icon
                                    else R.drawable.notice_visit_icon
                                ),
                                modifier = Modifier
                                    .padding(start = 30.dp, end = 25.dp)
                                    .size(30.dp),
                                contentDescription = null
                            )
                            Text(
                                text = notices[idx]?.content ?: "",
                                modifier = Modifier.padding(end = 30.dp),
                                fontSize = 15.sp
                            )
                        }

                        Divider(
                            thickness = 1.dp,
                            color = Color(0xFFE9E9E9),
                        )
                    }
                }
            }
        }
    }
}