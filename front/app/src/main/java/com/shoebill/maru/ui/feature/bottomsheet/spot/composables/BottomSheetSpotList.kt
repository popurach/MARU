package com.shoebill.maru.ui.bottomsheet

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.shoebill.maru.ui.feature.bottomsheet.spot.SpotListItem
import com.shoebill.maru.ui.feature.map.MapViewModel

@Composable
fun BottomSheetSpotList(
    mapViewModel: MapViewModel = viewModel(),
) {
    val spotList = mapViewModel.spotList.collectAsLazyPagingItems()

    LazyColumn() {
        if (spotList.itemCount == 0)
            item {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .height(300.dp)
                ) {
                    Text(
                        text = "근처에 SPOT이 없습니다.",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        else {
            items(spotList.itemCount) { idx ->
                SpotListItem(spotList[idx]!!)
            }
        }
    }
}