package com.shoebill.maru.ui.feature.bottomsheet.landmark

import android.content.Context
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
            } ?: Owner()
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

    suspend fun visitLandmark(context: Context, navController: NavHostController) {
        val point = withContext(Dispatchers.IO) {
            apiCallback(navController) {
                landmarkRepository.visitLandmark(landmarkId!!)
            }
        }
        withContext(Dispatchers.Main) {
            Toast.makeText(context, "$point 포인트 획득!", Toast.LENGTH_SHORT).show()
        }
    }
}