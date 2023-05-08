package com.shoebill.maru.ui.component.bottomsheet.spotlist

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.shoebill.maru.ui.theme.MaruBrush
import com.shoebill.maru.viewmodel.MapViewModel

@OptIn(ExperimentalTextApi::class)
@Composable
fun SpotList(
    mapViewModel: MapViewModel = viewModel(),
) {
    val isBottomSheetOpen = mapViewModel.bottomSheetOpen.observeAsState()
    BackHandler(isBottomSheetOpen.value == true) {
        mapViewModel.updateBottomSheetState(false)
    }
    val spotList = mapViewModel.spotList.observeAsState(listOf())
    val fontSize = 20.sp
    val annotatedText = buildAnnotatedString {
        withStyle(
            SpanStyle(
                MaruBrush,
                fontSize = fontSize,
                fontWeight = FontWeight.Bold,
            )
        ) {
            append("Shoebill")
        }

        withStyle(
            style = SpanStyle(fontSize = fontSize)
        ) {
            append("님을 위한 ")
        }

        withStyle(
            style = SpanStyle(
                fontWeight = FontWeight.Bold,
                fontSize = fontSize,
            )
        ) {
            append("내 근처 SPOT!") // "World"에 색상을 적용합니다.
        }
    }
    Column {
        Box(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 27.dp)
                .padding(top = 58.dp, bottom = 26.dp)
        ) {
            Text(text = annotatedText)
        }
        LazyColumn() {
            items(spotList.value) { spot ->
                SpotListItem(spot)
            }
        }
    }
}

