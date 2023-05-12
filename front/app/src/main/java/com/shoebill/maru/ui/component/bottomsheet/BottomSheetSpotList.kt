package com.shoebill.maru.ui.component.bottomsheet

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.shoebill.maru.ui.component.bottomsheet.spotlist.SpotListItem
import com.shoebill.maru.viewmodel.MapViewModel

@Composable
fun BottomSheetSpotList(
    mapViewModel: MapViewModel = viewModel(),
) {
    val spotList = mapViewModel.spotList.observeAsState(listOf())

    LazyColumn() {
        if (spotList.value.isNotEmpty())
            items(spotList.value) { spot ->
                SpotListItem(spot)
            }
        else {
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
        }
    }
}