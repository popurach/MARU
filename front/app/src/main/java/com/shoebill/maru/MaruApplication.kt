package com.shoebill.maru

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MaruApplication : Application() {
    override fun onCreate() {
        super.onCreate()
//        val keyHash = Utility.getKeyHash(this)
//        Log.d("HASH", keyHash)
        // kakao SDK 초기화
        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_APP_KEY)
    }
}