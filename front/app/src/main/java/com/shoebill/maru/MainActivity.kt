package com.shoebill.maru

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.shoebill.maru.ui.page.LoginPage
import com.shoebill.maru.ui.page.MainPage
import com.shoebill.maru.ui.theme.MaruTheme
import com.shoebill.maru.util.PreferenceUtil
import com.shoebill.maru.viewmodel.MapViewModel
import com.shoebill.maru.viewmodel.NavigateViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var prefUtil: PreferenceUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefUtil.clear()
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
    navigateViewModel: NavigateViewModel = viewModel()
) {
    navigateViewModel.init(navController)

    NavHost(
        navController = navigateViewModel.navigator!!,
        startDestination = startDestination
    ) {
        composable("main") { backStackEntry ->
            val viewModel = hiltViewModel<MapViewModel>()
            viewModel.initFocusManager(LocalFocusManager.current)
            MainPage(mapViewModel = viewModel, navController = navigateViewModel.navigator!!)
        }
        /** 이곳에 화면 추가 **/

        composable("login") { navBackStackEntry ->

            LoginPage(navigateViewModel = navigateViewModel)
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