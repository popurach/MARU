package com.shoebill.maru.ui.feature.mypage.auction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuctionScreenViewModel @Inject constructor(
) : ViewModel() {

    private val _tabIndex = MutableLiveData(0)
    val tabIndex: LiveData<Int> get() = _tabIndex

    fun switchTab(newIndex: Int) {
        _tabIndex.value = newIndex
    }

}