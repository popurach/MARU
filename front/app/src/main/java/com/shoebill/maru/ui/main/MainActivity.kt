package com.shoebill.maru.ui.main

import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.shoebill.maru.ui.feature.map.MapViewModel
import com.shoebill.maru.ui.feature.notice.NoticeViewModel
import com.shoebill.maru.ui.main.composables.MainPage
import com.shoebill.maru.ui.page.AuctionPage
import com.shoebill.maru.ui.page.CameraPage
import com.shoebill.maru.ui.page.LoginPage
import com.shoebill.maru.ui.page.MyPage
import com.shoebill.maru.ui.page.NoticePage
import com.shoebill.maru.ui.theme.MaruTheme
import com.shoebill.maru.util.FcmMessageReceiver
import com.shoebill.maru.util.PreferenceUtil
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var prefUtil: PreferenceUtil
    var noticeViewModel: NoticeViewModel? = null
    var fcmMessageReceiver: FcmMessageReceiver? = null

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(fcmMessageReceiver)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        noticeViewModel = ViewModelProvider(this)[NoticeViewModel::class.java]
        fcmMessageReceiver = FcmMessageReceiver(noticeViewModel!!)
        registerReceiver(fcmMessageReceiver, IntentFilter("android.intent.action.FCM_MESSAGE"))

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            MaruTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 45.dp),
                    color = MaterialTheme.colors.background
                ) {
                    CompositionLocalProvider(LocalRippleTheme provides NoRippleTheme) {
                        MyApp(startDestination = if (prefUtil.isLogin()) "main/{id}" else "login")
                    }
                }
            }
        }
    }

    private object NoRippleTheme : RippleTheme {
        @Composable
        override fun defaultColor() = Color.Unspecified

        @Composable
        override fun rippleAlpha(): RippleAlpha = RippleAlpha(0.0f, 0.0f, 0.0f, 0.0f)
    }
}

@Composable
fun MyApp(
    navController: NavHostController = rememberNavController(),
    startDestination: String,
) {
    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    val navigateViewModel: NavigateViewModel = viewModel()
    navigateViewModel.init(navController)

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(
            "main/{id}",
            arguments = listOf(navArgument("id") {
                type = NavType.LongType
                defaultValue = -1L
            })
        ) {
            CompositionLocalProvider(LocalViewModelStoreOwner provides viewModelStoreOwner) {
                val viewModel = hiltViewModel<MapViewModel>()
                viewModel.initFocusManager(LocalFocusManager.current)
                MainPage(mapViewModel = viewModel, spotId = it.arguments!!.getLong("id"))
            }
        }
        /** 이곳에 화면 추가 **/

        composable("login") { navBackStackEntry ->
            CompositionLocalProvider(LocalViewModelStoreOwner provides viewModelStoreOwner) {
                LoginPage()
            }
        }

        composable("notice") {
            CompositionLocalProvider(LocalViewModelStoreOwner provides viewModelStoreOwner) {
                NoticePage(navController = navController)
            }
        }

        composable(
            "auction/{id}",
            arguments = listOf(navArgument("id") {
                type = NavType.LongType
                defaultValue = 0L
            })
        ) {
            CompositionLocalProvider(LocalViewModelStoreOwner provides viewModelStoreOwner) {
                AuctionPage(id = it.arguments?.getLong("id")!!)
            }
        }

        composable(
            "camera/{id}",
            arguments = listOf(navArgument("id") {
                type = NavType.LongType
                defaultValue = -1L
            })
        ) {
            CompositionLocalProvider(LocalViewModelStoreOwner provides viewModelStoreOwner) {
                CameraPage(landmarkId = it.arguments!!.getLong("id"),
                    onImageCaptured = { _, _ ->
                    }, onError = {
                    }
                )
            }
        }

        composable("mypage") {
            CompositionLocalProvider(LocalViewModelStoreOwner provides viewModelStoreOwner) {
                MyPage()
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    MaruTheme {
//        MyApp()
//    }
//}