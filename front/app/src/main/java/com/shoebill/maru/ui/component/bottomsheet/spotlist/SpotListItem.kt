package com.shoebill.maru.ui.component.bottomsheet.spotlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.shoebill.maru.R
import com.shoebill.maru.model.data.Spot
import com.shoebill.maru.ui.component.common.Chip
import com.shoebill.maru.viewmodel.BottomSheetNavigatorViewModel

@Composable
fun SpotListItem(
    spot: Spot,
    bottomSheetNavigatorViewModel: BottomSheetNavigatorViewModel = hiltViewModel()
) {
    Column(
        Modifier
            .padding(horizontal = 27.dp)
            .padding(top = 18.dp, bottom = 7.dp)
            .clickable { bottomSheetNavigatorViewModel.navController?.navigate("spot/detail/${spot.id}") }
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(spot.imageUrl)
                    .size(400, 200)
                    .build(),
                contentDescription = "spot image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                loading = {
                    Box() {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(100.dp)
                                .align(Alignment.Center),
                            strokeWidth = 10.dp
                        )
                    }
                }
            )
            Box(
                Modifier
                    .padding(15.dp)
                    .align(Alignment.TopEnd)
            ) {
                Icon(
                    painter = painterResource(
                        id = if (spot.scraped) {
                            R.drawable.scrap_icon
                        } else {
                            R.drawable.spot_unscrap_icon
                        }
                    ),
                    contentDescription = "scrap icon",
                    modifier = Modifier
                        .size(25.dp),
                    tint = Color.White
                )
            }

        }
        val scrollState = rememberScrollState()
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.horizontalScroll(scrollState)
        ) {
            spot.tags?.forEach() { tag ->
                Chip(text = "# ${tag.name}")
            }
        }
    }
}