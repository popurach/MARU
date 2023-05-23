package com.shoebill.maru.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.shoebill.maru.model.data.Spot
import com.shoebill.maru.model.repository.SpotRepository
import com.shoebill.maru.util.apiCallback
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
        viewModelScope.launch(Dispatchers.Main) {
            val mySpot = withContext(Dispatchers.IO) {
                apiCallback(navController) {
                    spotRepository.getSpotDetail(spotId)
                }
            }
            _spotDetails.value = mySpot
        }
    }

    fun toggleLike(spotId: Long, navController: NavHostController) {
        viewModelScope.launch {
            val result = apiCallback(navController) {
                spotRepository.toggleLike(spotId)
            }

            if (result == Unit) {
                loadSpotDetailById(spotId, navController)
            }
        }
    }

    fun toggleScrap(spotId: Long, navController: NavHostController) {
        viewModelScope.launch {
            val result = apiCallback(navController) {
                spotRepository.toggleScrap(spotId)
            }

            if (result == Unit) {
                loadSpotDetailById(spotId, navController)
            }
        }
    }
}