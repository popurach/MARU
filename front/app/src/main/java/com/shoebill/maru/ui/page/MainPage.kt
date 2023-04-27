package com.shoebill.maru.ui.page

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.shoebill.maru.ui.component.MapboxScreen
import com.shoebill.maru.ui.component.common.FabCamera
import com.shoebill.maru.ui.component.searchbar.SearchBar
import com.shoebill.maru.viewmodel.DrawerViewModel
import com.shoebill.maru.viewmodel.MapViewModel

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainPage(
    mapViewModel: MapViewModel = viewModel(),
    drawerViewModel: DrawerViewModel = viewModel(),
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
                FabCamera(Modifier.align(Alignment.BottomCenter))
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