package com.shoebill.maru.ui.feature.bottomsheet

import android.Manifest
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.shoebill.maru.ui.feature.common.FabCamera
import com.shoebill.maru.ui.main.NavigateViewModel
import com.shoebill.maru.util.checkAndRequestPermissions

@Composable
fun BottomSheetFrame(
    navigateViewModel: NavigateViewModel = hiltViewModel(),
    landmarkId: Long = -1,
    hasFabCamera: Boolean = false,
    backgroundColor: Color = Color.White,
    cameraEnabled: Boolean = false,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val launcherMultiplePermissions = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionsMap ->
        val areGranted = permissionsMap.values.reduce { acc, next -> acc && next }
        /** 권한 요청시 동의 했을 경우 **/
        /** 권한 요청시 동의 했을 경우 **/
        if (areGranted) {
            Toast.makeText(context, "권한이 동의되었습니다.", Toast.LENGTH_SHORT).show()
        }
        /** 권한 요청시 거부 했을 경우 **/
        /** 권한 요청시 거부 했을 경우 **/
        else {
            Toast.makeText(context, "권한이 거부되었습니다.", Toast.LENGTH_SHORT).show()
        }
    }
    Box(
        Modifier
            .background(backgroundColor)
            .fillMaxSize()
    ) {
        content()
        BottomSheetIndicator()
        if (hasFabCamera) {
            FabCamera(
                Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 20.dp),
                enabled = cameraEnabled,
                onClick = {
                    checkAndRequestPermissions(
                        context, permissions = arrayOf(
                            Manifest.permission.CAMERA,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ),
                        launcherMultiplePermissions
                    )
                    navigateViewModel.navigator!!.navigate("camera/$landmarkId")
                }
            )
        }
    }
}