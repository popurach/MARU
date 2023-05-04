package com.shoebill.maru.ui.component.auction

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.shoebill.maru.ui.component.common.CustomAlertDialog
import com.shoebill.maru.ui.component.common.GradientButton
import com.shoebill.maru.ui.component.common.GradientColoredText
import com.shoebill.maru.ui.theme.MaruBrush
import com.shoebill.maru.viewmodel.AuctionViewModel
import com.shoebill.maru.viewmodel.MyPageViewModel
import com.shoebill.maru.viewmodel.NavigateViewModel
import java.text.DecimalFormat

@Composable
fun BiddingConfirmModal(
    navigateViewModel: NavigateViewModel = viewModel(),
    auctionViewModel: AuctionViewModel = viewModel(),
    myPageViewModel: MyPageViewModel = viewModel(),
    onDismissRequest: () -> Unit
) {
    val bid = auctionViewModel.bid.observeAsState()
    val dec = DecimalFormat("#,###")

    CustomAlertDialog(onDismissRequest = { onDismissRequest() }) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(horizontal = 25.dp)
                    .height(270.dp)
            ) {
                Text(
                    modifier = Modifier
                        .padding(top = 24.dp, bottom = 8.dp)
                        .padding(start = 24.dp, end = 24.dp, top = 10.dp, bottom = 4.dp),
                    text = "$ ${dec.format(bid.value)}",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Text(
                    modifier = Modifier
                        .padding(bottom = 24.dp),
//                        .padding(horizontal = 24.dp),
                    textAlign = TextAlign.Center,
                    text = "위의 입찰가로 경매에 참여하시겠습니까?",
                    fontSize = 14.sp,
                    color = Color.LightGray
                )
                GradientButton(
                    text = "입찰하기",
                    gradient = MaruBrush,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .clip(RoundedCornerShape(40.dp)),
                    onClick = {
                        auctionViewModel.createBidding { success ->
                            if (success) {
                                navigateToMyPage(3, myPageViewModel, navigateViewModel)
                            } else {
                                Log.e("AUCTION", "createBidding fail")
                            }
                        }
                    }
                )
                Box(
                    modifier = Modifier
                        .height(60.dp)
                        .padding(top = 12.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(40.dp))
                        .background(Color.Transparent)
                        .clickable { onDismissRequest() },
                ) {
                    GradientColoredText(
                        text = "입찰가 수정하기",
                        modifier = Modifier
                            .align(Alignment.Center),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

fun navigateToMyPage(
    tabIndex: Int,
    myPageViewModel: MyPageViewModel,
    navigateViewModel: NavigateViewModel,
) {
    myPageViewModel.switchTab(
        tabIndex
    )
    navigateViewModel.navigator!!.navigate("mypage")
}

