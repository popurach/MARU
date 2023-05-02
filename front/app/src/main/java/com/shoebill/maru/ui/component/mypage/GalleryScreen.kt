package com.shoebill.maru.ui.component.mypage

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.shoebill.maru.model.data.Spot
import com.shoebill.maru.viewmodel.MyPageViewModel

@Composable
fun GalleryScreen(
    myPageViewModel: MyPageViewModel = viewModel(),
) {
    val galleryList = myPageViewModel.getGalleryPagination().collectAsLazyPagingItems()

    LazyVerticalGrid(columns = GridCells.Adaptive(minSize = 128.dp)) {
        items(galleryList.itemCount) { idx ->
            GalleryItem(galleryList[idx]!!)
        }
    }
}

@Composable
fun GalleryItem(spot: Spot) {
    AsyncImage(
        model = spot.imageUrl,
        contentDescription = "gallery item",
        modifier = Modifier
            .size(120.dp)
            .padding(horizontal = 0.5.dp, vertical = 0.5.dp),
        contentScale = ContentScale.Crop
    )
}