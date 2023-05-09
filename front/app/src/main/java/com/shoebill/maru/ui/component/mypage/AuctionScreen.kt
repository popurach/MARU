package com.shoebill.maru.ui.component.mypage

import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.shoebill.maru.ui.theme.Pretendard
import com.shoebill.maru.viewmodel.AuctionScreenViewModel
import com.shoebill.maru.viewmodel.CountdownViewModel


@Composable
fun AuctionScreen(
    auctionScreenViewModel: AuctionScreenViewModel = hiltViewModel()
) {
    val tabIndex = auctionScreenViewModel.tabIndex.observeAsState(0).value
    val tabs = listOf("참여 중인 경매", "참여 가능한 경매")

    Column() {
        CountdownTimer(viewModel = CountdownViewModel())
        TabRow(
            selectedTabIndex = tabIndex,
            backgroundColor = MaterialTheme.colors.background,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    color = Color(0xFF6039DF),
                    height = 2.dp,
                    modifier = Modifier.tabIndicatorOffset(tabPositions[tabIndex])
                )
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    text = {
                        Text(
                            text = title,
                            color = if (tabIndex == index) Color(0xFF6039DF) else Color.Black,
                            fontSize = 14.sp,
                            fontFamily = Pretendard,
                            letterSpacing = -(0.3).sp
                        )
                    },
                    selected = tabIndex == index,
                    onClick = { auctionScreenViewModel.switchTab(index) },
                )
            }
        }

        when (tabIndex) {
            0 -> MyAuction()
            1 -> OpenAuction()
        }
    }

}


