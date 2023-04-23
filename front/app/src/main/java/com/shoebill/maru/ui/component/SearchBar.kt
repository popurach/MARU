package com.shoebill.maru.ui.component

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.shoebill.maru.R
import com.shoebill.maru.viewmodel.SearchBarViewModel

@Preview
@Composable
fun SearchBar(
    searchBarViewModel: SearchBarViewModel = viewModel(),
) {
    val keyword = searchBarViewModel.keyword.observeAsState("")
    BoxWithConstraints() {
        Column {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 50.dp)
            ) {
                TextField(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .width(320.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = Color.Black,
                        cursorColor = Color.Black,
                        disabledTextColor = Color.Transparent,
                        backgroundColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(8.dp),
                    placeholder = {
                        Text(text = "위치 또는 태그 검색")
                    },
                    value = keyword.value,
                    singleLine = true,
                    onValueChange = {
                        searchBarViewModel.updateKeyword(it)
                    },
                    leadingIcon = {
                        Icon(
                            modifier = Modifier
                                .size(20.dp),
                            painter = painterResource(id = R.drawable.search_icon),
                            contentDescription = "",
                            tint = Color.Unspecified
                        )
                    }
                )
            }
            FilterChips()
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 105.dp)
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .background(Color.White, RoundedCornerShape(8.dp))
                    .padding()
                    .width(320.dp)
                    .size(250.dp)
                    .verticalScroll(ScrollState(0))
            ) {
                for (i in 0..2) {
                    SuggestionList()
                }
            }
        }
    }
}