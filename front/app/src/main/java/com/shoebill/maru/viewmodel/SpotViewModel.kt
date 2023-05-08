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
}