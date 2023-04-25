package com.shoebill.maru.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shoebill.maru.R

@Composable
@Preview
fun SearchListItem(
//    text: String
) {
    Box(
        modifier = Modifier
            .width(320.dp)
            .height(50.dp)
            .background(color = Color.White, shape = RoundedCornerShape(8.dp))
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.padding(top = 15.dp, bottom = 15.dp, start = 6.dp, end = 20.dp),
                painter = painterResource(id = R.drawable.search_icon),
                contentDescription = "",
                tint = Color.Unspecified
            )
            Text(text = "강남")
        }
    }
}