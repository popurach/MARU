package com.shoebill.maru.ui.page

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.shoebill.maru.R
import com.shoebill.maru.ui.component.bottomsheet.SpotList

@Composable
fun BottomSheetPage() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .heightIn(min = 40.dp, max = 670.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.stick_bar),
            contentDescription = "stick bar",
            modifier = Modifier.padding(vertical = 10.dp),
            tint = Color.Gray
        )
        Divider()
        SpotList()
    }
}