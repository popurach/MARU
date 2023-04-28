package com.shoebill.maru.service

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.shoebill.maru.model.repository.MemberRepository
import com.shoebill.maru.util.PreferenceUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val prefUtil: PreferenceUtil,
    private val memberRepository: MemberRepository,
) : ViewModel() {
    private val TAG = "LOGIN"
    private fun kakaoApiLogin(token: OAuthToken) = runBlocking {
        val response: retrofit2.Response<Unit> =
            memberRepository.login("KAKAO ${token.accessToken}")
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

                // back end 로그인 API 호출부분
                isSuccess = kakaoApiLogin(token)
                if (isSuccess)
                    navigator?.navigate("main")
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

                    //back end 로그인 API 호출 부분
                    isSuccess = kakaoApiLogin(token)
                    if (isSuccess)
                        navigator?.navigate("main")
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
        }
    }
}