package com.shoebill.maru

import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.shoebill.maru.ui.page.AuctionPage
import com.shoebill.maru.ui.page.LoginPage
import com.shoebill.maru.ui.page.MainPage
import com.shoebill.maru.ui.page.MyPage
import com.shoebill.maru.ui.page.NoticePage
import com.shoebill.maru.ui.theme.MaruTheme
import com.shoebill.maru.util.FcmMessageReceiver
import com.shoebill.maru.util.PreferenceUtil
import com.shoebill.maru.viewmodel.MapViewModel
import com.shoebill.maru.viewmodel.NavigateViewModel
import com.shoebill.maru.viewmodel.NoticeViewModel
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
                    MyApp(startDestination = if (prefUtil.isLogin()) "main" else "login")
                }
            }
        }
    }
}

@Composable
fun MyApp(
    navController: NavHostController = rememberNavController(),
    startDestination: String = "main",
) {
    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    val navigateViewModel: NavigateViewModel = viewModel()
    navigateViewModel.init(navController)

    NavHost(
        navController = navigateViewModel.navigator!!,
        startDestination = startDestination
    ) {
        composable("main") { backStackEntry ->
            CompositionLocalProvider(LocalViewModelStoreOwner provides viewModelStoreOwner) {
                val viewModel = hiltViewModel<MapViewModel>()
                viewModel.initFocusManager(LocalFocusManager.current)
                MainPage(mapViewModel = viewModel)
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

        composable("auction") {
            CompositionLocalProvider(LocalViewModelStoreOwner provides viewModelStoreOwner) {
                AuctionPage()
            }
        }

        composable("mypage") {
            CompositionLocalProvider(LocalViewModelStoreOwner provides viewModelStoreOwner) {
                MyPage()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MaruTheme {
        MyApp()
    }
}