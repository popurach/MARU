package com.shoebill.maru.ui.feature.drawer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DrawerViewModel : ViewModel() {
    private val _isOpen = MutableLiveData<Boolean>()

    val isOpen: LiveData<Boolean> get() = _isOpen

    fun updateOpenState(isOpen: Boolean) {
        _isOpen.value = isOpen
    }
}