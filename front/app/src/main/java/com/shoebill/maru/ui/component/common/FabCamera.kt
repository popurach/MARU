package com.shoebill.maru.ui.component.common

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
import com.shoebill.maru.ui.theme.MaruBrush
import com.shoebill.maru.viewmodel.MapViewModel

@Composable
fun FabCamera(
    modifier: Modifier = Modifier,
    mapViewModel: MapViewModel = hiltViewModel()
) {
    IconButton(
        onClick = {
            mapViewModel.clearFocus()
            // TODO: 카메라 화면으로 이동
        },
        modifier = modifier
            .size(55.dp)
            .background(
                brush = MaruBrush,
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