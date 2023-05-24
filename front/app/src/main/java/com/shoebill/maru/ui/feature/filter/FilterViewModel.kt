package com.shoebill.maru.ui.feature.filter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FilterViewModel : ViewModel() {
    private val _selectedIndex = MutableLiveData<Int>()
    val selectedIndex: LiveData<Int> get() = _selectedIndex

    fun onClickChip(index: Int) {
        // TODO index 칩이 클릭 되었을 때
    }

}