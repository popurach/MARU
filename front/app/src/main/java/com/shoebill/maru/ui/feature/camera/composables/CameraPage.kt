package com.shoebill.maru.ui.page

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Size
import androidx.activity.compose.BackHandler
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY
import androidx.camera.core.ImageCaptureException
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.shoebill.maru.ui.feature.camera.CameraCapturedImage
import com.shoebill.maru.ui.feature.camera.CameraPreview
import com.shoebill.maru.ui.feature.camera.CameraViewModel
import com.shoebill.maru.ui.main.NavigateViewModel

@SuppressLint("MissingPermission")
@Composable
fun CameraPage(
    landmarkId: Long,
    cameraViewModel: CameraViewModel = hiltViewModel(),
    onImageCaptured: (Uri, Boolean) -> Unit,
    onError: (ImageCaptureException) -> Unit,
    navigateViewModel: NavigateViewModel = viewModel()
) {
    val isCapture = cameraViewModel.isCapture.observeAsState()
    BackHandler {
        if (isCapture.value == true) {
            cameraViewModel.clearCameraViewModel(false)
        } else {
            navigateViewModel.navigator?.popBackStack()
        }
    }

    if (isCapture.value == false) {
        Box {
            val context = LocalContext.current
            val scope = rememberCoroutineScope()
            val lensFacing by remember { mutableStateOf(CameraSelector.LENS_FACING_BACK) }
            val imageCapture: ImageCapture = remember {
                ImageCapture.Builder()
                    .setCaptureMode(CAPTURE_MODE_MINIMIZE_LATENCY)
                    .setTargetResolution(Size(720, 1600))
                    .build()
            }
            CameraPreview(
                imageCapture,
                lensFacing
            ) {
                cameraViewModel.takePhoto(
                    context,
                    imageCapture,
                    scope,
                    lensFacing,
                    onImageCaptured,
                    onError
                )
            }
        }
    } else {
        CameraCapturedImage(landmarkId = landmarkId)
    }

}



