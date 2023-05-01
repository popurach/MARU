package com.shoebill.maru.ui.page

import android.net.Uri
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.shoebill.maru.ui.component.camera.CameraCapturedImage
import com.shoebill.maru.ui.component.camera.CameraPreview
import com.shoebill.maru.viewmodel.CameraViewModel

@Composable
fun CameraPage(
    cameraViewModel: CameraViewModel = hiltViewModel(),
    onImageCaptured: (Uri, Boolean) -> Unit,
    onError: (ImageCaptureException) -> Unit
) {
    val isCapture = cameraViewModel.isCapture.observeAsState()
    if (isCapture.value == false) {
        Box {
            val context = LocalContext.current
            val scope = rememberCoroutineScope()
            var lensFacing by remember { mutableStateOf(CameraSelector.LENS_FACING_BACK) }
            val imageCapture: ImageCapture = remember {
                ImageCapture.Builder().build()
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
        CameraCapturedImage()
    }

}



