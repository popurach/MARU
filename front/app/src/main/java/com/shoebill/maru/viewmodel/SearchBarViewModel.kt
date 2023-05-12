package com.shoebill.maru.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shoebill.maru.model.data.Place
import com.shoebill.maru.model.repository.KakaoMapRepository
import com.shoebill.maru.util.PreferenceUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchBarViewModel @Inject constructor(
    val prefUtil: PreferenceUtil,
    private val kakaoMapRepository: KakaoMapRepository,
) : ViewModel() {
    private val _keyword = MutableLiveData<String>()
    private val _recommendedPlaces = MutableLiveData<List<Place>>(mutableListOf())

    val keyword: LiveData<String> get() = _keyword
    val recommendedPlaces: LiveData<List<Place>> get() = _recommendedPlaces

    fun updateKeyword(value: String) {
        Log.d("SearchBarViewModel", value)
        _keyword.value = value
    }

    fun getRecommendPlacesByKeyword(query: String) {
        viewModelScope.launch {
            val response = kakaoMapRepository.getRecommendPlacesByKeyword(query)
            if (response.isSuccessful) {
                val body = response.body()
                val recommendList = mutableListOf<Place>()
                for (doc in body?.documents!!) {
                    recommendList.add(
                        Place(
                            placeName = doc.place_name,
                            addressName = doc.address_name,
                            lng = doc.x.toDouble(),
                            lat = doc.y.toDouble(),
                        )
                    )
                }

                _recommendedPlaces.value = recommendList
            }
        }
    }

    fun resetRecommendPlacesByKeyword() {
        _recommendedPlaces.value = mutableListOf()
    }
}