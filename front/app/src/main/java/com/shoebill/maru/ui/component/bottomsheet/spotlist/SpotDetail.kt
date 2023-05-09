package com.shoebill.maru.ui.component.bottomsheet.spotlist

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.shoebill.maru.R
import com.shoebill.maru.ui.component.bottomsheet.BottomSheetIndicator
import com.shoebill.maru.ui.component.common.Chip
import com.shoebill.maru.viewmodel.BottomSheetNavigatorViewModel
import com.shoebill.maru.viewmodel.SpotViewModel

@Composable
fun SpotDetail(
    spotId: Long,
    bottomSheetNavigatorViewModel: BottomSheetNavigatorViewModel = viewModel(),
    spotViewModel: SpotViewModel = hiltViewModel() // 3. spotRepository 가 필요해서 컴포넌트랑 생명주기가 다른게 이상함
) {
    spotViewModel.initSpotId(spotId)
    val spotDetails = spotViewModel.spotDetails.observeAsState()
    spotViewModel.loadSpotDetailById(spotId) // 1. 이렇게 호출해서 spotViewModel 안에 spot detail 을 직접 초기화 해주는게 이상함

    if (spotDetails.value != null) // 2. 데이터가 로드 되기전, imageUrl 때문에 에러가 발생하지 않도록하는게 분기처리가 이상함
        Box(Modifier.fillMaxSize()) {
            Box {
                AsyncImage(
                    model = spotDetails.value?.imageUrl,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                    contentDescription = "image"
                )
                Box(Modifier.padding(top = 38.dp, start = 22.dp)) {
                    Icon(
                        painter = painterResource(id = R.drawable.arrow_back),
                        contentDescription = "back",
                        tint = Color.White,
                        modifier = Modifier
                            .size(30.dp)
                            .clickable {
                                bottomSheetNavigatorViewModel.navController?.navigate("spot/list") {
                                    launchSingleTop = true
                                    popUpTo(0)
                                }
                            }
                    )
                }
                Box(
                    Modifier
                        .padding(top = 38.dp, end = 22.dp)
                        .align(Alignment.TopEnd)
                        .clickable {
                            spotViewModel.toggleScrap(spotId)
                        },
                ) {
                    Icon(
                        // 스크랩 관련 로직 추가
                        // 스크랩 api 호출 추가
                        painter = painterResource(id = if (spotDetails.value?.scraped!!) R.drawable.scrap_icon else R.drawable.unscrap_icon),
                        contentDescription = "",
                        modifier = Modifier
                            .size(35.dp),
                        tint = Color.White
                    )
                }

                Column(
                    Modifier
                        .padding(top = 96.dp, end = 24.dp)
                        .align(Alignment.TopEnd)
                        .clickable {
                            spotViewModel.toggleLike(spotId)
                        },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // 좋아요 여부에 따라 분기
                    Icon(
                        painter = painterResource(id = if (spotDetails.value?.liked!!) R.drawable.favorite_icon else R.drawable.unfavorite_icon),
                        contentDescription = "favorite or not",
                        modifier = Modifier.size(30.dp),
                        tint = Color.White
                    )
                    // 좋아요 갯수?
                    Text(
                        text = spotDetails.value?.likeCount.toString(),
                        color = Color.White,
                        fontSize = 10.sp
                    )
                }
                val tags = spotDetails.value?.tags
                val scrollState = rememberScrollState()
                Box(
                    Modifier
                        .align(Alignment.BottomStart)
                        .padding(bottom = 18.dp, start = 18.dp)
                ) {
                    Row(
                        Modifier.horizontalScroll(scrollState),
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        tags?.forEach { tag ->
                            Chip(
                                text = "#${tag.name}",
                                textColor = Color.White,
                                color = Color.White.copy(alpha = 0.38f),
                                border = BorderStroke(0.dp, Color.Transparent)
                            )
                        }
                    }
                }
            }
            BottomSheetIndicator()
        }
}