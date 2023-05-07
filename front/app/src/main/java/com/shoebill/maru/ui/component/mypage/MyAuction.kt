package com.shoebill.maru.ui.component.mypage

import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.shoebill.maru.viewmodel.MyBiddingViewModel

@Composable
fun MyAuction(myBiddingViewModel: MyBiddingViewModel = hiltViewModel()) {

    val myBiddings = myBiddingViewModel.myBiddings.observeAsState()
    val myNonBiddings = myBiddingViewModel.myNonBiddings.observeAsState()


//    Column {
//        NoticeList()
//        Divider(
//            thickness = 1.dp,
//            color = Color(0xFFE9E9E9),
//            modifier = Modifier.padding(bottom = 0.dp)
//        )
//    }
}