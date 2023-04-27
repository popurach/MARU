package com.shoebill.maru.ui.component.bottomsheet.landmark

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.shoebill.maru.viewmodel.LandmarkOccupantPictureViewModel

@Composable
fun LandMarkPicture(
    viewModel: LandmarkOccupantPictureViewModel = hiltViewModel()
) {
    AsyncImage(
        model = viewModel.representImageUrl,
        contentDescription = "occupant image",
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )
}