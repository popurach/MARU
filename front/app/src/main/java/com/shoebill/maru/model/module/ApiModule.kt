package com.shoebill.maru.model.module

import com.shoebill.maru.BuildConfig
import com.shoebill.maru.model.AppInterceptor
import com.shoebill.maru.model.interfaces.MemberApi
import com.shoebill.maru.model.interfaces.NoticeApi
import com.shoebill.maru.model.repository.MemberRepository
import com.shoebill.maru.model.repository.NoticeRepository
import com.shoebill.maru.util.PreferenceUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {
    @Provides
    fun provideBaseUrl(): String = BuildConfig.BASE_URL

    @Singleton
    @Provides
    fun provideRetrofit(client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(provideBaseUrl())
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                client
            ).build()

    @Singleton
    @Provides
    fun provideClient(appInterceptor: AppInterceptor): OkHttpClient =
        OkHttpClient().newBuilder().addInterceptor(appInterceptor).build()

    @Singleton
    @Provides
    fun provideInterceptor(
        prefUtil: PreferenceUtil,
    ): AppInterceptor =
        AppInterceptor(prefUtil = prefUtil)

    @Singleton
    @Provides
    fun provideMemberApi(retrofit: Retrofit): MemberApi =
        retrofit.create(MemberApi::class.java)

    @Singleton
    @Provides
    fun provideMemberRepository(memberApi: MemberApi, prefUtil: PreferenceUtil): MemberRepository =
        MemberRepository(memberApi, prefUtil)

    @Singleton
    @Provides
    fun provideNoticeApi(retrofit: Retrofit): NoticeApi =
        retrofit.create(NoticeApi::class.java)

    @Singleton
    @Provides
    fun provideNoticeRepository(noticeApi: NoticeApi): NoticeRepository =
        NoticeRepository(noticeApi)
}