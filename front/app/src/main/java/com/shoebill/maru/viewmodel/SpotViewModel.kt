package com.shoebill.maru.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shoebill.maru.model.data.Spot
import com.shoebill.maru.model.repository.SpotRepository
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

    fun loadSpotDetailById(spotId: Long) {
        viewModelScope.launch {
            try {
                _spotDetails.value = spotRepository.getSpotDetail(spotId)
            } catch (e: Error) {
                Log.d("SpotDetail", "fail to load spotDetail $e")
            }
        }
    }

    fun toggleLike(spotId: Long) {
        viewModelScope.launch {
            val response = spotRepository.toggleLike(spotId)
            if (response.isSuccessful) {
                Log.d("SPOT", "toggleLike: 좋아요 토글 성공")
                Log.d("SPOT", "toggleLike: ${_spotDetails.value?.liked}")
                loadSpotDetailById(spotId)
                Log.d("SPOT", "toggleLike: ${_spotDetails.value?.liked}")
            } else {
                Log.d("SPOT", "toggleLike: 좋아요 토글 실패")
            }
        }
    }

    fun toggleScrap(spotId: Long) {
        viewModelScope.launch {
            val response = spotRepository.toggleScrap(spotId)
            if (response.isSuccessful) {
                Log.d("SPOT", "toggleScrap: 스크램 토글 성공")
                Log.d("SPOT", "toggleScrap: ${_spotDetails.value?.scraped}")
                loadSpotDetailById(spotId)
                Log.d("SPOT", "toggleScrap: ${_spotDetails.value?.scraped}")
            } else {
                Log.d("SPOT", "toggleScrap: 스크램 토글 실패")
            }
        }
    }
}