package com.shoebill.maru.ui.component.notice

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.shoebill.maru.R
import com.shoebill.maru.viewmodel.NoticeViewModel

@Composable
fun NoticeList(noticeViewModel: NoticeViewModel = hiltViewModel()) {
    val notices = noticeViewModel.getNoticePagination().collectAsLazyPagingItems()

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxSize()
    ) {
        items(notices) { notice ->
            Box {
                notice?.let {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 15.dp)
                    ) {
                        Image(
                            painter = painterResource(
                                if (notice.category == "AUCTION") R.drawable.notice_point_icon
                                else R.drawable.notice_visit_icon
                            ),
                            modifier = Modifier
                                .padding(start = 20.dp, end = 20.dp)
                                .size(40.dp),
                            contentDescription = null
                        )
                        Text(
                            text = notice.content,
                            modifier = Modifier.padding(end = 15.dp)
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

@Composable
fun NoticeListItem(message: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {

    }
}