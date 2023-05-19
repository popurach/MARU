package com.shoebill.maru.ui.component.bottomsheet.landmark

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.shoebill.maru.viewmodel.BottomSheetNavigatorViewModel
import com.shoebill.maru.viewmodel.LandmarkInfoViewModel

@Composable
fun LandMarkPicture(
    landmarkInfoViewModel: LandmarkInfoViewModel = hiltViewModel(),
    bottomSheetNavigatorViewModel: BottomSheetNavigatorViewModel = hiltViewModel()
) {
    val owner = landmarkInfoViewModel.owner.observeAsState()
    AsyncImage(
        model = owner.value!!.spotImageUrl,
        contentDescription = "occupant image",
        modifier = Modifier
            .fillMaxSize()
            .clickable {
                bottomSheetNavigatorViewModel.navController?.navigate("landmark/${landmarkInfoViewModel.landmarkId}/picture/list")
            },
        contentScale = ContentScale.Crop
    )
}