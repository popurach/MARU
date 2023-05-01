package com.shoebill.maru.viewmodel

import android.content.Context
import android.media.MediaScannerConnection
import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.core.net.toFile
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shoebill.maru.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.Executors

class CameraViewModel() : ViewModel() {
    private val FILENAME = "yyyy-MM-dd-HH-mm-ss-SSS"
    private val PHOTO_EXTENSION = ".png"

    private val _imageUrl = MutableLiveData("")
    private val _isCapture = MutableLiveData(false)
    val imageUrl get() = _imageUrl
    val isCapture get() = _isCapture

    fun updateImageUrl(value: String) {
        _imageUrl.value = value
        _isCapture.value = true
    }

    fun updateIsCapture(value: Boolean) {
        _isCapture.value = value
    }

    fun takePhoto(
        context: Context,
        imageCapture: ImageCapture,
        scope: CoroutineScope,
        lensFacing: Int,
        onImageCaptured: (Uri, Boolean) -> Unit,
        onError: (ImageCaptureException) -> Unit
    ) {
        imageCapture.takePicture(context, scope, lensFacing, onImageCaptured, onError)
    }

    private fun ImageCapture.takePicture(
        context: Context,
        scope: CoroutineScope,
        lensFacing: Int,
        onImageCaptured: (Uri, Boolean) -> Unit,
        onError: (ImageCaptureException) -> Unit
    ) {
        val outputDirectory = context.getOutputDirectory()
        // Create output file to hold the image
        val photoFile = createFile(outputDirectory, FILENAME, PHOTO_EXTENSION)
        val outputFileOptions = getOutputFileOptions(lensFacing, photoFile)

        this.takePicture(
            outputFileOptions,
            Executors.newSingleThreadExecutor(),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val savedUri = output.savedUri ?: Uri.fromFile(photoFile)
                    // If the folder selected is an external media directory, this is
                    // unnecessary but otherwise other apps will not be able to access our
                    // images unless we scan them using [MediaScannerConnection]
                    val mimeType = MimeTypeMap.getSingleton()
                        .getMimeTypeFromExtension(savedUri.toFile().extension)
                    MediaScannerConnection.scanFile(
                        context,
                        arrayOf(savedUri.toFile().absolutePath),
                        arrayOf(mimeType)
                    ) { _, uri ->

                    }
                    scope.launch {
                        _imageUrl.value = savedUri.toString()
                        _isCapture.value = true
                    }
                    onImageCaptured(savedUri, false)
                }

                override fun onError(exception: ImageCaptureException) {
                    onError(exception)
                }
            })
    }

    private fun getOutputFileOptions(
        lensFacing: Int,
        photoFile: File
    ): ImageCapture.OutputFileOptions {
        // Setup image capture metadata
        val metadata = ImageCapture.Metadata().apply {
            // Mirror image when using the front camera
            isReversedHorizontal = lensFacing == CameraSelector.LENS_FACING_FRONT
        }
        // Create output options object which contains file + metadata

        return ImageCapture.OutputFileOptions.Builder(photoFile)
            .setMetadata(metadata)
            .build()
    }

    private fun createFile(baseFolder: File, format: String, extension: String) =
        File(
            baseFolder, SimpleDateFormat(format, Locale.KOREA)
                .format(System.currentTimeMillis()) + extension
        )


    private fun Context.getOutputDirectory(): File {
        val mediaDir = this.externalMediaDirs.firstOrNull()?.let {
            File(it, this.resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else this.filesDir
    }
}

