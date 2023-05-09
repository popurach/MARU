package com.shoebill.maru.ui.component.drawer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.shoebill.maru.viewmodel.MemberViewModel
import com.shoebill.maru.viewmodel.NavigateViewModel

@Composable
fun DrawerBody(
    memberViewModel: MemberViewModel = hiltViewModel(),
    navigateViewModel: NavigateViewModel = viewModel(),
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(top = 25.dp, start = 30.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(text = "고객센터", color = Color.Gray)
        Text(text = "로그아웃", color = Color.Gray, modifier = Modifier.clickable {
            memberViewModel.logout()
            navigateViewModel.navigator?.navigate("login")
        })
    }
}