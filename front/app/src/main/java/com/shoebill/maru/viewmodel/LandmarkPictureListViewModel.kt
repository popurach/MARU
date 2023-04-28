package com.shoebill.maru.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LandmarkPictureListViewModel() : ViewModel() {
    private val _listData = mutableListOf<String>()
    private val _pictureList = MutableLiveData<List<String>>()
    val pictureList: LiveData<List<String>> get() = _pictureList

    init {
        _pictureList.value = _listData
        for (i in 10..1000) {
            addPicture(i)
        }
    }

    fun addPicture(num: Int) {
        _listData.add("https://picsum.photos/id/$num/200/300")
        _pictureList.value = _listData
    }

}