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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shoebill.maru.ui.theme.Pretendard
import com.shoebill.maru.util.BrushUtil

@Composable
fun ChipListItem(
    text: String,
    isSelected: Boolean,
    onSelected: () -> Unit
) {
    Surface(
        elevation = 10.dp,
        shape = RoundedCornerShape(999.dp)
    ) {
        Box(
            modifier = Modifier
                .background(
                    if (isSelected) BrushUtil.defaultBrush else BrushUtil.whiteBrush
                )
                .clickable(onClick = onSelected),
        ) {
            Text(
                text = text,
                modifier = Modifier
                    .padding(vertical = 5.dp, horizontal = 14.dp)
                    .align(Alignment.Center),
                color = Color(if (!isSelected) android.graphics.Color.BLACK else android.graphics.Color.WHITE),
                fontSize = 15.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                fontFamily = Pretendard,
            )
        }
    }
}