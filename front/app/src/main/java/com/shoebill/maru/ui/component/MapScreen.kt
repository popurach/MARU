package com.shoebill.maru.ui.component

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.mapbox.maps.ResourceOptionsManager
import com.shoebill.maru.R
import com.shoebill.maru.viewmodel.MapViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
fun MapboxScreen(
    viewModel: MapViewModel
) {
    val context = LocalContext.current

    /** 요청할 권한 **/
    val permissions = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
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
                    .fillMaxHeight(),
                factory = { context ->
                    ResourceOptionsManager.getDefault(
                        context,
                        context.getString(R.string.mapbox_public_token)
                    )
                    viewModel.createMapView(context)
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    checkAndRequestPermissions(context, permissions, launcherMultiplePermissions)
                    viewModel.trackCameraToUser(context)
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
                                        viewModel.myLocationColor,
                                        blendMode = BlendMode.SrcAtop
                                    )
                                }
                            },
                        painter = painterResource(id = R.drawable.my_location),
                        contentDescription = ""
                    )
                }
            )
        }
    )
}

fun checkAndRequestPermissions(
    context: Context,
    permissions: Array<String>,
    launcher: ManagedActivityResultLauncher<Array<String>, Map<String, @JvmSuppressWildcards Boolean>>
) {
    /** 권한이 없는 경우 **/
    if (!permissions.all {
            ContextCompat.checkSelfPermission(
                context,
                it
            ) == PackageManager.PERMISSION_GRANTED
        }) {
        launcher.launch(permissions)
    }
}