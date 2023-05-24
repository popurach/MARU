package com.shoebill.maru.ui.main

import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController

class NavigateViewModel : ViewModel() {
    private var _navigator: NavHostController? = null
    val navigator get() = _navigator

    fun init(navigator: NavHostController) {
        _navigator = navigator
    }
}