package com.shoebill.maru.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.shoebill.maru.viewmodel.MapViewModel

@Composable
fun FilterChips(
    mapViewModel: MapViewModel = viewModel()
) {
    val textList = listOf("전체", "랜드 마크", "스팟", "MY 스팟")
    val selectedChipIndex = remember { mutableStateOf(0) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 11.dp, start = 10.dp)
    ) {
        Row(
            modifier = Modifier
                .width(320.dp)
                .align(Alignment.Center),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            for (index in 0..3) {
                ChipListItem(
                    text = textList[index],
                    isSelected = index == selectedChipIndex.value,
                    onSelected = {
                        mapViewModel.clearFocus()
                        selectedChipIndex.value = index
                        // TODO filtering된 핀 목록 가져오기
                    }
                )
            }
        }
    }
}