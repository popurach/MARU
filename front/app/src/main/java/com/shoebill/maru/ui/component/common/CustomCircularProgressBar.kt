package com.shoebill.maru.ui.component.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CustomCircularProgressBar(
    text: String
) {
    CustomAlertDialog(onDismissRequest = { }) {
        Column {
            CircularProgressIndicator(
                modifier = Modifier.size(100.dp),
                strokeWidth = 10.dp
            )
            Text(text = text)
        }
    }
}