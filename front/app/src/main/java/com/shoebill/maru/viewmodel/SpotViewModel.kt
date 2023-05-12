package com.shoebill.maru.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.shoebill.maru.model.data.Spot
import com.shoebill.maru.model.repository.SpotRepository
import com.shoebill.maru.util.apiCallback
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SpotViewModel @Inject constructor(
    private val spotRepository: SpotRepository
) : ViewModel() {
    private var spotId: Long = -1

    private val _spotDetails = MutableLiveData(Spot())
    val spotDetails: LiveData<Spot> get() = _spotDetails

    fun initSpotId(value: Long) {
        spotId = value
    }

    fun loadSpotDetailById(spotId: Long, navController: NavHostController) {
        Log.d(TAG, "loadSpotDetailById: isLoad")
        viewModelScope.launch {
            val mySpot = apiCallback(navController) {
                spotRepository.getSpotDetail(spotId)
            }
            _spotDetails.value = mySpot
        }
    }

    fun toggleLike(spotId: Long, navController: NavHostController) {
        viewModelScope.launch {
            val result = apiCallback(navController) {
                spotRepository.toggleLike(spotId)
            }

            _spotDetails.value?.toggleLikeState()

            if (result != null) {
                Log.d("SPOT", "toggleLike: 좋아요 토글 성공")
                Log.d("SPOT", "toggleLike: ${_spotDetails.value?.liked}")
                loadSpotDetailById(spotId, navController)
                Log.d("SPOT", "toggleLike: ${_spotDetails.value?.liked}")
            } else {
                Log.d("SPOT", "toggleLike: 좋아요 토글 실패")
            }
        }
    }

    fun toggleScrap(spotId: Long, navController: NavHostController) {
        viewModelScope.launch {
            val result = apiCallback(navController) {
                spotRepository.toggleScrap(spotId)
            }

            _spotDetails.value?.toggleScrapState()

            if (result != null) {
                Log.d("SPOT", "toggleScrap: 스크램 토글 성공")
                Log.d("SPOT", "toggleScrap: ${_spotDetails.value?.scraped}")
                loadSpotDetailById(spotId, navController)
                Log.d("SPOT", "toggleScrap: ${_spotDetails.value?.scraped}")
            } else {
                Log.d("SPOT", "toggleScrap: 스크램 토글 실패")
            }
        }
    }
}