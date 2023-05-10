package com.shoebill.maru.ui.component.searchbar

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.shoebill.maru.ui.component.SearchListItem
import com.shoebill.maru.viewmodel.SearchBarViewModel

@Composable
fun SuggestionList(
    searchBarViewModel: SearchBarViewModel = hiltViewModel()
) {
    Column {
        val searchItems = searchBarViewModel.prefUtil.loadSearchHistory()
        for (keyword in searchItems) {
            SearchListItem(keyword)
        }
    }
}