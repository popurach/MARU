package com.shoebill.maru.ui.component.bottomsheet

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.shoebill.maru.viewmodel.MapViewModel

@Composable
fun SpotList(
    mapViewModel: MapViewModel = viewModel()
) {
    val spotList = mapViewModel.spotList.observeAsState(listOf())
    LazyColumn() {
        items(spotList.value) { spot ->
            SpotListItem(spot)
        }
    }
}

