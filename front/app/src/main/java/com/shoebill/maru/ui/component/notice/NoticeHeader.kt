package com.shoebill.maru.ui.component.notice

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.shoebill.maru.R

@Composable
fun NoticeHeader(
    navController: NavHostController
) {
    val image = painterResource(R.drawable.arrow_back)
    Box (
        modifier = Modifier.padding(horizontal = 15.dp, vertical = 15.dp).fillMaxWidth(),
    ) {

        Text(
            text = "알림",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )
        Image(
            painter = image,
            contentDescription = null,
            modifier = Modifier.clickable { navController.navigateUp() }
                .align(Alignment.CenterStart)
        )
    }

}