package com.shoebill.maru.ui.component.mypage

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.shoebill.maru.ui.component.auction.DeleteConfirmModal
import com.shoebill.maru.ui.component.common.GradientColoredText
import com.shoebill.maru.ui.theme.MaruBrush
import com.shoebill.maru.viewmodel.MyBiddingViewModel
import com.shoebill.maru.viewmodel.NavigateViewModel

@Composable
fun MyAuction(
    myBiddingViewModel: MyBiddingViewModel = hiltViewModel(),
    navigateViewModel: NavigateViewModel = viewModel(),
) {
    val myBiddings = myBiddingViewModel.getMyBiddingPagination().collectAsLazyPagingItems()
    val isDeleteModalOpen = remember { mutableStateOf(false) }
    val selectedId = remember {
        mutableStateOf<Long>(-1)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        if (myBiddings.itemCount == 0) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .height(400.dp)
                ) {
                    Text(
                        text = "참여 중인 경매가 없습니다.",
                        modifier = Modifier.align(Alignment.Center),
                        letterSpacing = -(0.3).sp
                    )
                }
            }
        }
        items(myBiddings) { myBidding ->
            Box {
                myBidding?.let {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp, bottom = 20.dp, start = 30.dp, end = 20.dp)
                    ) {
                        GradientColoredText(
                            text = myBidding.landmark.name,
                            fontSize = 19.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Row() {
                            Surface(
                                color = Color.White,
                                shape = RoundedCornerShape(16.dp),
                                border = BorderStroke(0.5.dp, Color(0XFFCECECE)),
                                elevation = 2.dp,
                                modifier = Modifier
                                    .padding(end = 5.dp)
                                    .clickable {
                                        selectedId.value = myBidding.id
                                        isDeleteModalOpen.value = true
                                    }
                            ) {
                                Text(
                                    text = "입찰 포기",
                                    modifier = Modifier.padding(
                                        horizontal = 10.dp,
                                        vertical = 5.dp
                                    ),
                                    fontSize = 12.sp,
                                    color = Color(0XFFCECECE)
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .shadow(elevation = 2.dp, RoundedCornerShape(16.dp))
                                    .background(MaruBrush, RoundedCornerShape(16.dp))
                                    .clickable { navigateViewModel.navigator!!.navigate("auction/${myBidding.landmark.id}") }
                            ) {
                                Text(
                                    text = "재입찰",
                                    modifier = Modifier.padding(
                                        horizontal = 10.dp,
                                        vertical = 5.dp
                                    ),
                                    fontSize = 12.sp,
                                    color = Color.White
                                )
                            }
                        }
                    }

                    Divider(
                        thickness = 1.dp,
                        color = Color(0xFFE9E9E9),
                    )

                    if (isDeleteModalOpen.value) {
                        DeleteConfirmModal(selectedId.value) {
                            isDeleteModalOpen.value = false
                        }
                    }
                }
            }
        }
    }
}