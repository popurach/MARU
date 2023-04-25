package com.shoebill.maru.ui.component.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun Chip(
    text: String
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(0.5.dp, Color(0XFFECECEC)),
        elevation = 2.dp
    ) {
        Text(
            text = text, modifier = Modifier.padding(horizontal = 15.dp, vertical = 5.dp),
        )
    }

}