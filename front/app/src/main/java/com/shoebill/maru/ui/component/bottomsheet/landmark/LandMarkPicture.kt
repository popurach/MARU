package com.shoebill.maru.ui.component.bottomsheet.landmark

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.shoebill.maru.viewmodel.BottomSheetNavigatorViewModel
import com.shoebill.maru.viewmodel.LandmarkOccupantPictureViewModel

@Composable
fun LandMarkPicture(
    landmarkOccupantPictureViewModel: LandmarkOccupantPictureViewModel = hiltViewModel(),
    bottomSheetNavigatorViewModel: BottomSheetNavigatorViewModel = hiltViewModel()
) {
    AsyncImage(
        model = landmarkOccupantPictureViewModel.representImageUrl,
        contentDescription = "occupant image",
        modifier = Modifier
            .fillMaxSize()
            .clickable { bottomSheetNavigatorViewModel.navController?.navigate("landmark/picture/list") },
        contentScale = ContentScale.Crop
    )
}