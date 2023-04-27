package com.shoebill.maru.ui.page

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.heightIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.shoebill.maru.ui.component.bottomsheet.BottomSheetFrame
import com.shoebill.maru.ui.component.bottomsheet.landmark.LandmarkFirstVisit
import com.shoebill.maru.ui.component.bottomsheet.spotlist.SpotDetail
import com.shoebill.maru.ui.component.bottomsheet.spotlist.SpotList
import com.shoebill.maru.ui.theme.MaruBackground
import com.shoebill.maru.viewmodel.BottomSheetNavigatorViewModel

@Composable
fun BottomSheetPage(
    bottomSheetNavigatorViewModel: BottomSheetNavigatorViewModel = hiltViewModel(),
    navController: NavHostController = rememberNavController()
) {
    bottomSheetNavigatorViewModel.init(navController)
    Column(
//        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .heightIn(min = 40.dp, max = 670.dp)
    ) {
        val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
            "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
        }
        NavHost(navController = navController, startDestination = "spot-list") {
            composable("spot-list") {
                CompositionLocalProvider(
                    LocalViewModelStoreOwner provides viewModelStoreOwner
                ) {
                    BottomSheetFrame {
                        SpotList()
                    }
                }
            }
            composable("landmark/first") {
                BottomSheetFrame(hasFabCamera = true, backgroundColor = MaruBackground) {
                    LandmarkFirstVisit()
                }
            }
            composable("spot/detail/{id}") {
                CompositionLocalProvider(
                    LocalViewModelStoreOwner provides viewModelStoreOwner
                ) {
                    BottomSheetFrame {
                        SpotDetail(it.arguments?.getString("id")!!.toLong())
                    }
                }

            }

        }
    }
//    Column(
//        horizontalAlignment = Alignment.CenterHorizontally,
//        modifier = Modifier
//            .heightIn(min = 40.dp, max = 670.dp)
//    ) {
////        BottomSheetIndicator()
////        SpotList()
//        BottomSheetFrame(hasFabCamera = true) {
//            LandmarkPictureList()
//        }
//    }
}