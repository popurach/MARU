package com.shoebill.maru.ui.feature.camera

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.media.MediaScannerConnection
import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.core.net.toFile
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.google.android.gms.location.LocationServices
import com.shoebill.maru.R
import com.shoebill.maru.model.data.Tag
import com.shoebill.maru.model.repository.SpotRepository
import com.shoebill.maru.util.apiCallback
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.Executors
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(private val spotRepository: SpotRepository) :
    ViewModel() {
    private val FILENAME = "yyyy-MM-dd-HH-mm-ss-SSS"
    private val PHOTO_EXTENSION = ".jpg"
    private val _imageUrl = MutableLiveData("")
    private val _location = MutableLiveData<Location?>()
    private var capturedFile: File? = null

    private var savedSpotId: Long = -1

    val location: LiveData<Location?> get() = _location
    fun setLocation(location: Location) {
        _location.value = location
    }

    val imageUrl get() = _imageUrl

    private val _isCapture = MutableLiveData(false)
    val isCapture get() = _isCapture

    private val _inputTag = MutableLiveData("")
    val inputTag get() = _inputTag

    private val listOfTag: MutableList<Tag> = mutableListOf()
    private val _tagList = MutableLiveData<List<Tag>>(listOf())
    val tagList get() = _tagList

    fun updateInputTag(value: String) {
        _inputTag.value = value
    }

    fun addTag() {
        if (_inputTag.value.isNullOrEmpty()) return
        listOfTag.add(
            Tag(name = _inputTag.value!!)
        )
        _tagList.value = listOfTag
        _inputTag.value = ""
    }

    fun clearCameraViewModel(value: Boolean) {
        _isCapture.value = value
        _imageUrl.value = null
        _inputTag.value = ""
        _location.value = null
        listOfTag.clear()
        _tagList.value = listOfTag
    }

    @SuppressLint("MissingPermission")
    fun takePhoto(
        context: Context,
        imageCapture: ImageCapture,
        scope: CoroutineScope,
        lensFacing: Int,
        onImageCaptured: (Uri, Boolean) -> Unit,
        onError: (ImageCaptureException) -> Unit,
    ) {

        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location.also {
                setLocation(location)
                imageCapture.takePicture(context, scope, lensFacing, onImageCaptured, onError)
            }
        }
    }

    private fun ImageCapture.takePicture(
        context: Context,
        scope: CoroutineScope,
        lensFacing: Int,
        onImageCaptured: (Uri, Boolean) -> Unit,
        onError: (ImageCaptureException) -> Unit,
    ) {
        val outputDirectory = context.getOutputDirectory()
        // Create output file to hold the image
        val photoFile = createFile(outputDirectory)
        val outputFileOptions = getOutputFileOptions(lensFacing, photoFile)
        capturedFile = photoFile

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
        photoFile: File,
    ): ImageCapture.OutputFileOptions {
        // Setup image capture metadata
        val metadata = ImageCapture.Metadata().apply {
            // Mirror image when using the front camera
            isReversedHorizontal = lensFacing == CameraSelector.LENS_FACING_FRONT
            _location.value?.let {
                this.location = it
            }
        }
        // Create output options object which contains file + metadata

        return ImageCapture.OutputFileOptions.Builder(photoFile)
            .setMetadata(metadata)
            .build()
    }

    private fun createFile(baseFolder: File) =
        File(
            baseFolder, SimpleDateFormat(FILENAME, Locale.KOREA)
                .format(System.currentTimeMillis()) + PHOTO_EXTENSION
        )


    private fun Context.getOutputDirectory(): File {
        val mediaDir = this.externalMediaDirs.firstOrNull()?.let {
            File(it, this.resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else this.filesDir
    }

    suspend fun saveSpot(navController: NavHostController, landmarkId: Long?): Long? {
        val spotId = apiCallback(navController) {
            spotRepository.saveSpot(
                spotImage = capturedFile!!,
                tags = _tagList.value,
                landmarkId = landmarkId
            )
        }
        if (spotId != null) savedSpotId = spotId
        return spotId
    }

    fun moveSpotDetail(navController: NavHostController) {
        clearCameraViewModel(false)
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                navController.navigate("main/$savedSpotId") {
                    popUpTo(navController.graph.id) {
                        inclusive = true
                    }
                }
            }
        }
    }

    fun moveAuctionPage(navController: NavHostController, landmarkId: Long) {
        clearCameraViewModel(false)
        navController.navigate("auction/$landmarkId") {
            popUpTo("main/-1")
        }
    }
}

