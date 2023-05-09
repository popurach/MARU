package com.shoebill.maru.ui.component.bottomsheet.landmark

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.shoebill.maru.ui.component.LottieDiamond
import com.shoebill.maru.ui.component.common.GradientColoredText
import com.shoebill.maru.ui.theme.MaruBrush
import com.shoebill.maru.viewmodel.BottomSheetNavigatorViewModel
import com.shoebill.maru.viewmodel.LandmarkInfoViewModel
import com.shoebill.maru.viewmodel.MemberViewModel
import com.shoebill.maru.viewmodel.NavigateViewModel
import kotlinx.coroutines.DelicateCoroutinesApi

@OptIn(ExperimentalTextApi::class, DelicateCoroutinesApi::class)
@Composable
fun LandmarkFirstVisit(
    landmarkId: Long,
    landmarkInfoViewModel: LandmarkInfoViewModel = hiltViewModel(),
    bottomSheetNavigatorViewModel: BottomSheetNavigatorViewModel = viewModel(),
    memberViewModel: MemberViewModel = hiltViewModel(),
    navigatorViewModel: NavigateViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    landmarkInfoViewModel.initLandmarkInfo(landmarkId)
    val landmarkName = landmarkInfoViewModel.landmarkName.observeAsState()

    val firstLine = buildAnnotatedString {
        withStyle(
            SpanStyle(
                brush = MaruBrush,
                fontWeight = FontWeight.Bold,
            )
        ) {
            append(landmarkName.value) // "Hello"에 색상을 적용합니다.
        }
        append(" 첫 방문을 축하합니다!")
    }
    val secondLine = buildAnnotatedString {
        withStyle(
            SpanStyle(
                fontWeight = FontWeight.Bold,
            )
        ) {
            append("위의 보석을 터치") // "Hello"에 색상을 적용합니다.
        }
        append(" 하셔서 포인트를 획득하세요!")
    }
    Column(
        Modifier
            .fillMaxSize()
            .padding(top = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(50.dp)
    ) {
        GradientColoredText(
            text = landmarkName.value!!,
            fontSize = 32.sp,
            fontWeight = FontWeight.SemiBold
        )
        Box(Modifier.size(250.dp)) {
            LottieDiamond(onClick = {
                landmarkInfoViewModel.visitLandmark(context)
                memberViewModel.getMemberInfo(navigatorViewModel)
                bottomSheetNavigatorViewModel.navController?.navigate("landmark/main/$landmarkId") {
                    popUpTo("landmark/first/$landmarkId") { inclusive = true }
                }
            })
        }
        Column(Modifier.padding(top = 70.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = firstLine, fontSize = 16.sp)
            Text(text = secondLine, fontSize = 16.sp)
        }
    }
}