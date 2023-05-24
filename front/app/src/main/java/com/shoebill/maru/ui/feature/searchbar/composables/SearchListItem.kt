package com.shoebill.maru.ui.feature.searchbar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.shoebill.maru.R
import com.shoebill.maru.model.data.Place
import com.shoebill.maru.model.data.SearchHistoryWrapper
import com.shoebill.maru.model.data.Tag
import com.shoebill.maru.ui.feature.map.MapViewModel

@Composable
fun SearchListItem(
    searchedItem: SearchHistoryWrapper,
    searchBarViewModel: SearchBarViewModel = hiltViewModel(),
    mapViewModel: MapViewModel = hiltViewModel(),
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
                .fillMaxWidth()
                .clickable {
                    searchBarViewModel.prefUtil.saveSearchHistory(searchedItem)

                    when (searchedItem.type) {
                        "place" -> {
                            val place = searchedItem.getData() as Place
                            mapViewModel.moveCamera(place.lat, place.lng)
                        }

                        "tag" -> {
                            val tag = searchedItem.getData() as Tag
                            // tag 관련 로직 추가 필요
                        }
                    }

                    mapViewModel.clearFocus()
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.padding(top = 15.dp, bottom = 15.dp, start = 6.dp, end = 20.dp),
                painter = painterResource(id = R.drawable.search_icon),
                contentDescription = "",
                tint = Color.Unspecified
            )
            if (searchedItem.type == "tag") {
                val tag = searchedItem.getData() as Tag
                Text(text = tag.name)
            } else if (searchedItem.type == "place") {
                val place = searchedItem.getData() as Place
                Column {
                    Text(text = place.placeName)
                    Text(text = place.addressName, fontSize = 12.sp, color = Color.Gray)
                }
            }
        }
    }
}