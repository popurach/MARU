package com.shoebill.maru.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shoebill.maru.model.data.Spot
import com.shoebill.maru.model.data.Stamp

class MyPageViewModel : ViewModel() {
    private val _tabIndex = MutableLiveData<Int>(0)
    val galleryList = ArrayList<Spot>()
    val stampList = ArrayList<Stamp>()
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

            stampList.add(
                Stamp(
                    id = i.toLong(),
                    imageUrl = "https://picsum.photos/id/${i + 10}/200/300"
                )
            )
        }
    }

    fun switchTab(next: Int) {
        _tabIndex.value = next
    }
}