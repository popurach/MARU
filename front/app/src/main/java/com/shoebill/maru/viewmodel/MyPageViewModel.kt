package com.shoebill.maru.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shoebill.maru.model.data.Spot

class MyPageViewModel : ViewModel() {
    private val _tabIndex = MutableLiveData<Int>(0)
    val galleryList = ArrayList<Spot>()

    val tabIndex: LiveData<Int> get() = _tabIndex

    init {
        for (i: Int in 1..100) {
            galleryList.add(
                Spot(
                    id = i.toLong(),
                    landmarkId = null,
                    imageUrl = "https://picsum.photos/id/$i/200/300",
                    isScrap = false,
                    hashTags = listOf("hashA", "hashB", "hashC")
                )
            )
        }
    }

    fun switchTab(next: Int) {
        _tabIndex.value = next
    }
}