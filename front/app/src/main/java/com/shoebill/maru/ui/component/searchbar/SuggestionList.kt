package com.shoebill.maru.ui.component.searchbar

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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.shoebill.maru.R
import com.shoebill.maru.model.data.Place
import com.shoebill.maru.ui.component.SearchListItem
import com.shoebill.maru.viewmodel.MapViewModel
import com.shoebill.maru.viewmodel.SearchBarViewModel

@Composable
fun SuggestionList(
    searchBarViewModel: SearchBarViewModel = hiltViewModel(),
) {
    val recommendedPlaces = searchBarViewModel.recommendedPlaces.observeAsState()

    Column {
        val searchItems = searchBarViewModel.prefUtil.loadSearchHistory()
        for (keyword in searchItems) {
            SearchListItem(keyword)
        }
        for (place in recommendedPlaces.value!!) {
            SearchRecommendListItem(place)
        }
    }
}


@Composable
fun SearchRecommendListItem(
    place: Place,
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
                    mapViewModel.moveCamera(lng = place.lng, lat = place.lat)

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
                Text(text = place.placeName)
                Text(text = place.addressName, fontSize = 12.sp, color = Color.Gray)
            }
        }
    }
}