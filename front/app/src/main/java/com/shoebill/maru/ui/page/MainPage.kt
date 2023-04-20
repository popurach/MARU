package com.shoebill.maru.ui.page

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
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
import androidx.navigation.NavHostController
import com.shoebill.maru.R
import com.shoebill.maru.ui.component.MapboxScreen
import com.shoebill.maru.viewmodel.MapViewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainPage(
    viewModel: MapViewModel,
    navController: NavHostController
) {
    Scaffold(
        content = {
            MapboxScreen(viewModel)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    // TODO: 카메라 화면으로 이동
                },
                modifier = Modifier
                    .size(50.dp),
                shape = RoundedCornerShape(16.dp),
                backgroundColor = Color.White,
                content = {
                    Icon(
                        modifier = Modifier
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
        },
        floatingActionButtonPosition = FabPosition.Center
    )
    Button(onClick = {
        navController.navigate("notice")
    }) {
        Text(text = "알림창 가즈아")
    }
}