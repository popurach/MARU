package com.shoebill.maru.ui.component.common

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.shoebill.maru.ui.theme.MaruBrush

@OptIn(ExperimentalTextApi::class)
@Composable
fun GradientColoredText(
    text: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 12.sp,
    fontWeight: FontWeight = FontWeight.Normal
) {
    val coloredText = buildAnnotatedString {
        withStyle(
            SpanStyle(
                MaruBrush,
                fontSize = fontSize,
                fontWeight = fontWeight,
            )
        ) {
            append(text)
        }
    }
    Text(
        text = coloredText,
        modifier = modifier,
    )
}