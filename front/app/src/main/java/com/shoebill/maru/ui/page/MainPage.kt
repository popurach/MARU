package com.shoebill.maru.ui.page

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.shoebill.maru.R
import com.shoebill.maru.ui.component.MapboxScreen
import com.shoebill.maru.ui.component.searchbar.SearchBar
import com.shoebill.maru.viewmodel.DrawerViewModel
import com.shoebill.maru.viewmodel.MapViewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainPage(
    mapViewModel: MapViewModel = viewModel(),
    drawerViewModel: DrawerViewModel = viewModel(),
    navController: NavHostController = rememberNavController(),
) {
    val scaffoldState = rememberScaffoldState()
    val isDrawerOpen = drawerViewModel.isOpen.observeAsState(initial = false)
    LaunchedEffect(isDrawerOpen.value) {
        if (isDrawerOpen.value) {
            scaffoldState.drawerState.open()
        } else {
            scaffoldState.drawerState.close()
        }
    }
    LaunchedEffect(key1 = scaffoldState.drawerState.isOpen) {
        if (scaffoldState.drawerState.isClosed) {
            drawerViewModel.updateOpenState(false)
        }
    }
    BackHandler(isDrawerOpen.value) {
        drawerViewModel.updateOpenState(false)
    }
    Scaffold(
        scaffoldState = scaffoldState,
        content = {
            MapboxScreen(mapViewModel)
            SearchBar()
        },
        drawerShape = customShape(),
        drawerContent = {
            DrawerMenuPage()
        },
        drawerGesturesEnabled = scaffoldState.drawerState.isOpen,
        floatingActionButton = {

            Button(onClick = { navController.navigate("login") }) {
                Text(text = "로그인 페이지 ㄱㄱ")
            }
            FloatingActionButton(
                onClick = {
                    mapViewModel.clearFocus()
                    // TODO: 카메라 화면으로 이동
                },
                modifier = Modifier
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
        },
        floatingActionButtonPosition = FabPosition.Center
    )
}

fun customShape() = object : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Rectangle(
            Rect(
                0f,
                0f,
                size.width * 3 / 4 /* width */,
                size.height /* height */
            )
        )
    }
}