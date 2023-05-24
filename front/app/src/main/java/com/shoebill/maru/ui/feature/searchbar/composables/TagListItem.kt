package com.shoebill.maru.ui.feature.searchbar.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.shoebill.maru.R
import com.shoebill.maru.model.data.search.SearchTag
import com.shoebill.maru.ui.feature.map.MapViewModel
import com.shoebill.maru.ui.feature.searchbar.SearchBarViewModel

@Composable
fun TagListItem(
    tag: SearchTag,
    searchBarViewModel: SearchBarViewModel = hiltViewModel(),
    mapViewModel: MapViewModel = hiltViewModel()
) {
    Box(
        modifier = Modifier
            .width(320.dp)
            .height(50.dp)
            .background(color = Color.White, shape = RoundedCornerShape(8.dp))
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .clickable {
                    searchBarViewModel.updateKeyword("#${tag.name}")
                    mapViewModel.updateTagId(tag.id)
                    mapViewModel.loadMarker()
                    mapViewModel.clearFocus()
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.padding(top = 15.dp, bottom = 15.dp, start = 6.dp, end = 20.dp),
                painter = painterResource(id = R.drawable.location_on),
                contentDescription = "",
                tint = Color.Unspecified
            )
            Column() {
                Text(text = "# ${tag.name}")
            }
        }
    }
}