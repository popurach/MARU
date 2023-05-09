package com.shoebill.maru.ui.component.bottomsheet.landmark

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.shoebill.maru.viewmodel.BottomSheetNavigatorViewModel
import com.shoebill.maru.viewmodel.LandmarkPictureListViewModel

@Composable
fun LandmarkPictureList(
    landmarkPictureListViewModel: LandmarkPictureListViewModel = hiltViewModel(),
    bottomSheetNavigatorViewModel: BottomSheetNavigatorViewModel = hiltViewModel(),
    landmarkId: Long
) {
    landmarkPictureListViewModel.initLandmarkId(landmarkId)
    val pictureList =
        landmarkPictureListViewModel.getLandmarkPicturePagination().collectAsLazyPagingItems()
    if (pictureList.itemCount == 0) {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(
                text = "등록된 사진이 없습니다.",
                modifier = Modifier.align(Alignment.Center),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    } else {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 128.dp)
        ) {
            items(pictureList.itemCount) { idx ->
                AsyncImage(
                    model = pictureList[idx]!!.imageUrl,
                    contentDescription = "landmark picture",
                    modifier = Modifier
                        .size(120.dp)
                        .clickable { bottomSheetNavigatorViewModel.navController?.navigate("spot/detail/${pictureList[idx]!!.id}") },
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}