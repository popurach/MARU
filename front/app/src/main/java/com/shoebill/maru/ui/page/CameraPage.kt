package com.shoebill.maru.ui.page

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.activity.compose.BackHandler
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
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.location.LocationServices
import com.shoebill.maru.ui.component.camera.CameraCapturedImage
import com.shoebill.maru.ui.component.camera.CameraPreview
import com.shoebill.maru.viewmodel.CameraViewModel
import com.shoebill.maru.viewmodel.NavigateViewModel

@SuppressLint("MissingPermission")
@Composable
fun CameraPage(
    cameraViewModel: CameraViewModel = hiltViewModel(),
    onImageCaptured: (Uri, Boolean) -> Unit,
    onError: (ImageCaptureException) -> Unit,
    navigateViewModel: NavigateViewModel = viewModel()
) {
    val isCapture = cameraViewModel.isCapture.observeAsState()
    BackHandler {
        if (isCapture.value == true) {
            cameraViewModel.backToCameraScreen(false)
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
        // gps 켜고
        val context = LocalContext.current
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location.also {
                Log.d("TEST", "CameraPage: ${cameraViewModel.location.value}")
                cameraViewModel.setLocation(location)
                Log.d("TEST", "CameraPage: ${cameraViewModel.location.value}")
            }
        }

        CameraCapturedImage()
    }

}



