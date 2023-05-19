package com.shoebill.maru.ui.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.shoebill.maru.R
import com.shoebill.maru.ui.component.mypage.MyPageMemberInfo
import com.shoebill.maru.ui.component.mypage.MyPageTabs
import com.shoebill.maru.viewmodel.NavigateViewModel

@Composable
fun MyPage(
    navigateViewModel: NavigateViewModel = viewModel(),
) {
    Box(
        Modifier
            .fillMaxWidth()
            .background(color = Color.White),
    ) {
        // 뒤로 가기 버튼
        Image(
            painter = painterResource(R.drawable.arrow_back),
            contentDescription = null,
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 60.dp)
                .size(30.dp)
                .clickable { navigateViewModel.navigator?.navigateUp() }
        )

        // component 구성
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            MyPageMemberInfo()
            MyPageTabs()
        }
    }
}