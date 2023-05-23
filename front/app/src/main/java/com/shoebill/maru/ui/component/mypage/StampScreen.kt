package com.shoebill.maru.ui.component.mypage

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.shoebill.maru.R
import com.shoebill.maru.model.data.Stamp
import com.shoebill.maru.viewmodel.MyPageViewModel
import com.shoebill.maru.viewmodel.NavigateViewModel

@Composable
fun StampScreen(
    myPageViewModel: MyPageViewModel = viewModel(),
    navigateViewModel: NavigateViewModel = hiltViewModel(),
) {
    val stamps = myPageViewModel.getStampPagination(navigateViewModel.navigator!!).collectAsLazyPagingItems()

    LazyVerticalGrid(columns = GridCells.Adaptive(minSize = 128.dp)) {
        items(stamps.itemCount) { idx ->
            StampItem(stamps[idx]!!)
        }
    }
}

@Composable
fun StampItem(stamp: Stamp, navigateViewModel: NavigateViewModel = viewModel()) {
    if (stamp.imageUrl != null)
        AsyncImage(
            model = stamp.imageUrl,
            contentDescription = "stamp item",
            modifier = Modifier
                .size(120.dp)
                .padding(horizontal = 0.5.dp, vertical = 0.5.dp)
                .clickable {
                    navigateViewModel.navigator?.navigate("main/${stamp.spotId}")
                },
            contentScale = ContentScale.Crop
        )
    else
        Image(
            painter = painterResource(id = R.drawable.locked_stamp),
            contentDescription = "locked stamp item",
            modifier = Modifier
                .size(120.dp)
                .padding(horizontal = 0.5.dp, vertical = 0.5.dp),
            contentScale = ContentScale.Crop
        )
}

