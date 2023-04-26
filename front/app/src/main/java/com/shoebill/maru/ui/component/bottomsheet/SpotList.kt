package com.shoebill.maru.ui.component.bottomsheet

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.shoebill.maru.viewmodel.MapViewModel

@OptIn(ExperimentalTextApi::class)
@Composable
fun SpotList(
    mapViewModel: MapViewModel = viewModel()
) {
    val spotList = mapViewModel.spotList.observeAsState(listOf())
    val nickname = "Shoebill"
    val fontSize = 20.sp
    val annotatedText = buildAnnotatedString {
        withStyle(
            SpanStyle(
                brush = Brush.linearGradient(
                    listOf(
                        Color(0xFF6039DF),
                        Color(0xFFA14AB7)
                    )
                ),
                fontSize = fontSize,
                fontWeight = FontWeight.Bold,
            )
        ) {
            append(nickname) // "Hello"에 색상을 적용합니다.
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

    Box(
        Modifier
            .fillMaxWidth()
            .padding(27.dp)
    ) {
        Text(text = annotatedText)
    }
    LazyColumn() {
        items(spotList.value) { spot ->
            SpotListItem(spot)
        }
    }
}

