package com.shoebill.maru.viewmodel

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.lifecycle.ViewModel
import com.shoebill.maru.model.data.Coordinate
import com.shoebill.maru.model.data.Landmark
import com.shoebill.maru.ui.theme.MaruBrush

@OptIn(ExperimentalTextApi::class)
class LandmarkLandingViewModel : ViewModel() {
    val landmark: Landmark
    val coloredLandmarkName: AnnotatedString

    init {
        landmark = Landmark(
            1,
            "서울현대백화점",
            Coordinate(127.048196645563, 37.64765601365385),
            true,
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