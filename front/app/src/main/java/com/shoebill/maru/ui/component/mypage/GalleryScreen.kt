package com.shoebill.maru.ui.component.mypage

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.shoebill.maru.model.data.Spot
import com.shoebill.maru.viewmodel.MyPageViewModel
import com.shoebill.maru.viewmodel.NavigateViewModel

@Composable
fun GalleryScreen(
    myPageViewModel: MyPageViewModel = viewModel(),
    navigateViewModel: NavigateViewModel = viewModel(),
) {
    val galleryList = myPageViewModel.getGalleryPagination().collectAsLazyPagingItems()

    LazyVerticalGrid(columns = GridCells.Adaptive(minSize = 128.dp)) {
        if (galleryList.itemCount == 0) {
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
        items(galleryList.itemCount) { idx ->
            GalleryItem(galleryList[idx]!!, navigateViewModel)
        }
    }
}

@Composable
fun GalleryItem(spot: Spot, navigateViewModel: NavigateViewModel) {
    AsyncImage(
        model = spot.imageUrl,
        contentDescription = "gallery item",
        modifier = Modifier
            .size(120.dp)
            .padding(horizontal = 0.5.dp, vertical = 0.5.dp)
            .clickable {
                navigateViewModel.navigator?.currentBackStackEntry?.savedStateHandle?.set(
                    key = "spotId",
                    value = spot.id
                )
                navigateViewModel.navigator?.currentBackStackEntry?.savedStateHandle?.set(
                    key = "expandState",
                    value = true
                )

                navigateViewModel.navigator?.navigate("main")
            },
        contentScale = ContentScale.Crop
    )
}