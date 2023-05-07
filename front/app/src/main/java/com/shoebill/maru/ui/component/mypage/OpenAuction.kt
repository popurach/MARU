package com.shoebill.maru.ui.component.mypage

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.shoebill.maru.ui.component.common.GradientColoredText
import com.shoebill.maru.viewmodel.MyBiddingViewModel

@Composable
fun OpenAuction(myBiddingViewModel: MyBiddingViewModel = hiltViewModel()) {
    val myNonBiddings = myBiddingViewModel.getMyNonBiddingPagination().collectAsLazyPagingItems()
//    Log.d("MYNONBIDDINGS", "OpenAuction: $myNonBiddings")

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxSize()
    ) {
        items(myNonBiddings) { myNonBidding ->
            Box {
                myNonBidding?.let {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 15.dp)
                    ) {
                        GradientColoredText(
                            text = myNonBidding.landmarkName,
                            modifier = Modifier.padding(end = 15.dp),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
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