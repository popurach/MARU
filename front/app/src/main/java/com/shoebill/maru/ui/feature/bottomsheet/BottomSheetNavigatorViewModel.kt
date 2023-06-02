package com.shoebill.maru.ui.feature.bottomsheet

import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController

class BottomSheetNavigatorViewModel : ViewModel() {
    private var _navController: NavHostController? = null
    val navController get() = _navController

    fun init(navController: NavHostController) {
        _navController = navController
    }
}