package com.shoebill.maru.ui.component.mypage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.shoebill.maru.R
import com.shoebill.maru.viewmodel.MyPageViewModel

@Composable
fun MyPageTabs(
    myPageViewModel: MyPageViewModel = viewModel(),
) {
    val tabIndex = myPageViewModel.tabIndex.observeAsState(initial = 0)
    val iconSize = 22.dp

    val tabs = listOf("사진첩", "스탬프", "스크랩", "경매")

    TabRow(
        selectedTabIndex = tabIndex.value,
        backgroundColor = MaterialTheme.colors.background,
        modifier = Modifier.padding(top = 24.dp),
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                color = Color(0xFF6039DF),
                height = 2.dp,
                modifier = Modifier.tabIndicatorOffset(tabPositions[tabIndex.value])
            )
        }
    ) {
        tabs.forEachIndexed { index, title ->
            Tab(text = {
                Text(
                    text = title,
                    color = if (tabIndex.value == index) Color(0xFF6039DF) else Color.Black,
                    fontSize = 15.sp
                )
            },
                selected = tabIndex.value == index,
                onClick = { myPageViewModel.switchTab(index) },
                icon = {
                    when (index) {
                        0 -> Icon(
                            painterResource(R.drawable.photo_album_icon),
                            "photo_album",
                            Modifier.size(iconSize),
                            tint = if (tabIndex.value == index) Color(0xFF6039DF) else Color.Black,
                        )

                        1 -> Icon(
                            painterResource(R.drawable.stamp_icon),
                            "stamp",
                            Modifier.size(iconSize),
                            tint = if (tabIndex.value == index) Color(0xFF6039DF) else Color.Black,
                        )

                        2 -> Icon(
                            painterResource(R.drawable.unscrap_icon),
                            "scrap",
                            Modifier.size(iconSize),
                            tint = if (tabIndex.value == index) Color(0xFF6039DF) else Color.Black,
                        )

                        3 -> Icon(
                            painterResource(R.drawable.auction_icon),
                            "auction",
                            Modifier.size(iconSize),
                            tint = if (tabIndex.value == index) Color(0xFF6039DF) else Color.Black,
                        )

                    }
                }
            )
        }
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)) {
        when (tabIndex.value) {
            0 -> GalleryScreen()
            1 -> StampScreen()
            2 -> ScrapScreen()
            3 -> AuctionScreen()
        }
    }
}