package com.shoebill.maru.ui.component.bottomsheet.landmark

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.shoebill.maru.R
import com.shoebill.maru.ui.component.common.GradientColoredText
import com.shoebill.maru.viewmodel.BottomSheetNavigatorViewModel
import com.shoebill.maru.viewmodel.LandmarkInfoViewModel
import com.shoebill.maru.viewmodel.NavigateViewModel

@Composable
fun LandmarkMain(
    landmarkInfoViewModel: LandmarkInfoViewModel = hiltViewModel(),
    bottomSheetNavigatorViewModel: BottomSheetNavigatorViewModel = hiltViewModel(),
    navigatorViewModel: NavigateViewModel = hiltViewModel(),
    landmarkId: Long
) {
    landmarkInfoViewModel.initLandmarkInfo(landmarkId, navigatorViewModel.navigator!!)
    val owner = landmarkInfoViewModel.owner.observeAsState()
    val landmarkName = landmarkInfoViewModel.landmarkName.observeAsState()
    Column(
        Modifier
            .fillMaxSize()
            .padding(top = 50.dp)
            .clickable {
                if (owner.value!!.id > 0L && owner.value!!.spotImageUrl != null) {
                    bottomSheetNavigatorViewModel.navController!!.navigate("landmark/picture/$landmarkId")
                } else {
                    bottomSheetNavigatorViewModel.navController!!.navigate("landmark/$landmarkId/picture/list")
                }

            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(50.dp)
    ) {
        GradientColoredText(
            text = landmarkName.value ?: "",
            fontSize = 32.sp,
            fontWeight = FontWeight.SemiBold
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            Box(Modifier.height(267.dp)) {
                AsyncImage(
                    model = owner.value!!.profileImageUrl,
                    contentDescription = "occupant profile",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(200.dp)
                        .padding(1.dp)
                        .clip(CircleShape)
                        .border(4.dp, Color.White, CircleShape)
                        .align(Alignment.BottomCenter)
                )
                Image(
                    painter = painterResource(id = R.drawable.crown_image),
                    contentDescription = "crown",
                    modifier = Modifier
                        .size(92.dp)
                        .align(Alignment.TopCenter)
                )
            }
            Text(
                text = owner.value!!.nickname,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Text(
            text = "나는 ${landmarkName.value}의 주인 ${owner.value!!.nickname}이다.\n" +
                    "나의 공간에서 잘 즐기다가도록 하여라.",
            Modifier.padding(horizontal = 50.dp),
            textAlign = TextAlign.Center
        )
    }
}