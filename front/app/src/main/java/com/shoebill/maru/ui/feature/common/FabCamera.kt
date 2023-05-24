package com.shoebill.maru.ui.feature.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.shoebill.maru.R
import com.shoebill.maru.ui.feature.map.MapViewModel
import com.shoebill.maru.ui.theme.GreyBrush
import com.shoebill.maru.ui.theme.MaruBrush

@Composable
fun FabCamera(
    modifier: Modifier = Modifier,
    mapViewModel: MapViewModel = hiltViewModel(),
    enabled: Boolean,
    onClick: () -> Unit = {}
) {
    IconButton(
        enabled = enabled,
        onClick = {
            mapViewModel.clearFocus()
            onClick()
        },
        modifier = modifier
            .size(55.dp)
            .background(
                brush = if (enabled) MaruBrush else GreyBrush,
                shape = RoundedCornerShape(16.dp)
            ),
        content = {
            Icon(
                modifier = Modifier
                    .size(35.dp),
                painter = painterResource(id = R.drawable.camera),
                contentDescription = "",
                tint = Color.White
            )
        }
    )
}