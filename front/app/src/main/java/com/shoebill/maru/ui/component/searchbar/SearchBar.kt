package com.shoebill.maru.ui.component.searchbar

import android.util.Log
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.shoebill.maru.R
import com.shoebill.maru.ui.component.filter.FilterChips
import com.shoebill.maru.ui.theme.MaruBrush
import com.shoebill.maru.viewmodel.DrawerViewModel
import com.shoebill.maru.viewmodel.MapViewModel
import com.shoebill.maru.viewmodel.SearchBarViewModel

@Composable
fun SearchBar(
    mapViewModel: MapViewModel = viewModel(),
    drawerViewModel: DrawerViewModel = viewModel(),
    searchBarViewModel: SearchBarViewModel = hiltViewModel(),
) {
    val keyword = searchBarViewModel.keyword.observeAsState("")
    var isFocused by remember { mutableStateOf(true) }
    val focusManager = LocalFocusManager.current

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
                        .width(320.dp)
                        .onFocusChanged {
                            isFocused = !isFocused
                            Log.d("SEARCHBAR", "SearchBar: Focused $isFocused")
                        },
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
                        if (it.length >= 2) {
                            searchBarViewModel.getRecommendPlacesByKeyword(it)
                        }
                    },
                    keyboardActions = KeyboardActions(
                        onDone = {
                            searchBarViewModel.prefUtil.saveSearchHistory(keyword.value)
                            searchBarViewModel.updateKeyword("")
                            focusManager.clearFocus()
                        }
                    ),
                    leadingIcon = {
                        Icon(
                            modifier = Modifier
                                .size(30.dp)
                                .graphicsLayer(alpha = 0.99f)
                                .clickable {
                                    drawerViewModel.updateOpenState(true)
                                    mapViewModel.clearFocus()
                                }
                                .drawWithCache {
                                    onDrawWithContent {
                                        drawContent()
                                        drawRect(
                                            MaruBrush,
                                            blendMode = BlendMode.SrcAtop
                                        )
                                    }
                                },
                            painter = painterResource(id = R.drawable.menu_icon),
                            contentDescription = "",

                            )
                    }
                )
            }
            FilterChips()
        }

        if (isFocused)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 105.dp)
                    .offset(y = (-11).dp)
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
                    SuggestionList()
                }
            }
    }
}