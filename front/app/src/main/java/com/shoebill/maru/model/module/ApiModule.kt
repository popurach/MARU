package com.shoebill.maru.model.module

import android.content.Context
import com.shoebill.maru.model.AppInterceptor
import com.shoebill.maru.model.interfaces.MemberApi
import com.shoebill.maru.model.repository.MemberRepository
import com.shoebill.maru.util.PreferenceUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {
    @Provides
    fun provideBaseUrl() = "http://10.0.2.2:8080/"

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
        @ApplicationContext context: Context,
        prefUtil: PreferenceUtil,
    ): AppInterceptor =
        AppInterceptor(context = context, prefUtil = prefUtil)

    @Singleton
    @Provides
    fun provideMemberApi(retrofit: Retrofit): MemberApi =
        retrofit.create(MemberApi::class.java)

    @Singleton
    @Provides
    fun provideMemberRepository(memberApi: MemberApi): MemberRepository =
        MemberRepository(memberApi)
}