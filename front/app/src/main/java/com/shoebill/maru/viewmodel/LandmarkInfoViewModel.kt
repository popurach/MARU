package com.shoebill.maru.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.shoebill.maru.model.data.landmark.Owner
import com.shoebill.maru.model.repository.LandmarkRepository
import com.shoebill.maru.util.apiCallback
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LandmarkInfoViewModel @Inject constructor(
    private val landmarkRepository: LandmarkRepository
) : ViewModel() {
    private val _owner = MutableLiveData(Owner())
    val owner get() = _owner

    private val _landmarkName = MutableLiveData("")
    val landmarkName get() = _landmarkName

    var landmarkId: Long? = null

    fun initLandmarkInfo(landmarkId: Long, navController: NavHostController) {
        Log.d(TAG, "initLandmarkInfo: $landmarkId")
        this.landmarkId = landmarkId
        loadLandmarkName(landmarkId, navController)
        loadOwnerInfo(landmarkId, navController)
    }

    private fun loadOwnerInfo(landmarkId: Long, navController: NavHostController) {
        viewModelScope.launch {
            _owner.value = withContext(Dispatchers.IO) {
                apiCallback(navController) {
                    landmarkRepository.getLandmarkOwner(landmarkId)
                }
            }
        }
    }

    fun loadLandmarkName(landmarkId: Long, navController: NavHostController) {
        viewModelScope.launch {
            _landmarkName.value = withContext(Dispatchers.IO) {
                apiCallback(navController) {
                    landmarkRepository.getLandmarkName(landmarkId)
                }?.name
            }
        }
    }

    suspend fun visitLandmark(context: Context) {
        val point = withContext(Dispatchers.IO) {
            Log.d(TAG, "landmarkId: $landmarkId")
            landmarkRepository.visitLandmark(landmarkId!!)
        }
        withContext(Dispatchers.Main) {
            Toast.makeText(context, "$point 포인트 획득!", Toast.LENGTH_SHORT).show()
        }
    }
}