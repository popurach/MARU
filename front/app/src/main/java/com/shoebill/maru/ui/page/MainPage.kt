package com.shoebill.maru.ui.page

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
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
import com.shoebill.maru.R
import com.shoebill.maru.ui.component.MapboxScreen
import com.shoebill.maru.ui.component.searchbar.SearchBar
import com.shoebill.maru.viewmodel.DrawerViewModel
import com.shoebill.maru.viewmodel.MapViewModel

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainPage(
    mapViewModel: MapViewModel = viewModel(),
    drawerViewModel: DrawerViewModel = viewModel()
) {
    val scaffoldState = rememberBottomSheetScaffoldState()
//    val scaffoldState = rememberScaffoldState()
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
    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        content = {
            Box {
                MapboxScreen(mapViewModel)
                SearchBar()
                FloatingActionButton(
                    onClick = {
                        mapViewModel.clearFocus()
                        // TODO: 카메라 화면으로 이동
                    },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 40.dp)
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
        },
        drawerShape = customShape(),
        drawerContent = {
            DrawerMenuPage()
        },
        drawerGesturesEnabled = scaffoldState.drawerState.isOpen,
        sheetShape = RoundedCornerShape(topEnd = 16.dp, topStart = 16.dp),
        sheetContent = {
            BottomSheetPage()
        },
        sheetPeekHeight = 25.dp,
        floatingActionButton = null
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