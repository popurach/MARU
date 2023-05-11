package com.shoebill.maru.ui.component.camera

import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetState
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.core.graphics.drawable.toBitmap
import androidx.hilt.navigation.compose.hiltViewModel
import com.shoebill.maru.viewmodel.CameraViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CameraCapturedImage(
    landmarkId: Long,
    cameraViewModel: CameraViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val bitmap = cameraViewModel.imageUrl.value.let { uri ->
        context.contentResolver.openInputStream(Uri.parse(uri))?.use { stream ->
            stream.use {
                val drawable = Drawable.createFromStream(it, it.markSupported().toString())
                drawable?.toBitmap()
            }
        }
    }?.asImageBitmap()

    if (bitmap != null) {
        val bottomSheetState =
            rememberBottomSheetScaffoldState(bottomSheetState = BottomSheetState(BottomSheetValue.Expanded))
        BottomSheetScaffold(
            scaffoldState = bottomSheetState,
            sheetContent = {
                ImageUploadForm(bitmap, landmarkId)
            },
            sheetGesturesEnabled = false,
        ) {
            Image(bitmap, null, Modifier.fillMaxHeight(), contentScale = ContentScale.Crop)
        }
    }
}