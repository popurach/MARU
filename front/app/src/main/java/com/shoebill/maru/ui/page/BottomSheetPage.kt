package com.shoebill.maru.ui.page

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.heightIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.shoebill.maru.ui.component.bottomsheet.BottomSheetFrame
import com.shoebill.maru.ui.component.bottomsheet.landmark.LandMarkPicture
import com.shoebill.maru.ui.component.bottomsheet.landmark.LandmarkFirstVisit
import com.shoebill.maru.ui.component.bottomsheet.landmark.LandmarkMain
import com.shoebill.maru.ui.component.bottomsheet.landmark.LandmarkPictureList
import com.shoebill.maru.ui.component.bottomsheet.spotlist.SpotDetail
import com.shoebill.maru.ui.component.bottomsheet.spotlist.SpotList
import com.shoebill.maru.ui.theme.MaruBackground
import com.shoebill.maru.viewmodel.BottomSheetNavigatorViewModel
import com.shoebill.maru.viewmodel.MapViewModel
import com.shoebill.maru.viewmodel.NavigateViewModel

@Composable
fun BottomSheetPage(
    bottomSheetNavigatorViewModel: BottomSheetNavigatorViewModel = hiltViewModel(),
    navController: NavHostController = rememberNavController(),
    navigatorViewModel: NavigateViewModel = viewModel(),
    mapViewModel: MapViewModel = hiltViewModel(),
    startDestination: String = "spot/list",
) {
    bottomSheetNavigatorViewModel.init(navController)
    mapViewModel.initBottomSheetController(navController)
    Column(
        modifier = Modifier
            .heightIn(min = 40.dp, max = 670.dp)
    ) {
        val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
            "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
        }
        NavHost(navController = navController, startDestination = startDestination) {
            composable("spot/list") {
                CompositionLocalProvider(
                    LocalViewModelStoreOwner provides viewModelStoreOwner
                ) {
                    BottomSheetFrame {
                        SpotList()
                    }
                }
            }
            composable(
                "landmark/first/{id}",
                arguments = listOf(navArgument("id") {
                    type = NavType.LongType
                    defaultValue = 0
                })
            ) {
                CompositionLocalProvider(
                    LocalViewModelStoreOwner provides viewModelStoreOwner
                ) {
                    BottomSheetFrame(hasFabCamera = true, backgroundColor = MaruBackground) {
                        LandmarkFirstVisit(it.arguments!!.getLong("id"))
                    }
                }
            }
            composable(
                "spot/detail/{id}",
                arguments = listOf(navArgument("id") {
                    type = NavType.LongType
                    defaultValue =
                        navigatorViewModel.navigator?.previousBackStackEntry?.savedStateHandle?.get(
                            "spotId"
                        ) ?: 1
                })
            ) {
                CompositionLocalProvider(
                    LocalViewModelStoreOwner provides viewModelStoreOwner
                ) {
                    BottomSheetFrame {
                        SpotDetail(it.arguments?.getLong("id")!!)
                    }
                }
            }
            composable(
                "landmark/main/{landmarkId}",
                arguments = listOf(navArgument("landmarkId") {
                    type = NavType.LongType
                    defaultValue = 0
                })
            ) {
                CompositionLocalProvider(
                    LocalViewModelStoreOwner provides viewModelStoreOwner
                ) {
                    BottomSheetFrame(hasFabCamera = true, backgroundColor = MaruBackground) {
                        LandmarkMain(landmarkId = it.arguments!!.getLong("landmarkId"))
                    }
                }
            }
            composable("landmark/picture") {
                CompositionLocalProvider(
                    LocalViewModelStoreOwner provides viewModelStoreOwner
                ) {
                    BottomSheetFrame(hasFabCamera = true, backgroundColor = MaruBackground) {
                        LandMarkPicture()
                    }
                }
            }
            composable(
                "landmark/{id}/picture/list",
                arguments = listOf(navArgument("id") {
                    type = NavType.LongType
                    defaultValue = 0
                })
            ) {
                CompositionLocalProvider(
                    LocalViewModelStoreOwner provides viewModelStoreOwner
                ) {
                    BottomSheetFrame(hasFabCamera = true, backgroundColor = MaruBackground) {
                        LandmarkPictureList(landmarkId = it.arguments!!.getLong("id"))
                    }
                }
            }
        }
    }
}