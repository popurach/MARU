package com.shoebill.maru.util

import android.util.Log
import androidx.navigation.NavHostController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

suspend fun <T> apiCallback(
    navController: NavHostController,
    activeFunction: suspend () -> Response<T>
): T? {
    val response = activeFunction()
    Log.d("apiCallback", "response code: ${response.code()}")
    if (response.isSuccessful) {
        return response.body()
    } else if (response.code() == 401) {
        withContext(Dispatchers.Main) {
            navController.navigate("login") {
                popUpTo(navController.graph.id) {
                    inclusive = true
                }
            }
        }
    }
    return null
}

