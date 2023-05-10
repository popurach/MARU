package com.shoebill.maru.util

import androidx.navigation.NavHostController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

fun apiCallback(
    navController: NavHostController,
    vararg args: Any,
    scope: CoroutineScope,
    activeFunction: suspend (vararg: Any) -> Response<Any>
): Any? {
    var result: Any? = Unit
    scope.launch {
        val response = activeFunction(args)
        if (response.isSuccessful) {
            result = response.body()
        } else if (response.code() == 401) {
            withContext(Dispatchers.Main) {
                navController.navigate("login") {
                    popUpTo("login")
                }
            }
        }
    }
    return result
}
