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
import androidx.compose.ui.unit.sp

@Composable
fun Chip(
    text: String,
    textColor: Color = Color.Black,
    color: Color = Color.White,
    border: BorderStroke = BorderStroke(0.5.dp, Color(0XFFECECEC))
) {
    Surface(
        color = color,
        shape = RoundedCornerShape(16.dp),
        border = border,
        elevation = 2.dp,
        modifier = Modifier.padding(top = 7.dp)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 15.dp, vertical = 7.dp),
            fontSize = 12.sp,
            color = textColor
        )
    }

}