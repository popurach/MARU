package com.shoebill.maru.ui.component.mypage

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.shoebill.maru.model.data.Spot
import com.shoebill.maru.viewmodel.MyPageViewModel


@Composable
fun ScrapScreen(
    myPageViewModel: MyPageViewModel = hiltViewModel(),
) {
    val scrapedSpots = myPageViewModel.getScrapedSpotsPagination().collectAsLazyPagingItems()

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 128.dp)
    ) {
        items(scrapedSpots.itemCount) { idx ->
            ScrapItem(scrapedSpots[idx]!!)
        }
    }
}

@Composable
fun ScrapItem(spot: Spot) {
    AsyncImage(
        model = spot.imageUrl,
        contentDescription = "scrap image",
        modifier = Modifier
            .size(120.dp)
            .clickable { },
        contentScale = ContentScale.Crop
    )
}