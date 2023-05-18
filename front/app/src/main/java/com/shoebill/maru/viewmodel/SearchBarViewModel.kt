package com.shoebill.maru.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.shoebill.maru.model.data.Place
import com.shoebill.maru.model.data.search.SearchTag
import com.shoebill.maru.model.repository.KakaoMapRepository
import com.shoebill.maru.model.repository.SearchRepository
import com.shoebill.maru.util.PreferenceUtil
import com.shoebill.maru.util.apiCallback
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchBarViewModel @Inject constructor(
    val prefUtil: PreferenceUtil,
    private val kakaoMapRepository: KakaoMapRepository,
    private val searchRepository: SearchRepository
) : ViewModel() {
    private val _keyword = MutableLiveData<String>()
    val keyword: LiveData<String> get() = _keyword

    private val _recommendedPlaces = MutableLiveData<List<Place>>(mutableListOf())
    val recommendedPlaces: LiveData<List<Place>> get() = _recommendedPlaces

    private val _recommendedTags = MutableLiveData<List<SearchTag>>(listOf())
    val recommendedTags get() = _recommendedTags

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

    private fun resetRecommendPlacesByKeyword() {
        _recommendedPlaces.value = mutableListOf()
    }

    fun loadRecommendTagsByKeyword(keyword: String, navController: NavHostController) {
        viewModelScope.launch {
            val newKeyword = keyword.replace("#", "").trim()
            if (newKeyword == "") return@launch
            val tagList = apiCallback(navController) {
                searchRepository.getElasticTagList(newKeyword)
            }
            _recommendedTags.value = tagList ?: listOf()
        }
    }

    private fun resetTags() {
        _recommendedTags.value = listOf()
    }

    fun resetRecommendList() {
        resetRecommendPlacesByKeyword()
        resetTags()
    }
}