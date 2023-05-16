package com.shoebill.maru.ui.page

import android.Manifest
import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.shoebill.maru.ui.component.MapboxScreen
import com.shoebill.maru.ui.component.common.FabCamera
import com.shoebill.maru.ui.component.searchbar.SearchBar
import com.shoebill.maru.util.checkAndRequestPermissions
import com.shoebill.maru.viewmodel.CameraViewModel
import com.shoebill.maru.viewmodel.DrawerViewModel
import com.shoebill.maru.viewmodel.MapViewModel
import com.shoebill.maru.viewmodel.MemberViewModel
import com.shoebill.maru.viewmodel.NavigateViewModel

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
@Composable
fun MainPage(
    spotId: Long,
    mapViewModel: MapViewModel = viewModel(),
    drawerViewModel: DrawerViewModel = viewModel(),
    memberViewModel: MemberViewModel = hiltViewModel(),
    navigateViewModel: NavigateViewModel = hiltViewModel(),
    cameraViewModel: CameraViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val scaffoldState = rememberBottomSheetScaffoldState()
    val isDrawerOpen = drawerViewModel.isOpen.observeAsState(initial = false)
    val isBottomSheetOpen = mapViewModel.bottomSheetOpen.observeAsState(initial = false)

    val isTracking = mapViewModel.isTracking.observeAsState()

    val launcherMultiplePermissions = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionsMap ->
        val areGranted = permissionsMap.values.reduce { acc, next -> acc && next }
        /** 권한 요청시 동의 했을 경우 **/
        if (areGranted) {
            Toast.makeText(context, "권한이 동의되었습니다.", Toast.LENGTH_SHORT).show()
        }
        /** 권한 요청시 거부 했을 경우 **/
        else {
            Toast.makeText(context, "권한이 거부되었습니다.", Toast.LENGTH_SHORT).show()
        }
    }
    LaunchedEffect(Unit) {
        Log.d("LaunchedEffect", "executed $spotId")
        memberViewModel.getMemberInfo(navigateViewModel)
        if (cameraViewModel.imageUrl.value != null) {
            cameraViewModel.clearCameraViewModel(false)
        }
    }
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
    LaunchedEffect(key1 = isBottomSheetOpen.value) {
        if (isBottomSheetOpen.value == true) {
            scaffoldState.bottomSheetState.expand()
        }
    }
    LaunchedEffect(key1 = scaffoldState.bottomSheetState.isExpanded) {
        if (!scaffoldState.bottomSheetState.isExpanded) {
            mapViewModel.updateBottomSheetState(false)
        } else {
            mapViewModel.updateBottomSheetState(true)
        }
    }
    LaunchedEffect(key1 = mapViewModel.bottomSheetOpen.value) {
        if (mapViewModel.bottomSheetOpen.value == true) {
            scaffoldState.bottomSheetState.expand()
        } else {
            scaffoldState.bottomSheetState.collapse()
        }
    }
    DisposableEffect(Unit) {
        onDispose {

        }
    }
    BackHandler(isDrawerOpen.value) {
        drawerViewModel.updateOpenState(false)
    }
    BackHandler(isBottomSheetOpen.value == true) {
        mapViewModel.updateBottomSheetState(false)
    }
    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        content = {
            Box {
                MapboxScreen(spotId == -1L)
                SearchBar()
                FabCamera(
                    Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 40.dp),
                    enabled = isTracking.value!!,
                    onClick = {
                        checkAndRequestPermissions(
                            context,
                            arrayOf(
                                Manifest.permission.CAMERA,
                                Manifest.permission.ACCESS_FINE_LOCATION
                            ),
                            launcherMultiplePermissions
                        )
                        navigateViewModel.navigator!!.navigate("camera/-1")
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
            BottomSheetPage(
                spotId = spotId,
                startDestination = if (spotId == -1L) "spot/list" else "spot/detail/{id}"
            )
        },
        sheetPeekHeight = 25.dp,
        floatingActionButton = null
    )
}

fun customShape() = object : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density,
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