package com.shoebill.maru.ui.component

import android.Manifest
import android.annotation.SuppressLint
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.mapbox.maps.ResourceOptionsManager
import com.shoebill.maru.R
import com.shoebill.maru.ui.component.common.GradientColoredText
import com.shoebill.maru.ui.theme.GreyBrush
import com.shoebill.maru.ui.theme.MaruBrush
import com.shoebill.maru.util.checkAndRequestPermissions
import com.shoebill.maru.viewmodel.MapViewModel

@Composable
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
fun MapboxScreen(
    mapViewModel: MapViewModel = hiltViewModel(),
) {
    mapViewModel.initFocusManager(LocalFocusManager.current)
    val context = LocalContext.current
    mapViewModel.initImages(context)

    /** 요청할 권한 **/
    val permissions = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

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

    val isTracking = mapViewModel.isTracking.observeAsState()
    val canSearch = mapViewModel.canSearch.observeAsState()

    Scaffold(
        content = { _ ->
            Box(modifier = Modifier.fillMaxSize()) {
                AndroidView(
                    modifier = Modifier
                        .padding(bottom = 25.dp)
                        .fillMaxHeight(),
                    factory = { context ->
                        ResourceOptionsManager.getDefault(
                            context,
                            context.getString(R.string.mapbox_public_token)
                        )
                        mapViewModel.createMapView(context)
                    }
                )
                if (canSearch.value != null && canSearch.value!!) {
                    Surface(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(top = 160.dp)
                            .clip(RoundedCornerShape(100.dp))
                            .clickable {
                                mapViewModel.loadMarker()
                            },
                        elevation = 30.dp
                    ) {
                        GradientColoredText(
                            text = "현 지도에서 검색",
                            fontSize = 15.sp,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    checkAndRequestPermissions(context, permissions, launcherMultiplePermissions)
                    mapViewModel.trackCameraToUser(context)
                },
                modifier = Modifier
                    .padding(bottom = 25.dp)
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
                                        brush = if (isTracking.value == true) MaruBrush else GreyBrush,
                                        blendMode = BlendMode.SrcAtop
                                    )
                                }
                            },
                        painter = painterResource(id = R.drawable.my_location),
                        contentDescription = ""
                    )
                }
            )
            Box(modifier = Modifier.height(50.dp))
        },
        floatingActionButtonPosition = FabPosition.End,
        isFloatingActionButtonDocked = true
    )
}