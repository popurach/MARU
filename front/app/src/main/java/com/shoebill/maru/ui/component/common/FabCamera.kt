package com.shoebill.maru.ui.component.common

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.shoebill.maru.R
import com.shoebill.maru.viewmodel.MapViewModel

@Composable
fun FabCamera(
    modifier: Modifier = Modifier,
    mapViewModel: MapViewModel = hiltViewModel()
) {
    FloatingActionButton(
        onClick = {
            mapViewModel.clearFocus()
            // TODO: 카메라 화면으로 이동
        },
        modifier = modifier
            .size(60.dp),
        shape = RoundedCornerShape(16.dp),
        backgroundColor = Color.White,
        content = {
            Icon(
                modifier = Modifier
                    .size(40.dp)
                    .graphicsLayer(alpha = 0.99f)
                    .drawWithCache {
                        onDrawWithContent {
                            drawContent()
                            drawRect(
                                Brush.linearGradient(
                                    listOf(
                                        Color(0xFF6039DF),
                                        Color(0xFFA14AB7)
                                    )
                                ),
                                blendMode = BlendMode.SrcAtop
                            )
                        }
                    },
                painter = painterResource(id = R.drawable.camera),
                contentDescription = ""
            )
        }
    )
}