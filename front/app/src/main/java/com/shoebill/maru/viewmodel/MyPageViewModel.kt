package com.shoebill.maru.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyPageViewModel : ViewModel() {
    private val _tabIndex = MutableLiveData<Int>(0)
    val tabIndex: LiveData<Int> get() = _tabIndex

    fun switchTab(next: Int) {
        _tabIndex.value = next
    }
}