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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.shoebill.maru.ui.component.common.CustomAlertDialog
import com.shoebill.maru.ui.component.common.GradientButton
import com.shoebill.maru.ui.component.common.GradientColoredText
import com.shoebill.maru.ui.theme.MaruBrush
import com.shoebill.maru.viewmodel.AuctionViewModel
import com.shoebill.maru.viewmodel.MemberViewModel
import com.shoebill.maru.viewmodel.MyPageViewModel
import com.shoebill.maru.viewmodel.NavigateViewModel

@Composable
fun DeleteConfirmModal(
    auctionLogId: Long,
    navigateViewModel: NavigateViewModel = viewModel(),
    auctionViewModel: AuctionViewModel = viewModel(),
    myPageViewModel: MyPageViewModel = viewModel(),
    memberViewModel: MemberViewModel = hiltViewModel(),
    onDismissRequest: () -> Unit
) {

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
                    .height(250.dp)
            ) {
                Text(
                    modifier = Modifier
                        .padding(top = 24.dp, bottom = 8.dp)
                        .padding(start = 24.dp, end = 24.dp, top = 10.dp, bottom = 20.dp),
                    text = "정말 해당 입찰을\n" + "포기하시겠습니까?",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                )
                GradientButton(
                    text = "돌아가기",
                    gradient = MaruBrush,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .clip(RoundedCornerShape(40.dp)),
                    onClick = { onDismissRequest() }
                )
                Box(
                    modifier = Modifier
                        .height(60.dp)
                        .padding(top = 12.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(40.dp))
                        .background(Color.Transparent)
                        .clickable {
                            auctionViewModel.deleteBidding(auctionLogId) { success ->
                                if (success) {
                                    onDismissRequest()
                                    memberViewModel.getMemberInfo(navigateViewModel)
                                    navigateViewModel.navigator?.navigateUp()
                                    navigateToMyPage2(3, myPageViewModel, navigateViewModel)
                                } else {
                                    Log.e("AUCTION", "deleteBidding fail")
                                }
                            }
                        },
                ) {
                    GradientColoredText(
                        text = "입찰 포기하기",
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

fun navigateToMyPage2(
    tabIndex: Int,
    myPageViewModel: MyPageViewModel,
    navigateViewModel: NavigateViewModel,
) {
    myPageViewModel.switchTab(
        tabIndex
    )
    navigateViewModel.navigator!!.navigate("mypage")
}