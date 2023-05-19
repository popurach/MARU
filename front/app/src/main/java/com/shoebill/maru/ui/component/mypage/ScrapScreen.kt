package com.shoebill.maru.ui.component.mypage

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.shoebill.maru.model.data.Spot
import com.shoebill.maru.viewmodel.MyPageViewModel
import com.shoebill.maru.viewmodel.NavigateViewModel


@Composable
fun ScrapScreen(
    myPageViewModel: MyPageViewModel = hiltViewModel(),
) {
    val scrapedSpots = myPageViewModel.getScrapedSpotsPagination().collectAsLazyPagingItems()

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 128.dp)
    ) {
        if (scrapedSpots.itemCount == 0) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .height(400.dp)
                ) {
                    Text(
                        text = "저장된 스크랩이 없습니다.",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
        items(scrapedSpots.itemCount) { idx ->
            ScrapItem(scrapedSpots[idx]!!)
        }
    }
}

@Composable
fun ScrapItem(
    spot: Spot,
    navigateViewModel: NavigateViewModel = viewModel(),
) {
    AsyncImage(
        model = spot.imageUrl,
        contentDescription = "scrap image",
        modifier = Modifier
            .size(120.dp)
            .clickable {
                navigateViewModel.navigator?.navigate("main/${spot.id}")
            },
        contentScale = ContentScale.Crop
    )
}