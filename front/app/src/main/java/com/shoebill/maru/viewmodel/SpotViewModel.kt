package com.shoebill.maru.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shoebill.maru.model.data.Spot
import com.shoebill.maru.model.repository.SpotRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SpotViewModel @Inject constructor(
    private val spotRepository: SpotRepository
) : ViewModel() {
    private val _spotDetails = MutableLiveData<Spot>(Spot())
    val spotDetails: LiveData<Spot> get() = _spotDetails

    fun loadSpotDetailById(spotId: Long) {
        
    }
}