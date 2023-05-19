package com.shoebill.maru.ui.component.bottomsheet.landmark

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.shoebill.maru.viewmodel.BottomSheetNavigatorViewModel
import com.shoebill.maru.viewmodel.LandmarkPictureListViewModel

@Composable
fun LandmarkPictureList(
    viewModel: LandmarkPictureListViewModel = hiltViewModel(),
    bottomSheetNavigatorViewModel: BottomSheetNavigatorViewModel = hiltViewModel()
) {
    val pictureList = viewModel.pictureList.observeAsState(listOf<String>())
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 128.dp)
    ) {
        items(pictureList.value.size) { idx ->
            AsyncImage(
                model = pictureList.value[idx],
                contentDescription = "landmark picture",
                modifier = Modifier
                    .size(120.dp)
                    .clickable { bottomSheetNavigatorViewModel.navController?.navigate("spot/detail/$idx") },
                contentScale = ContentScale.Crop
            )
        }
    }
}