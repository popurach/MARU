package com.shoebill.maru.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.shoebill.maru.BuildConfig
import com.shoebill.maru.model.data.LoginGoogleRequestModel
import com.shoebill.maru.model.repository.MemberRepository
import com.shoebill.maru.util.PreferenceUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val prefUtil: PreferenceUtil,
    private val memberRepository: MemberRepository,
) : ViewModel() {
    private val TAG = "LOGIN"
    private suspend fun kakaoApiLogin(token: OAuthToken) =
        withContext(viewModelScope.coroutineContext) {
            val deferredResponse = async {
                memberRepository.kakaoNaverLogin("KAKAO ${token.accessToken}")
            }
            val response = deferredResponse.await()
            if (response.isSuccessful) {
                val accessToken = response.headers()["access-token"]
                val refreshToken = response.headers()["refresh-token"]

                prefUtil.setString("accessToken", accessToken!!)
                Log.d("LOGIN", "accessToken -> $accessToken") // backend 테스트 용으로 남겨둠
                prefUtil.setString("refreshToken", refreshToken!!)
                Log.d("LOGIN", "refreshToken -> $refreshToken")

                true
            } else {
                false
            }
        }

    fun kakaoLogin(context: Context, navigator: NavHostController?) {
        // 카카오계정으로 로그인 공통 callback 구성
        // 카카오톡으로 로그인 할 수 없어 카카오계정으로 로그인할 경우 사용됨
        var isSuccess: Boolean
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e(TAG, "카카오 계정으로 로그인 실패", error)
            } else if (token != null) {
                Log.i(TAG, "카카오 계정으로 로그인 성공 ${token.accessToken}")
                viewModelScope.launch {
                    // back end 로그인 API 호출부분
                    isSuccess = kakaoApiLogin(token)
                    Log.d(TAG, "kakaoLogin: $isSuccess")
                    if (isSuccess) {
                        navigator?.navigate("main") {
                            popUpTo(0)
                        }
                    }
                }
            }
        }

        // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                if (error != null) {
                    Log.e(TAG, "카카오톡으로 로그인 실패", error)
                    // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우
                    // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소 처리
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }

                    // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                    UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
                } else if (token != null) {
                    Log.i(TAG, "카카오톡으로 로그인 성공 ${token.accessToken}")
                    viewModelScope.launch {
                        //back end 로그인 API 호출 부분
                        isSuccess = kakaoApiLogin(token)
                        if (isSuccess) {
                            navigator?.navigate("main") {
                                popUpTo(0)
                            }
                        }
                    }
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
        }
    }

    fun naverLogin(context: Context, navigator: NavHostController) {
        val oauthLoginCallback = object : OAuthLoginCallback {
            override fun onSuccess() {
                // 네이버 로그인 인증이 성공했을 때 수행할 코드 추가
                val accessToken = NaverIdLoginSDK.getAccessToken()
                viewModelScope.launch {
                    val isSuccess = naverApiLogin(accessToken)
                    if (isSuccess) {
                        navigator.navigate("main") {
                            popUpTo(0)
                        }
                    }
                }
            }

            override fun onFailure(httpStatus: Int, message: String) {
                val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                Toast.makeText(
                    context,
                    "errorCode:$errorCode, errorDesc:$errorDescription",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onError(errorCode: Int, message: String) {
                onFailure(errorCode, message)
            }
        }
        NaverIdLoginSDK.authenticate(context, oauthLoginCallback)
    }

    private suspend fun naverApiLogin(token: String?) =
        withContext(viewModelScope.coroutineContext) {
            val deferredResponse = async {
                memberRepository.kakaoNaverLogin("NAVER $token")
            }
            val response = deferredResponse.await()
            if (response.isSuccessful) {
                val accessToken = response.headers()["access-token"]
                val refreshToken = response.headers()["refresh-token"]

                prefUtil.setString("accessToken", accessToken!!)
                Log.d("LOGIN", "accessToken -> $accessToken") // backend 테스트 용으로 남겨둠
                prefUtil.setString("refreshToken", refreshToken!!)
                Log.d("LOGIN", "refreshToken -> $refreshToken")

                true
            } else {
                false
            }
        }

    fun handleSignInResult(completedTask: Task<GoogleSignInAccount>, navigator: NavHostController) =
        viewModelScope.launch {
            try {
                val authCode: String? =
                    completedTask.getResult(ApiException::class.java)?.serverAuthCode
                val isSuccess = authCode != null && googleApiLogin(authCode)
                if (isSuccess) {
                    navigator.navigate("main")
                }
            } catch (e: ApiException) {
                Log.w(TAG, "handleSignInResult: error" + e.statusCode)
            }
        }

    private suspend fun googleApiLogin(authCode: String) =
        withContext(viewModelScope.coroutineContext) {
            val deferredResponse = async(Dispatchers.IO) {
                memberRepository.googleLogin(
                    LoginGoogleRequestModel(
                        grant_type = "authorization_code",
                        client_id = BuildConfig.GOOGLE_CLIENT_ID,
                        client_secret = BuildConfig.GOOGLE_CLIENT_SECRET,
                        code = authCode
                    )
                )
            }
            val response = deferredResponse.await()
            if (response.isSuccessful) {
                Log.d("LOGIN", "GOOGLE ACCESS TOKEN : ${response.body()?.accessToken}")
                val accessToken = response.body()?.accessToken
                val myResponse = memberRepository.kakaoNaverLogin("GOOGLE $accessToken")

                if (myResponse.isSuccessful) {
                    val backAccessToken = myResponse.headers()["access-token"]
                    val backRefreshToken = myResponse.headers()["refresh-token"]

                    prefUtil.setString("accessToken", backAccessToken!!)
                    Log.d("LOGIN", "accessToken -> $backAccessToken") // backend 테스트 용으로 남겨둠
                    prefUtil.setString("refreshToken", backRefreshToken!!)
                    Log.d("LOGIN", "refreshToken -> $backRefreshToken")

                    true
                } else {
                    Log.d("LOGIN", "GOOGLE Login FAILED")

                    false
                }

            } else {
                false
            }
        }
}