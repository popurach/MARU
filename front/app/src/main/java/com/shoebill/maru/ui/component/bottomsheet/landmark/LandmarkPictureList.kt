package com.shoebill.maru.ui.component.bottomsheet.landmark

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
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
    Box(modifier = Modifier.fillMaxSize()) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 128.dp)
        ) {
            if (pictureList.itemCount == 0) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .height(400.dp)
                    ) {
                        Text(
                            text = "등록된 사진이 없습니다.",
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            }

            items(pictureList.itemCount) { idx ->
                SubcomposeAsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(pictureList[idx]!!.imageUrl)
                        .size(150)
                        .build(),
                    contentDescription = "landmark picture",
                    modifier = Modifier
                        .size(120.dp)
                        .clickable { bottomSheetNavigatorViewModel.navController?.navigate("spot/detail/${pictureList[idx]!!.id}") },
                    contentScale = ContentScale.Crop,
                    loading = {
                        Box() {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .size(80.dp)
                                    .align(Alignment.Center),
                                strokeWidth = 10.dp
                            )
                        }
                    }
                )
            }
        }
    }
}