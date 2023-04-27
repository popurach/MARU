package com.shoebill.maru.viewmodel

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.lifecycle.ViewModel
import com.shoebill.maru.model.data.Landmark
import com.shoebill.maru.model.data.Occupant
import com.shoebill.maru.model.data.SpotSimple

@OptIn(ExperimentalTextApi::class)
class LandmarkViewModel : ViewModel() {
    val landmark: Landmark
    val coloredLandmarkName: AnnotatedString

    init {
        landmark = Landmark(
            "서울현대백화점",
            Occupant(
                "Shoebill",
                "https://picsum.photos/200",
                "나는 서울현대박물관의 주인 Shoebill이다. 나의 공간에서 잘 즐기다가도록 하여라."
            ),
            listOf(
                SpotSimple("https://picsum.photos/id/10/200/200", 0),
                SpotSimple("https://picsum.photos/id/11/200/200", 1),
                SpotSimple("https://picsum.photos/id/12/200/200", 2),
                SpotSimple("https://picsum.photos/id/13/200/200", 3),
                SpotSimple("https://picsum.photos/id/14/200/200", 4),
            )
        )
        coloredLandmarkName = buildAnnotatedString {
            withStyle(
                SpanStyle(
                    brush = Brush.linearGradient(
                        listOf(
                            Color(0xFF6039DF),
                            Color(0xFFA14AB7)
                        )
                    ),
                    fontWeight = FontWeight.Bold,
                )
            ) {
                append("서울현대박물관") // "Hello"에 색상을 적용합니다.
            }
        }
        // TODO landmark 정보 불러오기
    }
}