package com.shoebill.maru.ui.component.mypage

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.shoebill.maru.ui.component.common.GradientColoredText
import com.shoebill.maru.ui.theme.MaruBrush
import com.shoebill.maru.viewmodel.MyBiddingViewModel
import com.shoebill.maru.viewmodel.NavigateViewModel

@Composable
fun OpenAuction(
    myBiddingViewModel: MyBiddingViewModel = hiltViewModel(),
    navigateViewModel: NavigateViewModel = viewModel(),
) {
    val landmarkInfos = myBiddingViewModel.getMyNonBiddingPagination().collectAsLazyPagingItems()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        if (landmarkInfos.itemCount == 0) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .height(400.dp)
                ) {
                    Text(
                        text = "참여 가능한 경매가 없습니다.",
                        modifier = Modifier.align(Alignment.Center),
                        letterSpacing = -(0.3).sp
                    )
                }
            }
        }
        items(landmarkInfos) { landmarkInfo ->
            Box {
                landmarkInfo?.let {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp, bottom = 20.dp, start = 30.dp, end = 20.dp)
                    ) {
                        GradientColoredText(
                            text = landmarkInfo.name,
                            fontSize = 19.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Box(
                            modifier = Modifier
                                .shadow(elevation = 2.dp, RoundedCornerShape(16.dp))
                                .background(MaruBrush, RoundedCornerShape(16.dp))
                                .clickable { navigateViewModel.navigator!!.navigate("auction/${landmarkInfo.id}") }
                        ) {
                            Text(
                                text = "경매 참여",
                                modifier = Modifier.padding(
                                    horizontal = 10.dp,
                                    vertical = 5.dp
                                ),
                                fontSize = 12.sp,
                                color = Color.White
                            )
                        }
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