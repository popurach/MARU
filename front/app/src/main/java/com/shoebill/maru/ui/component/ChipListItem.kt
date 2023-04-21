package com.shoebill.maru.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.shoebill.maru.util.BrushUtil

@Composable
fun ChipListItem(
    text: String,
    isSelected: Boolean,
    onSelected: () -> Unit
) {
    val backgroundColor = if (isSelected) {
        BrushUtil.defaultBrush
    } else {
        BrushUtil.whiteBrush
    }
    Surface(
        elevation = 10.dp,
        shape = RoundedCornerShape(999.dp)
    ) {
        Box(
            modifier = Modifier
                .background(backgroundColor)
                .clickable(onClick = onSelected),
        ) {
            Text(
                text = text,
                modifier = Modifier
                    .padding(vertical = 5.dp, horizontal = 14.dp)
                    .align(Alignment.Center)
            )
        }
    }
}