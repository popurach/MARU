package com.shoebill.maru.viewmodel

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.lifecycle.ViewModel
import com.shoebill.maru.model.data.Landmark
import com.shoebill.maru.ui.theme.MaruBrush

@OptIn(ExperimentalTextApi::class)
class LandmarkLandingViewModel : ViewModel() {
    val landmark: Landmark
    val coloredLandmarkName: AnnotatedString

    init {
        landmark = Landmark(
            "서울현대백화점",
            true,
            "https://picsum.photos/id/29/200/300",
            "Shoebill",
            "나는 서울현대박물관의 주인 Shoebill이다. 나의 공간에서 잘 즐기다가도록 하여라."
        )
        coloredLandmarkName = buildAnnotatedString {
            withStyle(
                SpanStyle(
                    brush = MaruBrush,
                    fontWeight = FontWeight.Bold,
                )
            ) {
                append(landmark.name) // "Hello"에 색상을 적용합니다.
            }
        }
        // TODO landmark 정보 불러오기
    }
}