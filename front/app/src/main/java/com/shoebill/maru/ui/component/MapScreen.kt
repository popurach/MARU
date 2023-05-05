package com.shoebill.maru.ui.component

import android.Manifest
import android.annotation.SuppressLint
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.mapbox.maps.ResourceOptionsManager
import com.shoebill.maru.R
import com.shoebill.maru.util.checkAndRequestPermissions
import com.shoebill.maru.viewmodel.MapViewModel
import com.shoebill.maru.viewmodel.MemberViewModel

@Composable
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
fun MapboxScreen(
    mapViewModel: MapViewModel = hiltViewModel(),
    memberViewModel: MemberViewModel = hiltViewModel()
) {
    mapViewModel.initFocusManager(LocalFocusManager.current)
    mapViewModel.initMarkerImage(
        AppCompatResources.getDrawable(
            LocalContext.current,
            R.drawable.landmark
        )
    )
    val context = LocalContext.current

    val memberInfo = memberViewModel.memberInfo.observeAsState()

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

    Scaffold(
        content = { _ ->
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
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    checkAndRequestPermissions(context, permissions, launcherMultiplePermissions)
                    mapViewModel.trackCameraToUser(context, memberInfo.value!!)
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
                                        mapViewModel.myLocationColor,
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