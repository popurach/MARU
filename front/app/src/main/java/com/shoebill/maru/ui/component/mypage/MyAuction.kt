package com.shoebill.maru.ui.component.mypage

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.material.Text
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
fun MyAuction(
    myBiddingViewModel: MyBiddingViewModel = hiltViewModel(),
) {
    val myBiddings = myBiddingViewModel.getMyBiddingPagination().collectAsLazyPagingItems()
    Log.d("MYBIDDINGS", "MyAuction: $myBiddings")

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        items(myBiddings) { myBidding ->
            Box {
                myBidding?.let {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 20.dp, horizontal = 30.dp)
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
                                border = BorderStroke(1.dp, Color(0XFFCECECE)),
                                elevation = 2.dp,
                                modifier = Modifier
                                    .padding(end = 5.dp)
                                    .clickable { }
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
                            Surface(
                                color = Color.Red,
                                shape = RoundedCornerShape(16.dp),
//                                border = BorderStroke(1.dp, Color(0XFFCECECE)),
                                elevation = 2.dp,
                                modifier = Modifier
                                    .clickable { }
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
                }
            }
        }

    }
}