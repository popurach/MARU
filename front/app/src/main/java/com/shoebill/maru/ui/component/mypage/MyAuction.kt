package com.shoebill.maru.ui.component.mypage

import androidx.compose.foundation.BorderStroke
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
import com.shoebill.maru.ui.component.common.Chip
import com.shoebill.maru.ui.component.common.GradientColoredText
import com.shoebill.maru.viewmodel.MyBiddingViewModel

@Composable
fun MyAuction(myBiddingViewModel: MyBiddingViewModel = hiltViewModel()) {
    val myBiddings = myBiddingViewModel.getMyBiddingPagination().collectAsLazyPagingItems()
//    Log.d("MYBIDDINGS", "MyAuction: $myBiddings")

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxSize()
    ) {
        items(myBiddings) { myBidding ->
            Box {
                myBidding?.let {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 15.dp)
                    ) {
                        GradientColoredText(
                            text = myBidding.landmark.landmarkName,
                            modifier = Modifier.padding(end = 15.dp),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Chip(
                            text = "입찰 포기",
                            textColor = Color.LightGray,
                            color = Color.White,
                            border = BorderStroke(1.dp, Color(0XFFECECEC))
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