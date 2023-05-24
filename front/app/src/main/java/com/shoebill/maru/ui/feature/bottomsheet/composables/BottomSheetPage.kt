package com.shoebill.maru.ui.page

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.heightIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.livedata.observeAsState
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
import com.shoebill.maru.ui.feature.bottomsheet.BottomSheetFrame
import com.shoebill.maru.ui.feature.bottomsheet.BottomSheetNavigatorViewModel
import com.shoebill.maru.ui.feature.bottomsheet.landmark.LandmarkMain
import com.shoebill.maru.ui.feature.bottomsheet.landmark.LandmarkPictureList
import com.shoebill.maru.ui.feature.bottomsheet.landmark.composables.LandMarkPicture
import com.shoebill.maru.ui.feature.bottomsheet.landmark.composables.LandmarkFirstVisit
import com.shoebill.maru.ui.feature.bottomsheet.spot.SpotDetail
import com.shoebill.maru.ui.feature.bottomsheet.spot.SpotList
import com.shoebill.maru.ui.feature.map.MapViewModel
import com.shoebill.maru.ui.main.NavigateViewModel
import com.shoebill.maru.ui.theme.MaruBackground

@Composable
fun BottomSheetPage(
    spotId: Long,
    bottomSheetNavigatorViewModel: BottomSheetNavigatorViewModel = hiltViewModel(),
    navController: NavHostController = rememberNavController(),
    navigatorViewModel: NavigateViewModel = viewModel(),
    mapViewModel: MapViewModel = hiltViewModel(),
    startDestination: String = "spot/list",
) {
    bottomSheetNavigatorViewModel.init(navController)
    mapViewModel.initBottomSheetController(navController)
    val visitingLandmarkId = mapViewModel.visitingLandmarkId.observeAsState()
    val arg = "id"

    Column(
        modifier = Modifier
            .heightIn(min = 40.dp, max = 670.dp)
    ) {
        val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
            "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
        }
        NavHost(navController = navController, startDestination = startDestination) {
            composable(
                "spot/list",
            ) {
                CompositionLocalProvider(
                    LocalViewModelStoreOwner provides viewModelStoreOwner
                ) {
                    BottomSheetFrame {
                        SpotList()
                    }
                }
            }
            composable(
                "spot/detail/{$arg}",
                arguments = listOf(navArgument(arg) {
                    type = NavType.LongType
                    defaultValue = -1L
                })
            ) {
                CompositionLocalProvider(
                    LocalViewModelStoreOwner provides viewModelStoreOwner
                ) {
                    var movedSpotId = it.arguments!!.getLong(arg)
                    if (movedSpotId == -1L) movedSpotId = spotId
                    BottomSheetFrame {
                        SpotDetail(movedSpotId)
                    }
                }
            }
            composable(
                "landmark/first/{landmarkId}",
                arguments = listOf(navArgument("landmarkId") {
                    type = NavType.LongType
                    defaultValue =
                        navigatorViewModel.navigator?.previousBackStackEntry?.savedStateHandle?.get(
                            "landmarkId"
                        ) ?: 1
                })
            ) {
                CompositionLocalProvider(
                    LocalViewModelStoreOwner provides viewModelStoreOwner
                ) {
                    val landmarkId = it.arguments!!.getLong("landmarkId")
                    BottomSheetFrame(
                        hasFabCamera = true,
                        landmarkId = landmarkId,
                        backgroundColor = MaruBackground,
                        cameraEnabled = visitingLandmarkId.value == landmarkId
                    ) {
                        LandmarkFirstVisit(landmarkId)
                    }
                }
            }
            composable(
                "landmark/main/{landmarkId}",
                arguments = listOf(navArgument("landmarkId") {
                    type = NavType.LongType
                    defaultValue =
                        navigatorViewModel.navigator?.previousBackStackEntry?.savedStateHandle?.get(
                            "landmarkId"
                        ) ?: 1
                })
            ) {
                CompositionLocalProvider(
                    LocalViewModelStoreOwner provides viewModelStoreOwner
                ) {
                    val landmarkId = it.arguments!!.getLong("landmarkId")
                    BottomSheetFrame(
                        hasFabCamera = true,
                        landmarkId = landmarkId,
                        backgroundColor = MaruBackground,
                        cameraEnabled = visitingLandmarkId.value == landmarkId
                    ) {
                        LandmarkMain(landmarkId = landmarkId)
                    }
                }
            }
            composable(
                "landmark/picture/{landmarkId}",
                arguments = listOf(navArgument("landmarkId") {
                    type = NavType.LongType
                    defaultValue = 0
                })
            ) {
                CompositionLocalProvider(
                    LocalViewModelStoreOwner provides viewModelStoreOwner
                ) {
                    val landmarkId = it.arguments!!.getLong("landmarkId")
                    BottomSheetFrame(
                        hasFabCamera = true,
                        landmarkId = landmarkId,
                        backgroundColor = MaruBackground,
                        cameraEnabled = visitingLandmarkId.value == landmarkId
                    ) {
                        LandMarkPicture()
                    }
                }
            }
            composable(
                "landmark/{landmarkId}/picture/list",
                arguments = listOf(navArgument("landmarkId") {
                    type = NavType.LongType
                    defaultValue = 0
                })
            ) {
                CompositionLocalProvider(
                    LocalViewModelStoreOwner provides viewModelStoreOwner
                ) {
                    val landmarkId = it.arguments!!.getLong("landmarkId")
                    BottomSheetFrame(
                        hasFabCamera = true,
                        landmarkId = landmarkId,
                        backgroundColor = MaruBackground,
                        cameraEnabled = visitingLandmarkId.value == landmarkId
                    ) {
                        LandmarkPictureList(landmarkId = landmarkId)
                    }
                }
            }
        }
    }
}